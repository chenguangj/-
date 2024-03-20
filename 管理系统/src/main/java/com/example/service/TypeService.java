package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.StudentInfoDao;
import com.example.dao.TeacherInfoDao;
import com.example.dao.TypeDao;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.entity.Type;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TypeService {
    @Resource
    private TypeDao typeDao;

    @Resource
    private StudentInfoDao studentInfoDao;

    @Resource
    private TeacherInfoDao teacherInfoDao;

    public List<Type> findAll() {
        return typeDao.selectAll();
    }

    public PageInfo<Type> selectAll(Integer pageNum , Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<Type> list = typeDao.selectAll();
        Map<String , Long> map = new HashMap<>();
        for (Type type : list) {
            map.put(type.getName() , 0l);
        }
        List<StudentInfo> studentInfos = studentInfoDao.selectAll();
        for (StudentInfo studentInfo : studentInfos) {
            if (ObjectUtil.isNotEmpty(studentInfo.getType())) {
                map.put(studentInfo.getType() , map.get(studentInfo.getType()) + 1l);
            }
        }
        for (Type type : list) {
            type.setStudent(map.get(type.getName()));
        }
        map = new HashMap<>();
        for (Type type : list) {
            map.put(type.getName() , 0l);
        }
        List<TeacherInfo> teacherInfos = teacherInfoDao.selectAll();
        for (TeacherInfo teacherInfo : teacherInfos) {
            map.put(teacherInfo.getType() , map.get(teacherInfo.getType()) + 1l);
        }
        for (Type type : list) {
            type.setTeacher(map.get(type.getName()));
        }

        return PageInfo.of(list);
    }

    public PageInfo<Type> selectByName(String name , Integer pageNum , Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<Type> list = typeDao.selectByName(name);
        Map<String , Long> map = new HashMap<>();
        for (Type type : list) {
            map.put(type.getName() , 0l);
        }
        List<StudentInfo> studentInfos = studentInfoDao.selectAll();
        for (StudentInfo studentInfo : studentInfos) {
            map.put(studentInfo.getType() , map.get(studentInfo.getType()) + 1l);
        }
        for (Type type : list) {
            type.setStudent(map.get(type.getName()));
        }
        map = new HashMap<>();
        for (Type type : list) {
            map.put(type.getName() , 0l);
        }
        List<TeacherInfo> teacherInfos = teacherInfoDao.selectAll();
        for (TeacherInfo teacherInfo : teacherInfos) {
            map.put(teacherInfo.getType() , map.get(teacherInfo.getType()) + 1l);
        }
        for (Type type : list) {
            type.setTeacher(map.get(type.getName()));
        }
        return PageInfo.of(list);
    }

    public void add(Type type) {
        typeDao.insertSelective(type);
    }

    public void update(Type type) {
        if (type.getStudent() != 0 || type.getTeacher() != 0) {
            throw new CustomException("-1" , "当前专业有学生和老师在使用，所以不能修改，要去专门一个个改学生的专业才可以");
        }
        typeDao.updateByPrimaryKeySelective(type);
    }

    public void delete(Long id) {
        Type type = typeDao.selectByPrimaryKey(id);
        if (type.getStudent() != 0 || type.getTeacher() != 0) {
            throw new CustomException("-1" , "当前专业有学生和老师在使用，不能删除");
        }
        typeDao.deleteByPrimaryKey(id);
    }

    public Object selectName(Type type) {
        List<Type> types = typeDao.selectName(type.getName());
        for (Type type1 : types) {
            if (type1.getId() == type.getId()) {
                continue;
            }
            if (type1.getName().equals(type.getName())) {
                return types;
            }
        }
        return null;
    }

    //    新增时的判断
    public Object selectName2(String name) {
        List<Type> types = typeDao.selectName(name);
        return types.size() == 0 ? null : 1;
    }
}
