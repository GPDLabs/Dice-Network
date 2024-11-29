package org.gpd.dicenetwork.lotteryHash.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.gpd.dicenetwork.lotteryHash.entity.LotteryHashEntity;

import java.util.List;

public interface LotteryHashMapper extends BaseMapper<LotteryHashEntity> {
    // 插入数据
    int insert(@Param("entity") LotteryHashEntity entity);

    // 更新数据
    int updateById(@Param("entity") LotteryHashEntity entity);

    // 删除数据
//    int deleteLotteryHashById(@Param("id") Integer id);

    // 查询单条数据
    LotteryHashEntity selectById(@Param("id") Integer id);

    @Select("SELECT * FROM lottery_hash_list WHERE lottery_hash = #{lotteryHash} AND status = 0 ORDER BY created_at DESC LIMIT 1")
    LotteryHashEntity selectByLotteryHash(@Param("lotteryHash") String lotteryHash);
    // 查询所有数据
    List<LotteryHashEntity> selectList();
}