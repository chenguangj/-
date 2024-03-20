package com.example.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.entity.AdminInfo;
import com.example.service.AdminInfoService;
import com.example.service.StudentInfoService;
import com.example.service.TeacherInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenUtils {
    private static AdminInfoService staticAdminInfoService;
    private static StudentInfoService staticStudentInfoService;
    private static TeacherInfoService staticTeacherInfoService;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Resource
    private AdminInfoService adminInfoService;
    @Resource
    private StudentInfoService studentInfoService;

    @Resource
    private TeacherInfoService teacherInfoService;

    @PostConstruct
    public void setUserService(){
        staticAdminInfoService = adminInfoService;
        staticStudentInfoService = studentInfoService;
        staticTeacherInfoService = teacherInfoService;
    }


    public static String getToken(String userId , String level , String password){
        return JWT.create().withAudience(userId + "&" + level)
                .withExpiresAt(DateUtil.offsetHour(new Date() , 2))
                .sign(Algorithm.HMAC256(password));
    }


    public static Account getCurrentUser(){
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                log.error("获取当前登录的token失败，token: {}" , token);
                return null;
            }
            String InfoId = JWT.decode(token).getAudience().get(0);
            String[] split = InfoId.split("&");
            if (split[1].equals("admin")) {
                return staticAdminInfoService.findById(Long.valueOf(split[0]));
            } else if (split[1].equals("teacher")) {
                return staticTeacherInfoService.findById(Long.valueOf(split[0]));
            } else if (split[1].equals("student")){
                return staticStudentInfoService.findById(Long.valueOf(split[0]));
            }
            return null;
        } catch (Exception e) {
            log.error("获取当前登录的用户的信息失败，token: {}" , token , e);
            return null;
        }
    }


}
