package com.example.sptiledemo.mapper;

import com.example.sptiledemo.bean.Stock;
import com.example.sptiledemo.bean.TimeList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TimeListMapper {

    int save(@Param("timeList") TimeList timeList);

    List<TimeList> selectByConCode(@Param("code")String code, @Param("name")String name);

    List<TimeList> selectCode(@Param("code")String code, @Param("name")String name);

}
