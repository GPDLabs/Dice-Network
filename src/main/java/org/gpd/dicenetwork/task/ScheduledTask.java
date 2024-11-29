package org.gpd.dicenetwork.task;


import lombok.extern.slf4j.Slf4j;
import org.gpd.dicenetwork.config.FileManager;
import org.gpd.dicenetwork.config.ServerStatus;
import org.gpd.dicenetwork.config.ServerStatusManager;
import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;
import org.gpd.dicenetwork.lottery.service.LotteryListService;
import org.gpd.dicenetwork.lotteryHash.entity.LotteryHashEntity;
import org.gpd.dicenetwork.lotteryHash.service.LotteryHashService;
import org.gpd.dicenetwork.messageManage.LotteryResultEntity;
import org.gpd.dicenetwork.messageManage.LuckyWalletEntity;
import org.gpd.dicenetwork.messageManage.MessageName;
import org.gpd.dicenetwork.messageManage.MessageParser;
import org.gpd.dicenetwork.nettyServer.ClientManagerService;
import org.gpd.dicenetwork.secret.Secp256k1Utils;
import org.gpd.dicenetwork.utils.DateTimeUtils;
import org.gpd.dicenetwork.utils.StringUtils;
import org.gpd.dicenetwork.wallet.entity.WalletEntity;
import org.gpd.dicenetwork.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private ServerStatusManager serverStatusManager;

    @Autowired
    private LotteryListService lotteryListService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ClientManagerService clientManagerService;

    @Autowired
    private LotteryHashService lotteryHashService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private Submit submit;

    private final Lottery lottery = new Lottery();

    @Scheduled(cron = "${drawing.cron}")
    @Async("asyncScheduledPool")
    public void cron() {
        log.info("Lottery start:" + serverStatusManager.getLotteryTime());
        LuckyWalletEntity luckyWalletEntity = new LuckyWalletEntity();
        try{
            serverStatusManager.setStatus(ServerStatus.LOTTERYING);
            List<LotteryListEntity> lotteries = lotteryListService.getLotteriesByLotteryTime(serverStatusManager.getLotteryTime());

            if(lotteries == null || lotteries.size() == 0){
                log.info("No QR take part in the Lottery. LotteryTime:" + serverStatusManager.getLotteryTime());
                return;
            }

            // 添加哈希值到 Lottery
            for (LotteryListEntity lotteryEntity : lotteries) {
                lottery.addHash(lotteryEntity.getWalletAddr(), lotteryEntity.getRandomHash());
            }

            // 执行摇号
            boolean result = lottery.lottery();
            if (result) {
                //存储摇号哈希
                String lotteryHash = StringUtils.byteArrayToHexString(lottery.getLotteryHash());
                LotteryHashEntity lotteryHashEntity1 = new LotteryHashEntity();
                lotteryHashEntity1.setLotteryTime(serverStatusManager.getLotteryTime());
                lotteryHashEntity1.setLotteryHash(lotteryHash);
                lotteryHashEntity1.setStatus(0);
                lotteryHashEntity1.setFileStatus(0);
                if(!lotteryHashService.insertLotteryHash(lotteryHashEntity1)){
                    log.info("Failed to insert lottery hash.");
                    return;
                }

                //更新状态,发送随机数上传通知
                WalletEntity luakyWallet = walletService.getWalletByWalletAddr(lottery.getWinner());
                if(luakyWallet == null){
                    log.info("No valid hashes to perform the lottery.");
                }

                serverStatusManager.setQrId(luakyWallet.getQrId());
                serverStatusManager.setWalletAddr(luakyWallet.getWalletAddr());
                for(LotteryListEntity lotteryEntity : lotteries){
                    if(lotteryEntity.getWalletAddr().equals(lottery.getWinner())){
                        serverStatusManager.setWalletHash(lotteryEntity.getRandomHash());
                    }
                }

                String chrcksum = StringUtils.generateRandomCode();
                serverStatusManager.setChecksum(chrcksum);

                LotteryResultEntity lotteryResultEntity = new LotteryResultEntity();
                lotteryResultEntity.setWinnerWallet(lottery.getWinner());

                //发送随机数上传通知
                serverStatusManager.setStatus(ServerStatus.RESULT);
                clientManagerService.sendMessageToClient(luakyWallet.getQrId(), MessageParser.createMessageEntityJson(MessageName.lotteryResult, lotteryResultEntity, chrcksum));

                for (int i = 0; i < 60; i++) {
                    if (serverStatusManager.isReceiveFinish()) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("Task interrupted: " + e.getMessage());
                        break;
                    }
                }

                if(serverStatusManager.isReceiveFinish()){
                    //校验哈希值
//                System.out.println("RandomBytes:" + serverStatusManager.getRandomBytes().length);
                    byte[] verifyHash = Secp256k1Utils.sha256Hex(serverStatusManager.getRandomBytes());

//                System.out.println("verifyHash:" + StringUtils.byteArrayToHexString(verifyHash));
//                System.out.println("walletHash:" + serverStatusManager.getWalletHash());
                    if(!StringUtils.byteArrayToHexString(verifyHash).equals(serverStatusManager.getWalletHash())){
                        log.info("Hash verification failed.");
                        return;
                    }
                    log.info("Lottery time：" + DateTimeUtils.LocalDateTimeToString(serverStatusManager.getLotteryTime()) + "  ###reulst:" + "Hash verification passed.");

                    //校验测试结果

                    //创建文件，写入随机数文件

                    String filePath;
                    filePath = fileManager.writeFile(
                            serverStatusManager.getRandomBytes(),
                            DateTimeUtils.LocalDateTimeToString2(serverStatusManager.getLotteryTime()),
                            serverStatusManager.getWalletHash(),
                            0
                    );
                    log.info("filePath:" + filePath);

                    //更新数据库lottery_hash_list
                    LotteryHashEntity lotteryHashEntity2 = lotteryHashService.selectByLotteryHash(lotteryHash);
                    lotteryHashEntity2.setLuckyWallet(serverStatusManager.getWalletAddr());
                    lotteryHashEntity2.setRandromFile(filePath);
                    lotteryHashEntity2.setFileStatus(2);
                    lotteryHashEntity2.setStatus(1);
                    lotteryHashService.updateLotteryHashById(lotteryHashEntity2);

//                    SubmitLotteryHashRequest request = new SubmitLotteryHashRequest(
//                            DateTimeUtils.LocalDateTimeToString(serverStatusManager.getLotteryTime()),
//                            lotteryHash,
//                            serverStatusManager.getWalletHash(),
//                            serverStatusManager.getWalletAddr(),
//                            "success"
//                    );
//                    submit.submitLotteryHash(request);

                    //成功的摇号
                    luckyWalletEntity.setLotteryTime(DateTimeUtils.LocalDateTimeToString(serverStatusManager.getLotteryTime()));
                    luckyWalletEntity.setQrId(serverStatusManager.getQrId());
                    luckyWalletEntity.setWalletAddr(luakyWallet.getWalletAddr());
                    LocalDateTime nextLotteryStart = DateTimeUtils.getNextTenMinuteDateTime();
                    luckyWalletEntity.setNextLotteryStart(DateTimeUtils.LocalDateTimeToString(nextLotteryStart));

                }else{
                    log.warn("Random number reception failed.");
                }
            } else {
                log.info("No valid hashes to perform the lottery.");
            }
        } catch (IOException e){
            log.error("文件写入异常：" + e.getMessage());
        }catch (Exception e) {
            log.error("Lottery error:" + e.getMessage());
        } finally {
            //广播摇号结果
            LocalDateTime nextLotteryStart = DateTimeUtils.getNextTenMinuteDateTime();
            luckyWalletEntity.setNextLotteryStart(DateTimeUtils.LocalDateTimeToString(nextLotteryStart));
            clientManagerService.sendMessageToAllClients(
                    MessageParser.createMessageEntityJson(MessageName.luckyWallet, luckyWalletEntity, ""));
            //清除本轮摇号信息
            serverStatusManager.removeWinner(nextLotteryStart);        }


    }
}
