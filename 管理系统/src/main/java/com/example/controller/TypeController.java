package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.AdminInfo;
import com.example.entity.Type;
import com.example.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    @GetMapping
    public Result getType(){
        List<Type> list = typeService.findAll();
        return Result.success(list);
    }

    @GetMapping("/all")
    public Result selectAll(@RequestParam String name , @RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo<Type> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = typeService.selectByName(name , pageNum , pageSize);
        }else {
            info = typeService.selectAll(pageNum , pageSize);
        }
        return Result.success(info);
    }

    @PostMapping
    public Result add(@RequestBody Type type){
        if (ObjectUtil.isNotEmpty(typeService.selectName2(type.getName()))){
            return Result.error("-1" , "当前创建的专业名已存在");
        }
        typeService.add(type);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Type type){
        if (ObjectUtil.isNotEmpty(typeService.selectName(type))){
            return Result.error("-1" , "当前修改的名字已被其他专业占用，请修改成其他名字");
        }
        typeService.update(type);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Long id){
        typeService.delete(id);
        return Result.success();
    }

}
