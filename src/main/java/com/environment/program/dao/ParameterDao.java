package com.environment.program.dao;

import com.environment.program.bean.Parameter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
@Mapper
public interface ParameterDao {

    @Insert("INSERT INTO parameter" +
            "(temperature,humidity,HCHO,TVOC,CO_two,PM_two_point_five, PM_one_point_zero,illumination,windSpeed,windDirection)" +
            " values" +
            " (#{temperature},#{humidity},#{hcho},#{tvoc},#{coTwo},#{pMTwoPointFive}, #{pMOnePointZero},#{illumination},#{windSpeed},#{windDirection})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    Integer insert(Parameter parameter);
}
