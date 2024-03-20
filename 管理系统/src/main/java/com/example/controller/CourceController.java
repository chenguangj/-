package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.service.CourceService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cource")
public class CourceController {
    @Resource
    private CourceService courceService;

    @GetMapping
    public Result selectAll(@RequestParam String name , @RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo<Cource> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = courceService.selectByName(name , pageNum , pageSize);
        }else {
            info = courceService.selectAll(pageNum , pageSize);
        }
        return Result.success(info);
    }

    @PostMapping
    public Result add(@RequestBody Cource cource){
        if (ObjectUtil.isNotEmpty(courceService.selectName2(cource.getName()))){
            return Result.error("-1" , "当前创建的课程名已存在");
        }
        for (int i = 0; i < cource.getFormType().length; i++) {
            if (i == 0) {
                cource.setType(cource.getFormType()[i]);
                continue;
            }
            cource.setType(cource.getType() + "&" + cource.getFormType()[i]);
        }
        if (ObjectUtil.isEmpty(cource.getClassroom()) || ObjectUtil.isEmpty(cource.getWeek()) || ObjectUtil.isEmpty(cource.getWeekDay()) || ObjectUtil.isEmpty(cource.getFormType())) {
            throw new CustomException("-1" , "你所填写的信息不完整，请将信息填写完整");
        }
        courceService.add(cource);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Cource cource){
        if (ObjectUtil.isNotEmpty(courceService.selectName(cource))){
            return Result.error("-1" , "当前修改的名字已被其他课程占用，请修改成其他名字");
        }
        if (ObjectUtil.isEmpty(cource.getFormType())) {
            throw new CustomException("-1" , "你还没有选择专业，编辑不成功，请选择专业");
        }
        if (ObjectUtil.isEmpty(cource.getClassroom()) || ObjectUtil.isEmpty(cource.getWeek()) || ObjectUtil.isEmpty(cource.getWeekDay()) || ObjectUtil.isEmpty(cource.getFormType())) {
            throw new CustomException("-1" , "你所填写的信息不完整，请将信息填写完整");
        }
        for (int i = 0; i < cource.getFormType().length; i++) {
            if (i == 0) {
                cource.setType(cource.getFormType()[i]);
                continue;
            }
            cource.setType(cource.getType() + "&" + cource.getFormType()[i]);
        }
        courceService.update(cource);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Long id){
        courceService.delete(id);
        return Result.success();
    }

    @PutMapping("/delSelected")
    public Result delSelected(@RequestBody List<Cource> cources){
        for (Cource cource : cources) {
            courceService.delete(cource.getId());
        }
        return Result.success();
    }

    @PutMapping("/xuanjiao")
    public Result xuanjiao(@RequestBody Cource cource , @RequestParam Long teacherId){
        courceService.xuanjiao(cource , teacherId);
        return Result.success();
    }

    @PutMapping("/agree")
    public Result agree(@RequestBody Cource cource){
        courceService.agree(cource);
        return Result.success();
    }

    @PutMapping("/disagree")
    public Result disagree(@RequestBody Cource cource){
        courceService.disagree(cource);
        return Result.success();
    }

    @PutMapping("/force")
    public void force(@RequestBody Cource cource){
        throw new CustomException("-1" , "此功能涉及的数据太多，且没有什么技术含量，所以不实现了");
    }

    @PutMapping("/xuanke")
    public Result xuanke(@RequestBody Cource cource , @RequestParam Long studentId){
        courceService.xuanke(cource , studentId);
        return Result.success();
    }

    @PutMapping("/getReTe")
    public Result getReTe(@RequestParam String name , @RequestParam Integer pageNum , @RequestParam Integer pageSize , @RequestBody TeacherInfo teacherInfo){
        PageInfo<Cource> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = courceService.selectByNameTe(name , pageNum , pageSize , teacherInfo);
        }else {
            info = courceService.selectAllTe(pageNum , pageSize , teacherInfo);
        }
        return Result.success(info);
    }

    @PutMapping("/tuidingTe")
    public Result tuidingTe(@RequestBody TeacherInfo teacherInfo , @RequestParam Long id){
        TeacherInfo teacherInfo1 = courceService.tuidingTe(teacherInfo , id);
        return Result.success(teacherInfo1);
    }


    @PutMapping("/schoolTime")
    public Result schoolTime(@RequestBody StudentInfo studentInfo){
        List<Cource> list = courceService.schoolTime(studentInfo);
        return Result.success(list);
    }

    @PutMapping("/tuixuan")
    public Result tuixuan(@RequestParam Long studentId , @RequestBody Cource cource){
        courceService.tuixuan(studentId , cource);
        return Result.success();
    }

    @PutMapping("/upload")
    public Result upload(@RequestBody Cource cource){
        courceService.upload(cource);
        return Result.success();
    }


    @PutMapping("/getReSt")
    public Result getReSt(@RequestParam String name , @RequestParam Integer pageNum , @RequestParam Integer pageSize , @RequestBody StudentInfo studentInfo){
        PageInfo<Cource> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = courceService.selectByNameSt(name , pageNum , pageSize , studentInfo);
        }else {
            info = courceService.selectAllSt(pageNum , pageSize , studentInfo);
        }
        return Result.success(info);
    }

    @PutMapping("/uploadSt")
    public Result uploadSt(@RequestBody StudentInfo studentInfo , @RequestParam String img , @RequestParam Long courceId){
        courceService.uploadSt(studentInfo , img , courceId);
        return Result.success();
    }

    @PutMapping("/getHomeSt")
    public Result getHomeSt(@RequestBody Cource cource){
        List<StudentInfo> studentInfos = courceService.getHomeSt(cource);
        return Result.success(studentInfos);
    }

    @PutMapping("/getScore")
    public Result getScore(@RequestBody StudentInfo studentInfo , String score){
        String img = studentInfo.getImg();
        courceService.getScore(studentInfo , score , img);
        return Result.success();
    }

}
