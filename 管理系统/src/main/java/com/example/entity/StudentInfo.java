package com.example.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name = "student_info")
public class StudentInfo extends Account{
    @Alias("学号")
    @Column(name = "studentId")
    private String studentId;

    @Column(name = "isTrue")
    private Long isTrue;

    @Column(name = "classroomRe")
    private String classroomRe;

    @Column(name = "type")
    private String type;

    @Column(name = "courceId")
    private String courceId = "-1";

    @Column(name = "img")
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Column(name = "homeworkscore")
    private String homeworkscore;

    public String getHomeworkscore() {
        return homeworkscore;
    }

    public void setHomeworkscore(String homeworkscore) {
        this.homeworkscore = homeworkscore;
    }

    @Override
    public String getCourceId() {
        return courceId;
    }

    @Override
    public void setCourceId(String courceId) {
        this.courceId = courceId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public String getClassroomRe() {
        return classroomRe;
    }

    public void setClassroomRe(String classroomRe) {
        this.classroomRe = classroomRe;
    }

    public boolean equals(StudentInfo obj) {
        if (this.getName().equals(obj.getName()) && this.getId() == obj.getId()) {
            return true;
        }
        return false;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Long getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(Long isTrue) {
        this.isTrue = isTrue;
    }
}
