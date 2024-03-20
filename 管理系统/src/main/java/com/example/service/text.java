package com.example.service;

import java.time.LocalDateTime;

public class text {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.getDayOfWeek());
    }
}
