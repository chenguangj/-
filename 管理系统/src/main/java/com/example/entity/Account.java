package com.example.entity;

import cn.hutool.core.annotation.Alias;

import javax.persistence.*;

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Alias("名字")
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Alias("性别")
    @Column(name = "sex")
    private String sex;
    @Alias("年龄")
    @Column(name = "age")
    private Long age;
    @Alias("权限")
    @Column(name = "level")
    private String level;

    @Transient
    private String verCode;

    @Transient
    private String type;

//    下面这个赋值为 -1 很厉害
    @Transient
    private String courceId = "-1";

    public String getCourceId() {
        return courceId;
    }

    public void setCourceId(String courceId) {
        this.courceId = courceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Transient
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
