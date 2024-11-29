package org.gpd.dicenetwork.messageManage;

import java.util.List;

public class LotteryStartEntity {
//    @JsonProperty("qrId")
    private String qrId;
    private String lotteryStart;
    private String isJoin;
    private List<LotteryHash> lotteryList;
    private String signature;

    public LotteryStartEntity() {}

    public LotteryStartEntity(String qrId, String lotteryStart, String isJoin, List<LotteryHash> lotteryList) {

        this.qrId = qrId;
        this.lotteryStart = lotteryStart;
        this.isJoin = isJoin;
        this.lotteryList = lotteryList;
    }


    public String getQrId() {
        return qrId;
    }

    public void setQrId(String qrId) {
        this.qrId = qrId;
    }

    public String getLotteryStart() {
        return lotteryStart;
    }

    public void setLotteryStart(String lotteryStart) {
        this.lotteryStart = lotteryStart;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public List<LotteryHash> getLotteryList() {
        return lotteryList;
    }

    public void setLotteryList(List<LotteryHash> lotteryList) {
        this.lotteryList = lotteryList;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
