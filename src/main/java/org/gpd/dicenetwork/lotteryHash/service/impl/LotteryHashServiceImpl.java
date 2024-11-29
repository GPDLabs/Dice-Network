package org.gpd.dicenetwork.lotteryHash.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gpd.dicenetwork.lotteryHash.entity.LotteryHashEntity;
import org.gpd.dicenetwork.lotteryHash.mapper.LotteryHashMapper;
import org.gpd.dicenetwork.lotteryHash.service.LotteryHashService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryHashServiceImpl extends ServiceImpl<LotteryHashMapper, LotteryHashEntity> implements LotteryHashService {

    // 插入数据
    @Override
    public boolean insertLotteryHash(LotteryHashEntity entity) {
        return getBaseMapper().insert(entity) > 0;
    }

    // 更新数据
    @Override
    public boolean updateLotteryHashById(LotteryHashEntity entity) {
        return getBaseMapper().updateById(entity) > 0;
    }

    // 删除数据
//    @Override
//    public boolean deleteLotteryHashById(Integer id) {
//        return getBaseMapper().deleteById(id) > 0;
//    }

    // 查询单条数据
    @Override
    public LotteryHashEntity selectLotteryHashById(Integer id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    public LotteryHashEntity selectByLotteryHash(String lotteryHash) {
        return baseMapper.selectByLotteryHash(lotteryHash);
    }

    // 查询所有数据
    @Override
    public List<LotteryHashEntity> selectAllLotteryHash() {
        return getBaseMapper().selectList();
    }
}
