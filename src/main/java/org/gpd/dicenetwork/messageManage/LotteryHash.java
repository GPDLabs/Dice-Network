package org.gpd.dicenetwork.messageManage;

public class LotteryHash {
    private String walletAddr;
    private String randomHash;

    public LotteryHash() {}

    public LotteryHash(String walletAddr, String walletPubKey, String signature, String randomHash) {
        this.walletAddr = walletAddr;
        this.randomHash = randomHash;
    }

    public String getWalletAddr() {
        return walletAddr;
    }

    public void setWalletAddr(String walletAddr) {
        this.walletAddr = walletAddr;
    }


    public String getRandomHash() {
        return randomHash;
    }

    public void setRandomHash(String randomHash) {
        this.randomHash = randomHash;
    }

}
