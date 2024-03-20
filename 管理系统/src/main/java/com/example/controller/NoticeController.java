package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @GetMapping
    public Result getNotice(@RequestParam String name){
        Notice notice = noticeService.getNotice(name);
        return Result.success(notice);
    }

    @PutMapping
    public Result update(@RequestBody Notice notice){
        noticeService.update(notice);
        return Result.success();
    }

    @GetMapping("/getAllNotice")
    public Result getAllNotice(@RequestParam String module){
        Notice notice = noticeService.getAllNotice(module);
        return Result.success(notice);
    }


}
