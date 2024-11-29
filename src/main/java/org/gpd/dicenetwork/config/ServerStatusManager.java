package org.gpd.dicenetwork.config;

import org.gpd.dicenetwork.utils.DateTimeUtils;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class ServerStatusManager {

    private volatile ServerStatus status = ServerStatus.COLLECTING;
    private volatile LocalDateTime lotteryTime = DateTimeUtils.getNextTenMinuteDateTime();
    private volatile String qrId;
    private volatile String walletAddr;
    private volatile String walletHash;
    private volatile String checksum;
    private volatile byte[] randromBytes = new byte[0];
    private volatile int currentPacket;

    private volatile LocalDateTime nextLotteryTime;

    private static final int MAX_RANDOM_BYTES_SIZE = 1024 * 1024;
    private static final int TOTAL_PACKET = 128;


    public ServerStatusManager(){

    }

    public String getWalletHash() {
        return walletHash;
    }

    public void setWalletHash(String walletHash) {
        this.walletHash = walletHash;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public LocalDateTime getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(LocalDateTime lotteryTime) {
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

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public int getCurrentPacket() {
        return currentPacket;
    }

    public void setCurrentPacket(int currentPacket) {
        this.currentPacket = currentPacket;
    }

    public void addRandomBytes(byte[] chunk) {
        synchronized (this) {
            if (randromBytes.length + chunk.length > MAX_RANDOM_BYTES_SIZE) {
                throw new IllegalArgumentException("Exceeded maximum random bytes size of 1MB");
            }
            System.out.println("Adding " + chunk.length + " bytes to random bytes");
            byte[] newRandromBytes = Arrays.copyOf(randromBytes, randromBytes.length + chunk.length);
            System.arraycopy(chunk, 0, newRandromBytes, randromBytes.length, chunk.length);

            // 更新 randromBytes
            randromBytes = newRandromBytes;
            currentPacket++;
        }
    }

    public byte[] getRandomBytes() {
        return randromBytes;
    }

    public boolean isReceiveFinish() {
        if(currentPacket == TOTAL_PACKET){
            return true;
        }else{
            return false;
        }
    }

    public void removeWinner(LocalDateTime nextLotteryTime) {
        this.status = ServerStatus.COLLECTING;
        this.lotteryTime = nextLotteryTime;
        this.qrId = null;
        this.walletAddr = null;
        this.walletHash = null;
        this.checksum = null;
        this.randromBytes = new byte[0];
        this.currentPacket = 0;
    }
}


