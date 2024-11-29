package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LotteryResultEntity {
    @JsonProperty("winnerWallet")
    private String winnerWallet;
    @JsonProperty("totalPackets")
    private int totalPackets;
    @JsonProperty("currentPacket")
    private int currentPacket;
    @JsonProperty("random")
    private String random;

    public LotteryResultEntity() {
    }

    public LotteryResultEntity(String winnerWallet, int totalPackets, int currentPacket) {
        this.winnerWallet = winnerWallet;
        this.totalPackets = totalPackets;
        this.currentPacket = currentPacket;
    }

    public String getWinnerWallet() {
        return winnerWallet;
    }

    public void setWinnerWallet(String winnerWallet) {
        this.winnerWallet = winnerWallet;
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public void setTotalPackets(int totalPackets) {
        this.totalPackets = totalPackets;
    }

    public int getCurrentPacket() {
        return currentPacket;
    }

    public void setCurrentPacket(int currentPacket) {
        this.currentPacket = currentPacket;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }
}
