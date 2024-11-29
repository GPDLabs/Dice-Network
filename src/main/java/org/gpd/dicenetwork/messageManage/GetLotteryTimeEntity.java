package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetLotteryTimeEntity {
    @JsonProperty("lotteryPhase")
    private String lotteryPhase;
    @JsonProperty("lotteryTime")
    private String lotteryTime;

    public GetLotteryTimeEntity(){}

    public GetLotteryTimeEntity(String lotteryPhase, String lotteryTime)
    {
        this.lotteryPhase = lotteryPhase;
        this.lotteryTime = lotteryTime;
    }

    public String getLotteryPhase()
    {
        return lotteryPhase;
    }

    public String getLotteryTime()
    {
        return lotteryTime;
    }

    public void setLotteryPhase(String lotteryPhase)
    {
        this.lotteryPhase = lotteryPhase;
    }

    public void setLotteryTime(String lotteryTime)
    {
        this.lotteryTime = lotteryTime;
    }
}
