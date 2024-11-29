package org.gpd.dicenetwork.lottery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LotteryListMapper extends BaseMapper<LotteryListEntity> {
    void insertBatch(@Param("lotteries") List<LotteryListEntity> lotteries);

    List<LotteryListEntity> selectByLotteryTime(@Param("lotteryTime") LocalDateTime lotteryTime);

    // 插入单个记录
    int insert(LotteryListEntity lotteryListEntity);

    // 更新记录
    int updateById(LotteryListEntity lotteryListEntity);

    // 删除记录
    void deleteById(int id);

    // 查询所有记录
    List<LotteryListEntity> selectList();

    // 根据ID查询记录
    LotteryListEntity selectById(int id);
}
