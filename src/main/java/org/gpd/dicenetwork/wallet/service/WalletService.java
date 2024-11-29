package org.gpd.dicenetwork.wallet.service;

import org.gpd.dicenetwork.wallet.entity.WalletEntity;

import java.util.List;

public interface WalletService {
    List<WalletEntity> getAllWallets();
    WalletEntity getWalletByWalletAddr(String walletAddr);
    WalletEntity selectFirstByQrIdAndIsMaster(String qrId, int isMaster);
    List<WalletEntity> getWalletByQrId(String qrId);
    WalletEntity getWalletById(Long id);
    boolean loginQr(WalletEntity wallet);
    boolean registerWallet(WalletEntity wallet);
    void updateWallet(WalletEntity wallet);
    List<WalletEntity> getWalletsByWalletAddrs(List<String> walletAddrs);
    List<WalletEntity> getWalletsByQrId(String qrId);
//    void deleteWallet(Long id);
}
