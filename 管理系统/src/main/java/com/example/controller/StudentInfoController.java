package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.service.StudentInfoService;
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
@RequestMapping("/studentInfo")
public class StudentInfoController {
    @Resource
    private StudentInfoService studentInfoService;

    @GetMapping
    public Result selectAll(@RequestParam String name , @RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo<StudentInfo> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = studentInfoService.selectByName(name , pageNum , pageSize);
        }else {
            info = studentInfoService.selectAll(pageNum , pageSize);
        }
        return Result.success(info);
    }

    @PostMapping
    public Result add(@RequestBody StudentInfo studentInfo){
        if (ObjectUtil.isNotEmpty(studentInfoService.selectName2(studentInfo.getName()))){
            return Result.error("-1" , "当前创建的用户名已存在");
        }
        studentInfoService.add(studentInfo);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody StudentInfo studentInfo){
        if (ObjectUtil.isNotEmpty(studentInfoService.selectName(studentInfo))){
            return Result.error("-1" , "当前修改的名字已被其他用户占用，请修改成其他名字");
        }
        studentInfoService.update(studentInfo);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Long id){
        studentInfoService.delete(id);
        return Result.success();
    }

    @PutMapping("/delSelected")
    public Result delSelected(@RequestBody List<StudentInfo> studentInfos){
        for (StudentInfo studentInfo : studentInfos) {
            studentInfoService.delete(studentInfo.getId());
        }
        return Result.success();
    }

    @GetMapping("/export")
    public Result export(HttpServletResponse response)throws IOException {
        List<StudentInfo> all = studentInfoService.findAll();
        if (CollectionUtil.isEmpty(all)) {
            return Result.error("-1" , "都没有值，你还让我导出什么？");
        }
        List<Map<String , Object>> list = new ArrayList<>(all.size());
        for (StudentInfo studentInfo : all) {
            Map<String , Object> row = new HashMap<>();
            row.put("名字" , studentInfo.getName());
            row.put("学号" , studentInfo.getStudentId());
            row.put("年龄" , studentInfo.getAge());
            row.put("性别" , studentInfo.getSex());
            row.put("权限" , studentInfo.getLevel());
            list.add(row);
        }
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list , true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition" , "attachment;filename=student.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out , true);
        wr.close();
        IoUtil.close(System.out);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file)throws IOException{
        List<StudentInfo> list = ExcelUtil.getReader(file.getInputStream()).readAll(StudentInfo.class);
        if (!CollectionUtil.isEmpty(list)) {
            for (StudentInfo studentInfo : list) {
                try {
                    studentInfoService.add(studentInfo);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }

    @PutMapping("/agree")
    public Result agree(@RequestBody StudentInfo studentInfo){
        studentInfoService.agree(studentInfo);
        return Result.success();
    }

    @PutMapping("/disagree")
    public Result disagree(@RequestBody StudentInfo studentInfo){
        studentInfoService.disagree(studentInfo);
        return Result.success();
    }


}
