package com.example.service;

import com.example.dao.ClassRoomReDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ClassRoomReService {

    @Resource
    private ClassRoomReDao classRoomReDao;


}
