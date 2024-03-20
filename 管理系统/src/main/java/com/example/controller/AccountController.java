package com.example.controller;


import cn.hutool.core.util.ObjectUtil;
import com.example.common.CaptureConfig;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.exception.CustomException;
import com.example.service.AdminInfoService;
import com.example.service.StudentInfoService;
import com.example.service.TeacherInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping
public class AccountController {

    @Resource
    private AdminInfoService adminInfoService;

    @Resource
    private TeacherInfoService teacherInfoService;

    @Resource
    private StudentInfoService studentInfoService;


    @PostMapping("/login")
    public Result login(@RequestBody Account user , HttpServletRequest request , @RequestParam String key){
        if (!user.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
            CaptchaUtil.clear(request);
            CaptureConfig.CAPTURE_MAP.remove(key);
            return Result.error("-1" , "验证码不正确");
        }

        if (ObjectUtil.isEmpty(user.getName()) || ObjectUtil.isEmpty(user.getPassword()) || ObjectUtil.isEmpty(user.getLevel())) {
            return Result.error("-1" , "请将要求输入的信息输入完毕");
        }
        String level = user.getLevel();
        Account loginUser = new Account();
        if (level.equals("admin")) {
            loginUser = adminInfoService.login(user.getName() , user.getPassword());
        } else if (level.equals("teacher")) {
            loginUser = teacherInfoService.login(user.getName() , user.getPassword());
        } else {
            loginUser = studentInfoService.login(user.getName() , user.getPassword());
        }
        CaptureConfig.CAPTURE_MAP.remove(key);
        return Result.success(loginUser);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account user , HttpServletRequest request){
        if (ObjectUtil.isEmpty(user.getName()) || ObjectUtil.isEmpty(user.getPassword()) || ObjectUtil.isEmpty(user.getLevel()) || ObjectUtil.isEmpty(user.getAge()) || ObjectUtil.isEmpty(user.getSex())) {
            return Result.error("-1" , "注册失败，请将要求输入的信息输入完毕");
        }
        String level = user.getLevel();
        if (level.equals("teacher")) {
            TeacherInfo teacherInfo = new TeacherInfo();
            BeanUtils.copyProperties(user , teacherInfo);
            return teacherInfoService.register(teacherInfo);
        }
        if (level.equals("student")) {
            StudentInfo studentInfo = new StudentInfo();
            BeanUtils.copyProperties(user , studentInfo);
            return studentInfoService.register(studentInfo);
        }
        return Result.error("-1" , "你所创建的角色不合法");
    }



    @RequestMapping("/captcha")
    public void captcha(@RequestParam String key ,  HttpServletRequest request , HttpServletResponse response) throws Exception{
        SpecCaptcha captcha = new SpecCaptcha(135 , 33 , 5);
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        CaptureConfig.CAPTURE_MAP.put(key , captcha.text().toLowerCase());
        CaptchaUtil.out(captcha , request , response);
    }

    @PutMapping("/selftCenter")
    public Result selftCenter(@RequestBody Account account){
        Account account1 = new Account();
        if (account.getLevel().equals("admin")) {
            account1 = adminInfoService.findById(account.getId());
        } else if (account.getLevel().equals("student")) {
            account1 = studentInfoService.findById(account.getId());
        } else if (account.getLevel().equals("teacher")) {
            account1 = teacherInfoService.findById(account.getId());
        }
        return Result.success(account1);
    }

    @PostMapping("/updateSelf")
    public Result updateSelf(@RequestBody Account account){
        if (account.getLevel().equals("admin")) {
            AdminInfo adminInfo = new AdminInfo();
            BeanUtils.copyProperties(account , adminInfo);
            adminInfoService.updateSelf(adminInfo);
        } else if (account.getLevel().equals("student")) {
            StudentInfo studentInfo = new StudentInfo();
            BeanUtils.copyProperties(account , studentInfo);
            studentInfoService.updateSelf(studentInfo);
        } else if (account.getLevel().equals("teacher")) {
            TeacherInfo teacherInfo = new TeacherInfo();
            BeanUtils.copyProperties(account , teacherInfo);
            teacherInfoService.updateSelf(teacherInfo);
        }
        return Result.success();
    }

    @PostMapping("/schoolTime")
    public Result schoolTime(@RequestBody StudentInfo studentInfo){
        Account studentInfo1 = studentInfoService.login(studentInfo.getName() , studentInfo.getPassword());
        return Result.success(studentInfo1);
    }

}
