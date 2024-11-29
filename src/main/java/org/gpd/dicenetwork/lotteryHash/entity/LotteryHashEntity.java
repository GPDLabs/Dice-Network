package org.gpd.dicenetwork.lotteryHash.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lottery_hash_list")
public class LotteryHashEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("lottery_time")
    private LocalDateTime lotteryTime;

    @TableField("lottery_hash")
    private String lotteryHash;

    @TableField("lucky_wallet")
    private String luckyWallet;

    @TableField("randrom_file")
    private String randromFile;

    @TableField("file_status")
    private Integer fileStatus;

    @TableField("status")
    private Integer status;

    public LotteryHashEntity(){}

    public LotteryHashEntity(Integer id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lotteryTime, String lotteryHash, String luckyWallet, String randromFile, Integer fileStatus, Integer status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lotteryTime = lotteryTime;
        this.lotteryHash = lotteryHash;
        this.luckyWallet = luckyWallet;
        this.randromFile = randromFile;
        this.fileStatus = fileStatus;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(LocalDateTime lotteryTime) {
        this.lotteryTime = lotteryTime;
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

    public String getRandromFile() {
        return randromFile;
    }

    public void setRandromFile(String randromFile) {
        this.randromFile = randromFile;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Integer fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
