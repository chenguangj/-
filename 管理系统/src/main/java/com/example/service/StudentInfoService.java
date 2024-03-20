package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.JwtTokenUtils;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dao.LogDao;
import com.example.dao.StudentInfoDao;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StudentInfoService {
    @Resource
    private StudentInfoDao studentInfoDao;

    @Resource
    private LogDao logDao;


    public Account login(String name, String password) {
        StudentInfo studentInfo = studentInfoDao.findByNameAndPass(name, password);
        if (ObjectUtil.isEmpty(studentInfo)) {
            throw new CustomException("-1" , "用户名，密码或身份错误");
        }
        if (studentInfo.getIsTrue() == 0) {
            throw new CustomException("-1" , "当前用户还没有被管理员同意，不能登录");
        }
        String token = JwtTokenUtils.getToken(studentInfo.getId().toString(), studentInfo.getLevel(), studentInfo.getPassword());
        studentInfo.setToken(token);

        Log log = logDao.selectById(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你登录了系统");
        logDao.updateByPrimaryKeySelective(log);

        return studentInfo;
    }

    public Result register(StudentInfo studentInfo) {
        StudentInfo studentInfo1 = studentInfoDao.findByName(studentInfo.getName());
        if (ObjectUtil.isNotEmpty(studentInfo1)) {
            return Result.error("-1", "当前用户已存在，不能注册");
        }
        studentInfoDao.insertSelective(studentInfo);
        return Result.success();
    }

    public PageInfo<StudentInfo> selectAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentInfo> list = studentInfoDao.selectAll();
        return PageInfo.of(list);
    }

    public StudentInfo findById(Long aLong) {
        return studentInfoDao.selectByPrimaryKey(aLong);
    }

    public PageInfo<StudentInfo> selectByName(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentInfo> list = studentInfoDao.selectByName(name);
        return PageInfo.of(list);
    }

    public void add(StudentInfo studentInfo) {
        studentInfo.setLevel("student");
        studentInfo.setPassword("123");
        studentInfo.setIsTrue(1l);
        studentInfoDao.insertSelective(studentInfo);
        Log log = new Log();
        log.setAccountId(studentInfo.getId() + "&student");
        logDao.insertSelective(log);
    }

    public void update(StudentInfo studentInfo) {
        StudentInfo studentInfo1 = studentInfoDao.selectByPrimaryKey(studentInfo.getId());
        if (!studentInfo1.getType().equals(studentInfo.getType())) {
            String courceId = studentInfo.getCourceId();
            String[] split = courceId.split("&");
            for (String s : split) {
                if (ObjectUtil.isEmpty(s) || "null".equals(s) || "-1".equals(s) || "".equals(s)) {
                    continue;
                }
                throw new CustomException("-1" , "当前学生有预选的课程在，还不能修改他的专业");
            }
        }
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
    }

    public void delete(Long id) {
        StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(id);
        if (ObjectUtil.isNotEmpty(studentInfo.getClassroomRe())) {
            throw new CustomException("-1" , "当前学生还有借用的教室没有撤回，不能删除");
        }
        if (ObjectUtil.isNotEmpty(studentInfo.getCourceId())) {
            throw new CustomException("-1" , "当前学生还有预选的课程在，不能删除");
        }
        Log log = logDao.selectById(id + "&student");
        logDao.deleteByPrimaryKey(log.getId());
        studentInfoDao.deleteByPrimaryKey(id);
    }

    //    编辑时的判断
    public Object selectName(StudentInfo studentInfo) {
        List<StudentInfo> studentInfos = studentInfoDao.selectName(studentInfo.getName());
        boolean is = false;
        for (StudentInfo info : studentInfos) {
            if (info.equals(studentInfo)) {
                is = true;
            }
        }
        if (studentInfos.size() == 1 && is) {
            return null;
        }
        return studentInfos;
    }

    //    新增时的判断
    public Object selectName2(String name) {
        List<StudentInfo> studentInfos = studentInfoDao.selectName(name);
        return studentInfos.size() == 0 ? null : 1;
    }

    public List<StudentInfo> findAll() {
        return studentInfoDao.selectAll();
    }

    public void agree(StudentInfo studentInfo) {
        List<StudentInfo> list = studentInfoDao.selectAll();
        for (StudentInfo info : list) {
            if (ObjectUtil.isNotEmpty(info.getStudentId()) && info.getStudentId().equals(studentInfo.getStudentId())) {
                throw new CustomException("-1" , "当前学号已被使用，不能使用");
            }
        }
        studentInfo.setIsTrue(1l);
        Log log = new Log();
        log.setAccountId(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员同意当前用户合法，可以实现登录");
        logDao.insertSelective(log);
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
    }

    public void disagree(StudentInfo studentInfo) {
        studentInfoDao.deleteByPrimaryKey(studentInfo.getId());
    }

    public void updateSelf(StudentInfo account) {
        List<StudentInfo> list = studentInfoDao.selectAll();
        for (StudentInfo studentInfo : list) {
            if (studentInfo.getId() == account.getId()) {
                continue;
            }
            if (account.getName().equals(studentInfo.getName())) {
                throw new CustomException("-1" , "你修改后的名字在数据库当中已经有这个用户存在");
            }
        }
        studentInfoDao.updateByPrimaryKeySelective(account);
    }
}
