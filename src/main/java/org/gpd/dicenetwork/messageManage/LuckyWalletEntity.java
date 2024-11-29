package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LuckyWalletEntity {
    @JsonProperty("lotteryTime")
    private String lotteryTime;
    @JsonProperty("qrId")
    private String qrId;
    @JsonProperty("walletAddr")
    private String walletAddr;
    @JsonProperty("walletPubKey")
    private String walletPubKey;
    @JsonProperty("nextLotteryStart")
    private String nextLotteryStart;

    public LuckyWalletEntity() {
    }

    public LuckyWalletEntity(String lotteryTime, String qrId, String walletAddr, String walletPubKey, String nextLotteryStart) {
        this.lotteryTime = lotteryTime;
        this.qrId = qrId;
        this.walletAddr = walletAddr;
        this.walletPubKey = walletPubKey;
        this.nextLotteryStart = nextLotteryStart;
    }

    public String getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(String lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public String getQrId() {
        return qrId;
    }

    public void setQrId(String qrId) {
        this.qrId = qrId;
    }

    public String getWalletAddr() {
        return walletAddr;
    }

    public void setWalletAddr(String walletAddr) {
        this.walletAddr = walletAddr;
    }

    public String getWalletPubKey() {
        return walletPubKey;
    }

    public void setWalletPubKey(String walletPubKey) {
        this.walletPubKey = walletPubKey;
    }

    public String getNextLotteryStart() {
        return nextLotteryStart;
    }

    public void setNextLotteryStart(String nextLotteryStart) {
        this.nextLotteryStart = nextLotteryStart;
    }
}
