package com.example.controller;


import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.ClassRoom;
import com.example.entity.ClassRoomRe;
import com.example.entity.StudentInfo;
import com.example.entity.TeacherInfo;
import com.example.exception.CustomException;
import com.example.service.ClassRoomService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/classroom")
public class ClassRoomController {

    @Resource
    private ClassRoomService classRoomService;

    @GetMapping
    public Result selectAll(@RequestParam String name , @RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo<ClassRoom> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = classRoomService.selectByName(name , pageNum , pageSize);
        }else {
            info = classRoomService.findAll(pageNum , pageSize);
        }
        return Result.success(info);
    }

    @PutMapping("/getReSt")
    public Result selectAllReSt(@RequestParam String name , @RequestParam Integer pageNum , @RequestParam Integer pageSize , @RequestBody StudentInfo studentInfo){
        PageInfo<ClassRoomRe> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = classRoomService.selectByNameSt(name , pageNum , pageSize , studentInfo);
        }else {
            info = classRoomService.findAllSt(pageNum , pageSize , studentInfo);
        }
        return Result.success(info);
    }

    @PutMapping("/getReTe")
    public Result selectAllReTe(@RequestParam String name , @RequestParam Integer pageNum , @RequestParam Integer pageSize , @RequestBody TeacherInfo teacherInfo){
        PageInfo<ClassRoomRe> info = new PageInfo<>();
        if (ObjectUtil.isNotEmpty(name)) {
            info = classRoomService.selectByNameTe(name , pageNum , pageSize , teacherInfo);
        }else {
            info = classRoomService.findAllTe(pageNum , pageSize , teacherInfo);
        }
        return Result.success(info);
    }

    @PostMapping
    public Result add(@RequestBody ClassRoom classRoom){
        if (ObjectUtil.isNotEmpty(classRoomService.selectName2(classRoom.getName()))){
            return Result.error("-1" , "当前创建的教室已存在");
        }
        classRoomService.add(classRoom);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody ClassRoom classRoom){
        if (ObjectUtil.isNotEmpty(classRoomService.selectName(classRoom))){
            return Result.error("-1" , "当前修改的名字已被其他教室占用，请修改成其他名字");
        }
        classRoomService.update(classRoom);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Long id){
        classRoomService.delete(id);
        return Result.success();
    }

    @PutMapping("/delSelected")
    public Result delSelected(@RequestBody List<ClassRoom> classRoomList){
        for (ClassRoom classRoom : classRoomList) {
            classRoomService.delete(classRoom.getId());
        }
        return Result.success();
    }

    @PostMapping("/re")
    public Result re(@RequestBody ClassRoom classRoom , @RequestParam Long userId , @RequestParam String level){
        if (ObjectUtil.isEmpty(classRoom.getInitTime()) || ObjectUtil.isEmpty(classRoom.getDestroyTime())) {
            return Result.error("-1" , "请将开始时间和结束时间填写完整");
        }
        String[] split = classRoom.getInitTime().split("-");
        String[] split1 = classRoom.getDestroyTime().split("-");
        LocalDateTime localDateTime = LocalDateTime.now();
        int isTrue = 0;

        if (localDateTime.getYear() > Integer.valueOf(split[0]) && isTrue == 0){
            return Result.error("-1" , "你预约的时间的年份不能比当前时间的年份还小");
        } else if (localDateTime.getYear() < Integer.valueOf(split[0])) {
            isTrue = 1;
        }

        if (localDateTime.getMonthValue() > Integer.valueOf(split[1]) && isTrue == 0){
            return Result.error("-1" , "你预约的时间的月份不能比当前时间的月份还小");
        } else if (localDateTime.getMonthValue() < Integer.valueOf(split[1])) {
            isTrue = 1;
        }

        if (localDateTime.getDayOfMonth() > Integer.valueOf(split[2]) && isTrue == 0){
            return Result.error("-1" , "你预约的时间的日期不能比当前时间的日期还小");
        } else if (localDateTime.getDayOfMonth() < Integer.valueOf(split[2])) {
            isTrue = 1;
        }


        if (localDateTime.getHour() > Integer.valueOf(split[3]) && isTrue == 0){
            return Result.error("-1" , "你预约的时间的小时不能比当前时间的小时还小");
        } else if (localDateTime.getHour() < Integer.valueOf(split[3])) {
            isTrue = 1;
        }

        if (localDateTime.getMinute() > Integer.valueOf(split[4]) && isTrue == 0){
            return Result.error("-1" , "你预约的时间的分钟不能比当前时间的分钟还小");
        } else if (localDateTime.getMinute() < Integer.valueOf(split[4])) {
            isTrue = 1;
        }

        for (int i = 0; i < split.length; i++) {
            if (Long.valueOf(split[i]) < Long.valueOf(split1[i])) {
                break;
            }
            if (Long.valueOf(split[i]) > Long.valueOf(split1[i])) {
                return Result.error("-1" , "结束的时间不能比开始的时间小");
            }
        }
        classRoomService.re(classRoom , userId , level);
        return Result.success();
    }


    @PutMapping("/agree")
    public Result agree(@RequestBody ClassRoom classRoom){
        classRoomService.agree(classRoom);
        return Result.success();
    }

    @PutMapping("/disagree")
    public Result disagree(@RequestBody ClassRoom classRoom){
        classRoomService.disagree(classRoom);
        return Result.success();
    }

    @PutMapping("/forcere")
    public Result forcere(@RequestBody ClassRoom classRoom){
        classRoomService.forcere(classRoom);
        return Result.success();
    }

    @PutMapping("/tuidingSt")
    public Result tuidingSt(@RequestParam Long id , @RequestBody StudentInfo studentInfo){
        StudentInfo studentInfo1 = classRoomService.tuidingSt(id , studentInfo);
        return Result.success(studentInfo1);
    }


    @PutMapping("/tuidingTe")
    public Result tuidingTe(@RequestParam Long id , @RequestBody TeacherInfo teacherInfo){
        TeacherInfo teacherInfo1 = classRoomService.tuidingTe(id , teacherInfo , true);
        return Result.success(teacherInfo1);
    }

    @GetMapping("/cource")
    public Result getClassGiveCource(){
        List<ClassRoom> option = classRoomService.getClassGiveCource();
        return Result.success(option);
    }

}
