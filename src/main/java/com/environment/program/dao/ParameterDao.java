package com.environment.program.dao;

import com.environment.program.bean.Parameter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ParameterDao {

    @Insert("INSERT INTO parameter" +
            "(temperature,humidity,HCHO,TVOC,CO_two,PM_two_point_five, PM_one_point_zero,illumination,windSpeed,windDirection)" +
            " values" +
            " (#{temperature},#{humidity},#{hcho},#{tvoc},#{coTwo},#{pMTwoPointFive}, #{pMOnePointZero},#{illumination},#{windSpeed},#{windDirection})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    Integer insert(Parameter parameter);

    @Update({ "UPDATE parameter SET " +
            "temperature = #{temperature},humidity = #{humidity},HCHO = #{hcho},TVOC = #{tvoc} ," +
            "CO_two = #{coTwo},PM_two_point_five = #{pMTwoPointFive},PM_one_point_zero = #{pMOnePointZero}, " +
            "illumination = #{illumination},windSpeed = #{windSpeed},windDirection = #{windDirection} where id = #{id}" })
    Integer update(Parameter parameter);
}
