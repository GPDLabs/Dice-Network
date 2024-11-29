package org.gpd.dicenetwork.lottery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@TableName("lottery_list")
public class LotteryListEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("wallet_addr")
    private String walletAddr;

    @TableField("random_hash")
    private String randomHash;

    @TableField("lottery_time")
    private LocalDateTime lotteryTime; // 注意：这里使用了LocalDateTime代替TIMESTAMP

    @TableField("is_winner")
    private boolean isWinner;

    public LotteryListEntity() {
        this.isWinner = false;
    }

    public LotteryListEntity(String walletAddr, String randomHash, LocalDateTime lotteryTime) {
        this.walletAddr = walletAddr;
        this.randomHash = randomHash;
        this.lotteryTime = lotteryTime;
        this.isWinner = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(LocalDateTime lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }
}
