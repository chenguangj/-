package com.example.controller;

import com.example.common.Result;
import com.example.entity.Log;
import com.example.entity.LogFen;
import com.example.service.LogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @PutMapping
    public Result getLog(@RequestParam Long id , @RequestParam String level){
        Log l = logService.getLog(id , level);
        return Result.success(l);
    }

    @PutMapping("/fen")
    public Result fen(@RequestBody Log log12) {
        List<LogFen> list = logService.fen(log12);
        return Result.success(list);
    }

}
