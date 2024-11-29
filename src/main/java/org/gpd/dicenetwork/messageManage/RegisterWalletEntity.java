package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RegisterWalletEntity {
    @JsonProperty("qrId")
    private String qrId;
    @JsonProperty("walletList")
    private List<WalletAddr> walletList;

    public RegisterWalletEntity() {
    }


    public RegisterWalletEntity(String qrId, List<WalletAddr> walletList) {
        this.qrId = qrId;
        this.walletList = walletList;

    }

    public String getQrId() {
        return qrId;
    }

    public void setQrId(String qrId) {
        this.qrId = qrId;
    }

    public List<WalletAddr> getWalletList() {
        return walletList;
    }
    public void setWalletList(List<WalletAddr> walletList) {
        this.walletList = walletList;
    }
}
