package com.environment.program.dao;

import com.environment.program.bean.Parameter;
import org.apache.ibatis.annotations.*;

import java.util.List;

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


    @Results({
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "username", column = "USERNAME"),
            @Result(property = "password", column = "PASSWORD"),
            @Result(property = "mobileNum", column = "PHONE_NUM")
    })
    @Select("SELECT "
            + "a.id as 'id',a.create_date as 'createDate',a.content as 'content',"
            + "a.parent_id as 'parentId',a.first_comment_id as 'firstCommentId',"
            + "b.id as 'fromUser.id',b.realname as 'fromUser.realname',b.avatar as 'fromUser.avatar',"
            + "c.id as 'toUser.id',c.realname as 'toUser.realname',c.avatar as 'toUser.avatar' "
            + "FROM t_demand_comment a "
            + "LEFT JOIN t_user b ON b.id = a.from_uid "
            + "LEFT JOIN t_user c ON c.id = a.to_uid "
            + "WHERE a.demand_id = #{demandId} "
            + "ORDER BY a.create_date ASC"
            + "LIMIT #{startNo},#{pageSize}")
    public List<Parameter> select (@Param("demandId") Long demandId, @Param("startNo") Integer pageNo, @Param("pageSize") Integer pageSize);

}
