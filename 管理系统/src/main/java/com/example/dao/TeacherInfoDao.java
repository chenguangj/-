package com.example.dao;

import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TeacherInfoDao extends Mapper<TeacherInfo> {


    @Select("select * from teacher_info where name = #{name} and password = #{password}")
    TeacherInfo findByNameAndPass(@Param("name") String name, @Param("password") String password);

    @Select("select * from teacher_info where name = #{name}")
    TeacherInfo findByName(@Param("name") String name);

    @Select("select * from teacher_info where name like concat('%' , #{name} , '%')")
    List<TeacherInfo> selectByName(@Param("name") String name);

    @Select("select * from teacher_info where name = #{name}")
    List<TeacherInfo> selectName(@Param("name") String name);
}
