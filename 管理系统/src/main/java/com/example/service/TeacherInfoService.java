package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.JwtTokenUtils;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dao.AdminInfoDao;
import com.example.dao.LogDao;
import com.example.dao.TeacherInfoDao;
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
public class TeacherInfoService {
    @Resource
    private TeacherInfoDao teacherInfoDao;

    @Resource
    private LogDao logDao;



    public Account login(String name, String password) {
        TeacherInfo teacherInfo = teacherInfoDao.findByNameAndPass(name, password);
        if (ObjectUtil.isEmpty(teacherInfo)) {
            throw new CustomException("-1" , "用户名，密码或身份错误");
        }
        if (teacherInfo.getIsTrue() == 0) {
            throw new CustomException("-1" , "当前用户还没有被管理员同意，不能登录");
        }
        String token = JwtTokenUtils.getToken(teacherInfo.getId().toString(), teacherInfo.getLevel(), teacherInfo.getPassword());
        teacherInfo.setToken(token);

        Log log = logDao.selectById(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你登录了系统");
        logDao.updateByPrimaryKeySelective(log);

        return teacherInfo;
    }

    public Result register(TeacherInfo teacherInfo) {
        TeacherInfo Info = teacherInfoDao.findByName(teacherInfo.getName());
        if (ObjectUtil.isNotEmpty(Info)) {
            return Result.error("-1", "当前用户已存在，不能注册");
        }
        teacherInfoDao.insertSelective(teacherInfo);
        return Result.success();
    }

    public PageInfo<TeacherInfo> selectAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TeacherInfo> list = teacherInfoDao.selectAll();
        return PageInfo.of(list);
    }

    public TeacherInfo findById(Long aLong) {
        return teacherInfoDao.selectByPrimaryKey(aLong);
    }

    public PageInfo<TeacherInfo> selectByName(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TeacherInfo> list = teacherInfoDao.selectByName(name);
        return PageInfo.of(list);
    }

    public void add(TeacherInfo teacherInfo) {
        teacherInfo.setLevel("teacher");
        teacherInfo.setPassword("123");
        teacherInfo.setIsTrue(1l);
        teacherInfoDao.insertSelective(teacherInfo);
        Log log = new Log();
        log.setAccountId(teacherInfo.getId() + "&teacher");
        logDao.insertSelective(log);
    }

    public void update(TeacherInfo teacherInfo) {
        TeacherInfo teacherInfo1 = teacherInfoDao.selectByPrimaryKey(teacherInfo.getId());
        if (!teacherInfo1.getType().equals(teacherInfo.getType())) {
            String courceId = teacherInfo1.getCourceId();
            String[] split = courceId.split("&");
            for (String s : split) {
                if (ObjectUtil.isEmpty(s) || "null".equals(s) || "-1".equals(s) || "".equals(s)) {
                    continue;
                }
                throw new CustomException("-1" , "当前老师有预选的课程在，还不能修改他的专业");
            }
        }
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
    }

    public void delete(Long id) {
        TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(id);
        if (ObjectUtil.isNotEmpty(teacherInfo.getClassroomRe())) {
            throw new CustomException("-1" , "当前老师还有借用的教室没有撤回，不能删除");
        }
        if (ObjectUtil.isNotEmpty(teacherInfo.getCourceId())) {
            throw new CustomException("-1" , "当前老师还有预选的课程在，不能删除");
        }
        Log log = logDao.selectById(id + "&teacher");
        logDao.deleteByPrimaryKey(log.getId());
        teacherInfoDao.deleteByPrimaryKey(id);
    }

    //    编辑时的判断
    public Object selectName(TeacherInfo teacherInfo) {
        List<TeacherInfo> teacherInfos = teacherInfoDao.selectName(teacherInfo.getName());
        boolean is = false;
        for (TeacherInfo info : teacherInfos) {
            if (info.equals(teacherInfo)) {
                is = true;
            }
        }
        if (teacherInfos.size() == 1 && is) {
            return null;
        }
        return teacherInfos;
    }

    //    新增时的判断
    public Object selectName2(String name) {
        List<TeacherInfo> teacherInfos = teacherInfoDao.selectName(name);
        return teacherInfos.size() == 0 ? null : 1;
    }

    public List<TeacherInfo> findAll() {
        return teacherInfoDao.selectAll();
    }

    public void agree(TeacherInfo teacherInfo) {
        List<TeacherInfo> list = teacherInfoDao.selectAll();
        for (TeacherInfo info : list) {
            if (ObjectUtil.isNotEmpty(info.getTeacherId()) && info.getTeacherId().equals(teacherInfo.getTeacherId())) {
                throw new CustomException("-1" , "当前职工号已被使用，不能使用");
            }
        }
        teacherInfo.setIsTrue(1l);
        Log log = new Log();
        log.setAccountId(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员同意当前用户合法，可以实现登录");
        logDao.insertSelective(log);
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
    }

    public void disagree(TeacherInfo teacherInfo) {
        teacherInfoDao.deleteByPrimaryKey(teacherInfo.getId());
    }


    public void updateSelf(TeacherInfo account) {
        List<TeacherInfo> list = teacherInfoDao.selectAll();
        for (TeacherInfo teacherInfo : list) {
            if (account.getName().equals(teacherInfo.getName())) {
                throw new CustomException("-1" , "你修改后的名字在数据库当中已经有这个用户存在");
            }
        }
        teacherInfoDao.updateByPrimaryKeySelective(account);
    }

    public String getName(Long id) {
        return teacherInfoDao.selectByPrimaryKey(id).getName();
    }
}
