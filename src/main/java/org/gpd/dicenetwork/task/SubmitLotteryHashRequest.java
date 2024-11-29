package org.gpd.dicenetwork.task;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitLotteryHashRequest {
    @JsonProperty("lottery_time")
    private String lotteryTime;

    @JsonProperty("lottery_hash")
    private String lotteryHash;

    @JsonProperty("lucky_hash")
    private String luckyHash;

    @JsonProperty("lucky_wallet")
    private String luckyWallet;

    @JsonProperty("validity")
    private String validity;

    public SubmitLotteryHashRequest() {}

    public SubmitLotteryHashRequest(String lotteryTime, String lotteryHash, String luckyHash, String luckyWallet, String validity) {
        this.lotteryTime = lotteryTime;
        this.lotteryHash = lotteryHash;
        this.luckyHash = luckyHash;
        this.luckyWallet = luckyWallet;
        this.validity = validity;
    }

    public String getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(String lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public String getLuckyHash() {
        return luckyHash;
    }

    public void setLuckyHash(String luckyHash) {
        this.luckyHash = luckyHash;
    }

    public String getLotteryHash() {
        return lotteryHash;
    }

    public void setLotteryHash(String lotteryHash) {
        this.lotteryHash = lotteryHash;
    }

    public String getLuckyWallet() {
        return luckyWallet;
    }

    public void setLuckyWallet(String luckyWallet) {
        this.luckyWallet = luckyWallet;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
