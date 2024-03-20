package com.example.dao;

import com.example.entity.ClassRoom;
import com.example.entity.StudentInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ClassRoomDao extends Mapper<ClassRoom> {

    @Select("select * from classroom where name like concat('%' , #{name} , '%')")
    List<ClassRoom> selectByName(@Param("name") String name);

    @Select("select * from classroom where name = #{name}")
    List<ClassRoom> selectName(@Param("name") String name);

    @Select("select * from classroom")
    List<ClassRoom> selectAll2();

    @Select("select classroom.* , student_info.name as 'reName' from classroom left join student_info on classroom.studentId = student_info.id where classroom.studentId is not null")
    List<ClassRoom> selectAllSt();

    @Select("select classroom.* , teacher_info.name as 'reName' from classroom left join teacher_info on classroom.teacherId = teacher_info.id where classroom.teacherId is not null")
    List<ClassRoom> selectAllTe();

    @Select("select * from classroom where classroomRe = #{id}")
    ClassRoom selectByClassRe(@Param("id") String id);
}
