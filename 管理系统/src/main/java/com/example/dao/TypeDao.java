package com.example.dao;

import com.example.entity.AdminInfo;
import com.example.entity.Type;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TypeDao extends Mapper<Type> {

    @Select("select * from type where name like concat('%' , #{name} , '%')")
    List<Type> selectByName(@Param("name") String name);

    @Select("select * from type where name = #{name}")
    List<Type> selectName(@Param("name") String name);
}
