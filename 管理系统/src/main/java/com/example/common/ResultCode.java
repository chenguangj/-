package com.example.common;

public enum ResultCode {
    SUCCESS("0", "成功"),
    ERROR("-1", "系统异常");

    public String code;
    public String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
