package org.gpd.dicenetwork.lotteryHash.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gpd.dicenetwork.lotteryHash.entity.LotteryHashEntity;

import java.util.List;

public interface LotteryHashService extends IService<LotteryHashEntity> {
    // 插入数据
    boolean insertLotteryHash(LotteryHashEntity entity);

    // 更新数据
    boolean updateLotteryHashById(LotteryHashEntity entity);

    // 删除数据
//    boolean deleteLotteryHashById(Integer id);

    // 查询单条数据
    LotteryHashEntity selectLotteryHashById(Integer id);

    LotteryHashEntity selectByLotteryHash(String lotteryHash);
    // 查询所有数据
    List<LotteryHashEntity> selectAllLotteryHash();
}