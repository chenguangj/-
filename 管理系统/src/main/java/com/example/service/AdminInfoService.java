package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.JwtTokenUtils;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.dao.AdminInfoDao;
import com.example.entity.Account;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminInfoService {
    @Resource
    private AdminInfoDao adminInfoDao;


    public PageInfo<AdminInfo> selectAll(Integer pageNum , Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<AdminInfo> list = adminInfoDao.selectAll();
        return PageInfo.of(list);
    }


    public Account login(String name, String password) {
        AdminInfo adminInfo = adminInfoDao.findByNameAndPass(name , password);

        if (ObjectUtil.isEmpty(adminInfo)) {
            throw new CustomException("-1" , "用户名，密码或身份错误");
        }

        if (ObjectUtil.isEmpty(adminInfo)) {
            return null;
        }

//        jwt操作
//        如果可以查出来，那说明这个用户是在数据库当中的，是合法的，所以我们赋予他一个token传给前端
        String token = JwtTokenUtils.getToken(adminInfo.getId().toString() , adminInfo.getLevel(), adminInfo.getPassword());
        adminInfo.setToken(token);

        return adminInfo;
    }

    public AdminInfo findById(Long aLong) {
        return adminInfoDao.selectByPrimaryKey(aLong);
    }

    public PageInfo<AdminInfo> selectByName(String name , Integer pageNum , Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<AdminInfo> list = adminInfoDao.selectByName(name);
        return PageInfo.of(list);
    }

    public void add(AdminInfo adminInfo) {
        adminInfo.setLevel("admin");
        adminInfo.setPassword("123");
        adminInfoDao.insertSelective(adminInfo);
    }

    public void update(AdminInfo adminInfo) {
        adminInfoDao.updateByPrimaryKeySelective(adminInfo);
    }

    public void delete(Long id) {
        adminInfoDao.deleteByPrimaryKey(id);
    }

    public Object selectName(AdminInfo adminInfo) {
        List<AdminInfo> adminInfos = adminInfoDao.selectName(adminInfo.getName());
        boolean is = false;
        for (AdminInfo Info : adminInfos) {
            if (Info.equals(adminInfo)) {
                is = true;
            }
        }
        if (adminInfos.size() == 1 && is) {
            return null;
        }
        return adminInfos;
    }

    //    新增时的判断
    public Object selectName2(String name) {
        List<AdminInfo> adminInfos = adminInfoDao.selectName(name);
        return adminInfos.size() == 0 ? null : 1;
    }

    public List<AdminInfo> findAll() {
        return adminInfoDao.selectAll();
    }

    public void updateSelf(AdminInfo account) {
        List<AdminInfo> list = adminInfoDao.selectAll();
        for (AdminInfo adminInfo : list) {
            if (account.getName().equals(adminInfo.getName())) {
                throw new CustomException("-1" , "你修改后的名字在数据库当中已经有这个用户存在");
            }
        }
        adminInfoDao.updateByPrimaryKeySelective(account);
    }
}
