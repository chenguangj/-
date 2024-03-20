package com.example.dao;

import com.example.entity.AdminInfo;
import com.example.entity.Cource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CourceDao extends Mapper<Cource> {

    @Select("select * from cource where name like concat('%' , #{name} , '%')")
    List<Cource> selectByName(@Param("name") String name);

    @Select("select * from cource where name = #{name}")
    List<Cource> selectName(@Param("name") String name);

}
