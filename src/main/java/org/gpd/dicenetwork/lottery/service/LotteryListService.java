package org.gpd.dicenetwork.lottery.service;

import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface LotteryListService {
    List<LotteryListEntity> getAllLotteries();
    LotteryListEntity getLotteryById(Integer id);
    void addLottery(LotteryListEntity lottery);
    void updateLottery(LotteryListEntity lottery);
//    void deleteLottery(Integer id);
    void addLotteries(List<LotteryListEntity> lotteries);
    List<LotteryListEntity> getLotteriesByLotteryTime(LocalDateTime lotteryTime);
}
