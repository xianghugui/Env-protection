package com.environment.program.dao;

import com.environment.program.bean.Parameter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ParameterMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "temperature", column = "temperature"),
            @Result(property = "humidity", column = "humidity"),
            @Result(property = "hcho", column = "HCHO"),
            @Result(property = "tvoc", column = "TVOC"),
            @Result(property = "coTwo", column = "CO_two"),
            @Result(property = "pMTwoPointFive", column = "PM_two_point_five"),
            @Result(property = "pMOnePointZero", column = "PM_one_point_zero"),
            @Result(property = "pMTen", column = "PM_ten"),
            @Result(property = "illumination", column = "illumination"),
            @Result(property = "windSpeed", column = "windSpeed"),
            @Result(property = "deviceId", column = "deviceId"),
            @Result(property = "createTime", column = "createTime"),
            @Result(property = "windDirection", column = "windDirection")
    })

    @Insert("INSERT INTO parameter" +
            "(temperature,humidity,HCHO,TVOC,CO_two,PM_two_point_five, PM_one_point_zero, PM_ten, illumination,windSpeed,windDirection,deviceId,createTime)" +
            " values" +
            " (#{temperature},#{humidity},#{hcho},#{tvoc},#{coTwo},#{pMTwoPointFive}, #{pMOnePointZero}, #{pMTen}, #{illumination},#{windSpeed},#{windDirection},#{deviceId},#{createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    Integer insert(Parameter parameter);

    @Update({ "UPDATE parameter SET " +
            "temperature = #{temperature},humidity = #{humidity},HCHO = #{hcho},TVOC = #{tvoc} ," +
            "CO_two = #{coTwo},PM_two_point_five = #{pMTwoPointFive},PM_one_point_zero = #{pMOnePointZero}, PM_ten = #{pMTen}, " +
            "illumination = #{illumination},windSpeed = #{windSpeed},windDirection = #{windDirection},deviceId = #{deviceId} where id = #{id}" })
    Integer update(Parameter parameter);

    @Select("SELECT * FROM parameter ORDER BY createTime DESC LIMIT 1")
    Parameter selectOne ();
}
