package com.example.entity;

import javax.persistence.*;

@Table(name = "classroom")
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "teacherId")
    private String teacherId;

    @Column(name = "studentId")
    private String studentId;

    @Column(name = "state")
    private String state;

    @Column(name = "initTime")
    private String initTime;

    @Column(name = "destroyTime")
    private String destroyTime;

    @Column(name = "isTrue")
    private Long isTrue;

    @Column(name = "classroomRe")
    private String classroomRe;

    public String getClassroomRe() {
        return classroomRe;
    }

    public void setClassroomRe(String classroomRe) {
        this.classroomRe = classroomRe;
    }

    @Transient
    private String reName = "未被任何人预约";

    public Long getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(Long isTrue) {
        this.isTrue = isTrue;
    }

    public String getReName() {
        return reName;
    }

    public void setReName(String reName) {
        this.reName = reName;
    }


    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public String getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(String destroyTime) {
        this.destroyTime = destroyTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
