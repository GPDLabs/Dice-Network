package org.gpd.dicenetwork.lottery.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;
import org.gpd.dicenetwork.lottery.mapper.LotteryListMapper;
import org.gpd.dicenetwork.lottery.service.LotteryListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class LotteryListServiceImpl implements LotteryListService {

    @Autowired
    private LotteryListMapper lotteryListMapper;

    @Override
    public List<LotteryListEntity> getAllLotteries() {
        return lotteryListMapper.selectList();
    }

    @Override
    public LotteryListEntity getLotteryById(Integer id) {
        return lotteryListMapper.selectById(id);
    }

    @Override
    public void addLottery(LotteryListEntity lottery) {
        lotteryListMapper.insert(lottery);
    }

    @Override
    public void updateLottery(LotteryListEntity lottery) {
        lotteryListMapper.updateById(lottery);
    }

//    @Override
//    public void deleteLottery(Integer id) {
//        lotteryListMapper.deleteById(id);
//    }
    @Override
    public void addLotteries(List<LotteryListEntity> lotteries) {
        lotteryListMapper.insertBatch(lotteries);
    }

    @Override
    public List<LotteryListEntity> getLotteriesByLotteryTime(LocalDateTime lotteryTime) {
        try{
            if (lotteryTime == null) {
                log.warn("getLotteriesByLotteryTime called with null lotteryTime");
                return null;
            }
            return lotteryListMapper.selectByLotteryTime(lotteryTime);
        }catch (Exception e){
            return null;
        }
    }
}
