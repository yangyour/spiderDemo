package com.example.sptiledemo.mapper;


import com.example.sptiledemo.bean.RunningWater;
import com.example.sptiledemo.bean.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RunningWaterMapper {
    /**
     * 批量插入数据
     * @param runningWater
     * @return
     */
    int save(@Param("runningWater") RunningWater runningWater);
    /**
     * 分页查找全部信息
     */
    List<RunningWater> selectByConCode(@Param("code")String code);
//    /**
//     * 查找表的记录数
//     */
//    int selectCount();
}
