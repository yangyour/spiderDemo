package com.example.sptiledemo.mapper;

import com.example.sptiledemo.bean.Contract;
import com.example.sptiledemo.bean.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StockMapper{
    /**
     * 批量插入数据
     * @param stock
     * @return
     */
    int save(@Param("stock") Stock stock);
    /**
     * 分页查找全部信息
     */
    List<Stock> selectByConCode(@Param("conCode")String conCode,@Param("conNames")String conNames);

    List<Stock> selectCode(@Param("conCode")String conCode,@Param("conNames")String conNames);
//    /**
//     * 查找表的记录数
//     */
//    int selectCount();
}
