package com.example.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "teacher_info")
public class TeacherInfo extends Account{

    @Alias("职工号")
    @Column(name = "teacherId")
    public String teacherId;

    @Column(name = "isTrue")
    public Long isTrue;

    @Column(name = "classroomRe")
    private String classroomRe;

    @Column(name = "type")
    private String type;

    @Column(name = "courceId")
    private String courceId = "-1";

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

    public boolean equals(TeacherInfo obj) {
        if (this.getName().equals(obj.getName()) && this.getId() == obj.getId()) {
            return true;
        }
        return false;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Long getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(Long isTrue) {
        this.isTrue = isTrue;
    }
}
