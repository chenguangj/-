package com.example.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.exception.CustomException;
import com.example.service.AdminInfoService;
import com.example.service.StudentInfoService;
import com.example.service.TeacherInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminInfoService adminInfoService;

    @Resource
    private StudentInfoService studentInfoService;

    @Resource
    private TeacherInfoService teacherInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        if (StrUtil.isBlank(token)) {
            throw new CustomException("-1" , "请重新登录");
        }
        String infoId;
        AdminInfo adminInfo = null;
        StudentInfo studentInfo = null;
        TeacherInfo teacherInfo = null;
        try{
            infoId = JWT.decode(token).getAudience().get(0);
            String[] split = infoId.split("&");
            if (split[1].equals("admin")) {
                adminInfo = adminInfoService.findById(Long.valueOf(split[0]));
            } else if (split[1].equals("teacher")) {
                teacherInfo = teacherInfoService.findById(Long.valueOf(split[0]));
            } else if (split[1].equals("student")){

                studentInfo = studentInfoService.findById(Long.valueOf(split[0]));
            }
        } catch (Exception e){
            String errMsg = "token验证失败，请重新登录";
            log.error(errMsg + "，token=" + token , e);
            throw new CustomException("-1" , errMsg);
        }
        if (adminInfo == null && teacherInfo == null && studentInfo == null) {
            throw new CustomException("-1" , "用户不存在，请重新登录");
        }
        try {
            JWTVerifier jwtVerifier = null;
            if (ObjectUtil.isNotEmpty(adminInfo)) {
                jwtVerifier = JWT.require(Algorithm.HMAC256(adminInfo.getPassword())).build();
            } else if (ObjectUtil.isNotEmpty(teacherInfo)) {
                jwtVerifier = JWT.require(Algorithm.HMAC256(teacherInfo.getPassword())).build();
            } else if (ObjectUtil.isNotEmpty(studentInfo)) {
                jwtVerifier = JWT.require(Algorithm.HMAC256(studentInfo.getPassword())).build();
            }
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new CustomException("-1" , "token验证失败，请重新登录");
        }
        log.info("token正确允许放行");
        return true;
    }
}
