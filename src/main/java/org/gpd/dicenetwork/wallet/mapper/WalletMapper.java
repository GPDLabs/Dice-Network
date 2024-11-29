package org.gpd.dicenetwork.wallet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.gpd.dicenetwork.wallet.entity.WalletEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WalletMapper extends BaseMapper<WalletEntity> {

    List<WalletEntity> selectAll();

    WalletEntity selectById(Long id);

    int insert(WalletEntity walletEntity);

    List<WalletEntity> selectByWalletAddr(String walletAddr);

//    void deleteById(Long id);

    List<WalletEntity> selectByQrIdAndIsMaster(@Param("qrId") String qrId, @Param("isMaster") int isMaster);

    List<WalletEntity> selectByWalletAddrs(@Param("walletAddrs") List<String> walletAddrs);

    int updateById(WalletEntity walletEntity);

    List<WalletEntity> selectByQrId(@Param("qrId") String qrId);
}