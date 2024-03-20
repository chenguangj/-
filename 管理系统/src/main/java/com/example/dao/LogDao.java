package com.example.dao;

import com.example.entity.Log;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface LogDao extends Mapper<Log> {

    @Select("select * from log where accountId = #{id}")
    Log selectById(@Param("id") String id);

}
