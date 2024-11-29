package org.gpd.dicenetwork.wallet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.gpd.dicenetwork.wallet.entity.WalletEntity;
import org.gpd.dicenetwork.wallet.mapper.WalletMapper;
import org.gpd.dicenetwork.wallet.service.WalletService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WalletServiceImpl extends ServiceImpl<WalletMapper, WalletEntity> implements WalletService {

    @Override
    public List<WalletEntity> getAllWallets() {
        return list();
    }

    @Override
    public WalletEntity getWalletById(Long id) {
        return getById(id);
    }

    public WalletEntity selectFirstByWalletAddr(String walletAddr) {
        if (walletAddr == null || walletAddr.isEmpty()) {
            return null;
        }

        try {
            QueryWrapper<WalletEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("wallet_addr", walletAddr);
            queryWrapper.last("LIMIT 1");

            WalletEntity result = getOne(queryWrapper);

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public WalletEntity selectFirstByQrIdAndIsMaster(String qrId, int isMaster) {
        QueryWrapper<WalletEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qr_id", qrId);
        queryWrapper.eq("is_master", isMaster);
        queryWrapper.last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public WalletEntity getWalletByWalletAddr(String walletAddr) {
        return Optional.ofNullable(getBaseMapper().selectOne(new QueryWrapper<WalletEntity>().eq("wallet_addr", walletAddr))).orElse(null);
    }

    @Override
    public List<WalletEntity> getWalletByQrId(String qrId) {
        QueryWrapper<WalletEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qr_id", qrId);
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public boolean loginQr(WalletEntity wallet) {
        try {
            String qrId = wallet.getQrId();
            WalletEntity existingWallet = selectFirstByQrIdAndIsMaster(qrId, 1);
            System.out.println("existingWallet : " + existingWallet);
            if (existingWallet != null) {
                existingWallet.setOnlineStatus(1);
                return updateById(existingWallet);
            } else {
                wallet.setOnlineStatus(1);
                return save(wallet);
            }
        }catch (Exception e) {
            log.error("Add wallet error", e);
            return false;
        }
    }

    @Override
    public boolean registerWallet(WalletEntity wallet) {
        WalletEntity existingWallet = selectFirstByWalletAddr(wallet.getWalletAddr());
        if (existingWallet != null) {
            return true;
        }
        return save(wallet);
    }

    @Override
    public void updateWallet(WalletEntity wallet) {
        updateById(wallet);
    }

    @Override
    public List<WalletEntity> getWalletsByWalletAddrs(List<String> walletAddrs) {
        return getBaseMapper().selectByWalletAddrs(walletAddrs);
    }

    public List<WalletEntity> getWalletsByQrId(String qrId) {
        return getBaseMapper().selectByQrId(qrId);
    }

//    @Override
//    public void deleteWallet(Long id) {
//        removeById(id);
//    }
}
