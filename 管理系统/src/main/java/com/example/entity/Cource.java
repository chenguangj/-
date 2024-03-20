package com.example.entity;

import javax.persistence.*;

@Table(name = "cource")
public class Cource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "teacherId")
    private Long teacherId;

    @Column(name = "state")
    private Long state;

    @Column(name = "classroom")
    private String classroom;

    @Column(name = "capacity")
    private Long capacity;

    @Column(name = "type")
    private String type;

    @Column(name = "date")
    private String date;

    @Transient
    private String[] formType;

    @Column(name = "isTrue")
    private Long isTrue;

    @Column(name = "week")
    private String week;

    @Column(name = "weekDay")
    private String weekDay;

    @Transient
    private String weekName;

    @Column(name = "img")
    private String img;

    @Transient
    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Long getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(Long isTrue) {
        this.isTrue = isTrue;
    }

    @Transient
    private String teacherName = "未被任何老师选教";

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String[] getFormType() {
        return formType;
    }

    public void setFormType(String[] formType) {
        this.formType = formType;
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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
