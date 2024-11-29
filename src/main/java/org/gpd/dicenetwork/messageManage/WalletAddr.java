package org.gpd.dicenetwork.messageManage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletAddr {
    @JsonProperty("walletAddr")
    private String walletAddr;
    @JsonProperty("walletPubKey")
    private String walletPubKey;
    @JsonProperty("signature")
    private String signature;

    public WalletAddr() {}

    public WalletAddr(String walletAddr, String walletPubKey, String signature) {
        this.walletAddr = walletAddr;
        this.walletPubKey = walletPubKey;
        this.signature = signature;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
