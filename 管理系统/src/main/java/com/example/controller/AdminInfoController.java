package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.AdminInfo;
import com.example.service.AdminInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/adminInfo")
public class AdminInfoController {
    @Resource
    private AdminInfoService adminInfoService;

    @GetMapping
    public Result selectAll(@RequestParam String name , @RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo<AdminInfo> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = adminInfoService.selectByName(name , pageNum , pageSize);
        }else {
            info = adminInfoService.selectAll(pageNum , pageSize);
        }
        return Result.success(info);
    }

    @PostMapping
    public Result add(@RequestBody AdminInfo adminInfo){
        if (ObjectUtil.isNotEmpty(adminInfoService.selectName2(adminInfo.getName()))){
            return Result.error("-1" , "当前创建的用户名已存在");
        }
        adminInfoService.add(adminInfo);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody AdminInfo adminInfo){
        if (ObjectUtil.isNotEmpty(adminInfoService.selectName(adminInfo))){
            return Result.error("-1" , "当前修改的名字已被其他用户占用，请修改成其他名字");
        }
        adminInfoService.update(adminInfo);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Long id){
        adminInfoService.delete(id);
        return Result.success();
    }

    @PutMapping("/delSelected")
    public Result delSelected(@RequestBody List<AdminInfo> adminInfos){
        for (AdminInfo adminInfo : adminInfos) {
            adminInfoService.delete(adminInfo.getId());
        }
        return Result.success();
    }

    @GetMapping("/export")
    public Result export(HttpServletResponse response)throws IOException {
        List<AdminInfo> all = adminInfoService.findAll();
        if (CollectionUtil.isEmpty(all)) {
            return Result.error("-1" , "都没有值，你还让我导出什么？");
        }
        List<Map<String , Object>> list = new ArrayList<>(all.size());
        for (AdminInfo adminInfo : all) {
            Map<String , Object> row = new HashMap<>();
            row.put("名字" , adminInfo.getName());
            row.put("年龄" , adminInfo.getAge());
            row.put("性别" , adminInfo.getSex());
            row.put("权限" , adminInfo.getLevel());
            list.add(row);
        }
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list , true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition" , "attachment;filename=admin.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out , true);
        wr.close();
        IoUtil.close(System.out);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file)throws IOException{
        List<AdminInfo> list = ExcelUtil.getReader(file.getInputStream()).readAll(AdminInfo.class);
        if (!CollectionUtil.isEmpty(list)) {
            for (AdminInfo adminInfo : list) {
                try {
                    adminInfoService.add(adminInfo);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }

}
