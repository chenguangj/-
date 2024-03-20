package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.AdminInfo;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.service.AdminInfoService;
import com.example.service.TeacherInfoService;
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

//这里是专门给教师使用的，所以一些公用的操作，不要在这里写

@RestController
@RequestMapping("/teacherInfo")
public class TeacherInfoController {
    @Resource
    private TeacherInfoService teacherInfoService;


    @GetMapping
    public Result selectAll(@RequestParam String name , @RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo<TeacherInfo> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = teacherInfoService.selectByName(name , pageNum , pageSize);
        }else {
            info = teacherInfoService.selectAll(pageNum , pageSize);
        }
        return Result.success(info);
    }

    @PostMapping
    public Result add(@RequestBody TeacherInfo teacherInfo){
        if (ObjectUtil.isNotEmpty(teacherInfoService.selectName2(teacherInfo.getName()))){
            return Result.error("-1" , "当前创建的用户名已存在");
        }
        teacherInfoService.add(teacherInfo);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody TeacherInfo teacherInfo){
        if (ObjectUtil.isNotEmpty(teacherInfoService.selectName(teacherInfo))){
            return Result.error("-1" , "当前修改的名字已被其他用户占用，请修改成其他名字");
        }
        teacherInfoService.update(teacherInfo);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Long id){
        teacherInfoService.delete(id);
        return Result.success();
    }

    @PutMapping("/delSelected")
    public Result delSelected(@RequestBody List<TeacherInfo> teacherInfos){
        for (TeacherInfo teacherInfo : teacherInfos) {
            teacherInfoService.delete(teacherInfo.getId());
        }
        return Result.success();
    }

    @GetMapping("/export")
    public Result export(HttpServletResponse response)throws IOException {
        List<TeacherInfo> all = teacherInfoService.findAll();
        if (CollectionUtil.isEmpty(all)) {
            return Result.error("-1" , "都没有值，你还让我导出什么？");
        }
        List<Map<String , Object>> list = new ArrayList<>(all.size());
        for (TeacherInfo teacherInfo : all) {
            Map<String , Object> row = new HashMap<>();
            row.put("名字" , teacherInfo.getName());
            row.put("职工号" , teacherInfo.getTeacherId());
            row.put("年龄" , teacherInfo.getAge());
            row.put("性别" , teacherInfo.getSex());
            row.put("权限" , teacherInfo.getLevel());
            list.add(row);
        }
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list , true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition" , "attachment;filename=teacher.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out , true);
        wr.close();
        IoUtil.close(System.out);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file)throws IOException{
        List<TeacherInfo> list = ExcelUtil.getReader(file.getInputStream()).readAll(TeacherInfo.class);
        if (!CollectionUtil.isEmpty(list)) {
            for (TeacherInfo teacherInfo : list) {
                try {
                    teacherInfoService.add(teacherInfo);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }

    @PutMapping("/agree")
    public Result agree(@RequestBody TeacherInfo teacherInfo){
        teacherInfoService.agree(teacherInfo);
        return Result.success();
    }

    @PutMapping("/disagree")
    public Result disagree(@RequestBody TeacherInfo teacherInfo){
        teacherInfoService.disagree(teacherInfo);
        return Result.success();
    }

    @PutMapping("/getname")
    public Result getName(@RequestParam String id){
        String name = "";
        try {
            name = teacherInfoService.getName(Long.valueOf(id));
        }catch (Exception e){

        }
        return Result.success(name);
    }

}
