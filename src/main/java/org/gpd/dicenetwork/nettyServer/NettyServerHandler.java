package org.gpd.dicenetwork.nettyServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.ec.ECPoint;
import org.gpd.dicenetwork.config.FileManager;
import org.gpd.dicenetwork.config.ServerStatus;
import org.gpd.dicenetwork.config.ServerStatusManager;
import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;
import org.gpd.dicenetwork.lottery.service.LotteryListService;
import org.gpd.dicenetwork.messageManage.*;
import org.gpd.dicenetwork.secret.Secp256k1Utils;
import org.gpd.dicenetwork.utils.DateTimeUtils;
import org.gpd.dicenetwork.utils.StringUtils;
import org.gpd.dicenetwork.wallet.entity.WalletEntity;
import org.gpd.dicenetwork.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
//@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ClientManagerService clientManagerService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private LotteryListService lotteryListService;

    @Autowired
    private ServerStatusManager serverStatusManager;

//    public  NettyServerHandler(){}

//    public NettyServerHandler(WalletService walletService){
////        this.clientManagerService = clientManagerService;
//        this.walletService  = walletService;
//    }
    private StringBuilder buffer = new StringBuilder();
    private ScheduledFuture<?> validationTimer;
    private static final int VALIDATION_TIMEOUT = 5;
    private String macAddr;

    private boolean awaitingVerification = true;

    @Autowired
    public NettyServerHandler(ClientManagerService clientManagerService, WalletService walletService, LotteryListService lotteryListService, ServerStatusManager serverStatusManager) {
        this.clientManagerService = clientManagerService;
        this.walletService = walletService;
        this.lotteryListService = lotteryListService;
        this.serverStatusManager = serverStatusManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected");
        validationTimer = ctx.channel().eventLoop().schedule(() -> {
            System.out.println("Client did not send authentication in time. Closing channel.");
            ctx.close();
        }, VALIDATION_TIMEOUT, TimeUnit.SECONDS);

        // 客户端连接时添加到客户端列表
//        initializer.clients.put(ctx.channel(), "Client_" + initializer.clients.size());
//        System.out.println(initializer.clients.get(ctx.channel()) + " connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception, IOException {
        super.channelInactive(ctx);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
        log.info("channelInactive:" + macAddr + "-" + clientIp);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg == null || msg.toString().isEmpty()){
            return;
        }
        if (!(msg instanceof String)) {
            return;
        }
        String message = (String) msg;

        if (awaitingVerification) {
            validationTimer.cancel(false);
            validationTimer = null;
            handleVerificationMessage(ctx, message);
        }else{
            handleRegularMessage(ctx, message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handleVerificationMessage(ChannelHandlerContext ctx, String receivedMessage) {
        try {
            MessageEntity<?> message = MessageParser.parseMessage(receivedMessage);
            if (message == null || message.getBody() == null) {
                ctx.close();
                return;
            }
            WalletEntity wallet = new WalletEntity();
            if (message.getBody() instanceof LoginVqrEntity) {
                LoginVqrEntity loginVqr = (LoginVqrEntity) message.getBody();
                log.info(loginVqr.toString());

                byte[] messageHash = Secp256k1Utils.sha256Hex((loginVqr.getWalletAddr()+loginVqr.getQrId()).getBytes());

                System.out.println("login QR pubkey : " + loginVqr.getPubKey());
                if (!Secp256k1Utils.verifyBycompressedPublicKey(loginVqr.getPubKey(), messageHash, new Secp256k1Utils.ECDSASignature(loginVqr.getSignature()))) {
                    ctx.writeAndFlush(createMessageEntityJson(MessageName.loginVqr,
                            new ResultCodeEntity("Signature verification failed", 503), message.getHeader().getChecksum()));
                    ctx.close();
                    return;
                }

                macAddr = loginVqr.getQrId();
                wallet.setQrId(loginVqr.getQrId());
                wallet.setWalletAddr(loginVqr.getWalletAddr());
                wallet.setPubKey(loginVqr.getPubKey());
                wallet.setIsMaster(1);
                wallet.setOnlineStatus(1);
                walletService.loginQr(wallet);
                clientManagerService.addClient(wallet.getQrId(), ctx.channel());
                ctx.writeAndFlush(createMessageEntityJson(MessageName.loginVqr,
                        new ResultCodeEntity("Login successful", 200), message.getHeader().getChecksum()));
                awaitingVerification = false;
            }
        } catch (JsonProcessingException e) {
            log.error("JSON parsing exception", e);
            ctx.writeAndFlush(createMessageEntityJson(MessageName.loginVqr,
                    new ResultCodeEntity("Message format error", 500), ""));
            ctx.close();
        } catch (IllegalArgumentException e) {
            log.error("MessageLength is wrong!", e);
            ctx.writeAndFlush(createMessageEntityJson(MessageName.loginVqr,
                    new ResultCodeEntity("MessageLength is wrong!", 501), ""));
            ctx.close();
        } catch (Exception e) {
            log.error("unknown exception", e);
            ctx.writeAndFlush(
                    createMessageEntityJson(MessageName.loginVqr, new ResultCodeEntity("unknown exception", 502), ""));
            ctx.close();
        }
    }

    private void handleRegularMessage(ChannelHandlerContext ctx, String receivedMessage) {
        try {
            MessageEntity<?> message = MessageParser.parseMessage(receivedMessage);
            if (message == null || message.getBody() == null) {
                return;
            }
            switch (message.getHeader().getMessageName()) {
                case lotteryStart: {
                    if(message.getBody() instanceof LotteryStartEntity){
                        if(serverStatusManager.getStatus() != ServerStatus.COLLECTING){
                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryStart,
                                    new ResultCodeEntity("The current VQR server is in the non collection stage!", 511), message.getHeader().getChecksum()));
                            return;
                        }

                        LotteryStartEntity lotteryStart = (LotteryStartEntity) message.getBody();
                        if(DateTimeUtils.compareLocalDateTime(serverStatusManager.getLotteryTime(), DateTimeUtils.stringToLocalDateTime(lotteryStart.getLotteryStart())) != 0){
                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryStart,
                                    new ResultCodeEntity("The current lottery time is not the same as the server!", 512), message.getHeader().getChecksum()));
                            return;
                        }
                        List<LotteryHash> lotteryHashList = lotteryStart.getLotteryList();

                        List<WalletEntity> wallets = walletService.getWalletByQrId(lotteryStart.getQrId());
                        if(wallets.size() == 0){
                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryStart,
                                    new ResultCodeEntity("QrId is wrong!", 503), message.getHeader().getChecksum()));
                            return;
                        }
                        String masterPubKey = "";
                        int count = 0;
                        StringBuilder combinedHash = new StringBuilder();
                        List<LotteryListEntity> lotteryList = new ArrayList<>();
//                        Set<String> walletAddrsToCheck = new HashSet<>();
//                        for (LotteryHash lotteryHash : lotteryHashList) {
//                            walletAddrsToCheck.add(lotteryHash.getWalletAddr());
//                        }
//                        List<String> walletAddrsList = new ArrayList<>(walletAddrsToCheck);
                        List<WalletEntity> registeredWallets = walletService.getWalletsByQrId(lotteryStart.getQrId());
                        Map<String, WalletEntity> registeredWalletMap = new HashMap<>();
                        for (WalletEntity wallet : registeredWallets) {
                            registeredWalletMap.put(wallet.getWalletAddr(), wallet);
                            if (wallet.getIsMaster() == 1) {
                                masterPubKey = wallet.getPubKey();
                            }
                        }

                        for (LotteryHash lotteryHash : lotteryHashList) {
                            combinedHash.append(lotteryHash.getRandomHash());
                            if (registeredWalletMap.containsKey(lotteryHash.getWalletAddr())) {
                                LotteryListEntity lotteryListEntity = new LotteryListEntity(
                                        lotteryHash.getWalletAddr(),
                                        lotteryHash.getRandomHash(),
                                        DateTimeUtils.stringToLocalDateTime(lotteryStart.getLotteryStart())
                                );
                                lotteryList.add(lotteryListEntity);
                                count++;
                            }
                        }
                        if(count == 0){
                            return;
                        }
                        //验证签名
                        if(masterPubKey.equals("") || masterPubKey == null){
                            log.error("masterPubKey is wrong! QRID: " + lotteryStart.getQrId());
                            return;
                        }
                        ECPoint walletPubKey = Secp256k1Utils.decompressPublicKey(new BigInteger(masterPubKey, 16).toByteArray());
                        byte[] combinedHashBytes = Secp256k1Utils.sha256Hex(combinedHash.toString().getBytes());
                        if (!Secp256k1Utils.verify(walletPubKey, combinedHashBytes, new Secp256k1Utils.ECDSASignature(lotteryStart.getSignature()))) {
                            log.info("Signature verification failed");
                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryStart,
                                    new ResultCodeEntity("Tsignature verification failed", 504), message.getHeader().getChecksum()));
                            return;
                        }

                        lotteryListService.addLotteries(lotteryList);
                        log.info("MAC address: " + lotteryStart.getQrId() + ". The number of participants in the lottery is" + count);
                        ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryStart,
                                new ResultCodeEntity("The number of participants in the lottery is" + count, 200), message.getHeader().getChecksum()));

                    }
                }
                break;
                case lotteryResult:{
//                    System.out.println(receivedMessage.getBytes().length);
                    if(serverStatusManager.getStatus() != ServerStatus.RESULT){
                        ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryResult,
                                new ResultCodeEntity("The current VQR server is in the non result stage!", 521), message.getHeader().getChecksum()));
                        return;
                    }
                    if(message.getBody() instanceof LotteryResultEntity){
                        LotteryResultEntity lotteryResult = (LotteryResultEntity) message.getBody();
                        //验证MAC地址，钱包地址，钱包公钥，随机数哈希值
//                        if(clientManagerService.findKeyByChannel(ctx.channel()) != serverStatusManager.getQrId()){
//                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryResult,
//                                    new ResultCodeEntity("MAC address verification failed", 522), message.getHeader().getChecksum()));
//                            return;
//                        }
//                        if(lotteryResult.getWinnerWallet() != serverStatusManager.getWalletAddr()){
//                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryResult,
//                                    new ResultCodeEntity("Wallet address verification failed", 523), message.getHeader().getChecksum()));
//                            return;
//                        }
                        if(lotteryResult.getCurrentPacket() != serverStatusManager.getCurrentPacket() + 1){
                            ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryResult,
                                    new ResultCodeEntity("Packet number verification failed", 524), message.getHeader().getChecksum()));
                            return;
                        }
                        //生成文件名，写入随机数文件
//                        fileManager.generateFileName(serverStatusManager.getLotteryTime().toString(), serverStatusManager.getWalletHash(), 0);
//                        String fileName = fileManager.writeFile(lotteryResult.getRandom(), DateTimeUtils.LocalDateTimeToString2(serverStatusManager.getLotteryTime()), serverStatusManager.getWalletHash(), 0);
                        System.out.println("The number of the received data packet:" + lotteryResult.getCurrentPacket());
                        serverStatusManager.addRandomBytes(StringUtils.hexStringToByteArray(lotteryResult.getRandom()));
                        lotteryResult.setRandom(null);
                        //返回消息
                        ctx.writeAndFlush(createMessageEntityJson(MessageName.lotteryResult,
                                lotteryResult, message.getHeader().getChecksum()));

                        return;
                    }
                }
                break;
                case registerWallet:{
                    if(message.getBody() instanceof RegisterWalletEntity){
                        RegisterWalletEntity registerWallet = (RegisterWalletEntity) message.getBody();
                        List<WalletAddr> walletAddrs = registerWallet.getWalletList();
                        int count = 0;
                        for(WalletAddr walletAddr:walletAddrs){
                            BigInteger publicKey = new BigInteger(walletAddr.getWalletPubKey(), 16);
                            ECPoint walletPubKey = Secp256k1Utils.decompressPublicKey(publicKey.toByteArray());
                            byte[] walletAddrHash = Secp256k1Utils.sha256Hex(walletAddr.getWalletAddr().getBytes());
                            if(!Secp256k1Utils.verify(walletPubKey, walletAddrHash, new Secp256k1Utils.ECDSASignature(walletAddr.getSignature()))){
                                WalletEntity wallet = new WalletEntity();
                                wallet.setQrId(registerWallet.getQrId());
                                wallet.setWalletAddr(walletAddr.getWalletAddr());
                                wallet.setPubKey(walletAddr.getWalletPubKey());
                                wallet.setIsMaster(0);
                                walletService.registerWallet(wallet);
                                count++;
                            }
                        }
                        log.info("Register wallet:" + walletAddrs.size() + "#####success: " + count);
                        ctx.writeAndFlush(createMessageEntityJson(MessageName.registerWallet,
                                new ResultCodeEntity("Register successful,count:" + count, 200), message.getHeader().getChecksum()));

                    }
                }
                break;
                case getLotteryTime:{
                    ctx.writeAndFlush(createMessageEntityJson(
                            MessageName.getLotteryTime,
                            new GetLotteryTimeEntity(serverStatusManager.getStatus().name(), DateTimeUtils.LocalDateTimeToString(serverStatusManager.getLotteryTime())),
                            message.getHeader().getChecksum())
                    );
                }
                break;
                default:{

                }
            }
        } catch (JsonProcessingException e) {
            log.error("JSON parsing exception", e);
            ctx.writeAndFlush(createMessageEntityJson(MessageName.unknown,
                    new ResultCodeEntity("Message format error", 500), ""));
            ctx.close();
        } catch (IllegalArgumentException e) {
            log.error("MessageLength is wrong!", e);
            ctx.writeAndFlush(createMessageEntityJson(MessageName.unknown,
                    new ResultCodeEntity("MessageLength is wrong!", 501), ""));
            ctx.close();
        } catch (Exception e) {
            log.error("unknown exception", e);
            ctx.writeAndFlush(
                    createMessageEntityJson(MessageName.unknown, new ResultCodeEntity("unknown exception", 502), ""));
            ctx.close();
        }
    }

    private String createMessageEntityJson(MessageName messageName, Object body, String chrcksum) {
        try {
            MessageHeaderEntity header = new MessageHeaderEntity(
                    0,
                    MessageType.response,
                    messageName,
                    "1.0",
                    chrcksum);

            MessageEntity<?> messageEntity = new MessageEntity<>(header, body);

            // 使用 ObjectMapper 将整个 MessageEntity 转换为 JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(messageEntity);

            // 解析 JSON 以更新消息长度
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode bodyNode = rootNode.get("body");
            System.out.println("bodyNode：" + bodyNode.toString());
            int bodyLength = bodyNode.toString().length();
//            System.out.println("body：" + bodyNode.toString() + "，长度:" + bodyLength);

            // 更新消息头中的长度
            ((ObjectNode) rootNode.get("header")).put("messageLength", bodyLength);

            // 重新生成 JSON 字符串
            return rootNode.toString();
        } catch (JsonProcessingException e) {
            log.error("An error occurred while creating the message entity JSON", e);
            return null;
        }
    }
}
