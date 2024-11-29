package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginVqrEntity {

    private String qrId;

    private String walletAddr;

    private String pubKey;

    private String signature;

    public LoginVqrEntity() {}

    @JsonCreator
    public LoginVqrEntity(
            @JsonProperty("qrId") String qrId,
            @JsonProperty("walletAddr") String walletAddr,
            @JsonProperty("pubKey") String pubKey,
            @JsonProperty("signature") String signature) {
        this.qrId = qrId;
        this.walletAddr = walletAddr;
        this.pubKey = pubKey;
        this.signature = signature;
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

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
