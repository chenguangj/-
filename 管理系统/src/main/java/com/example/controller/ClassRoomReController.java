package com.example.controller;


import com.example.service.ClassRoomReService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/classroomRe")
public class ClassRoomReController {

    @Resource
    private ClassRoomReService classRoomReService;

}
