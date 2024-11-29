package org.gpd.dicenetwork.wallet.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("wallet_list")
public class WalletEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("qr_id")
    private String qrId;

    @TableField("wallet_addr")
    private String walletAddr;

    @TableField("pub_key")
    private String pubKey;

    @TableField("is_master")
    private Integer isMaster;

    @TableField("online_status")
    private Integer onlineStatus;

    @TableField("wallet_status")
    private Integer walletStatus;

    public WalletEntity(String qrId, String walletAddr, String pubKey, Integer isMaster) {
        this.qrId = qrId;
        this.walletAddr = walletAddr;
        this.pubKey = pubKey;
        this.isMaster = isMaster;
        this.onlineStatus = Integer.valueOf(0);
        this.walletStatus = Integer.valueOf(0);
    }

    public WalletEntity() {
        this.isMaster = Integer.valueOf(0);
        this.onlineStatus = Integer.valueOf(0);
        this.walletStatus = Integer.valueOf(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(Integer walletStatus) {
        this.walletStatus = walletStatus;
    }

    @Override
    public String toString() {
        return "WalletEntity{" +
                "id=" + id +
                ", qrId='" + qrId + '\'' +
                ", walletAddr='" + walletAddr + '\'' +
                ", pubKey='" + pubKey + '\'' +
                ", isMaster=" + isMaster +
                ", onlineStatus=" + onlineStatus +
                ", walletStatus=" + walletStatus +
                '}';
    }
}
