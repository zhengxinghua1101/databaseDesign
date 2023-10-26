package com.example.databasedesign.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/26 15:45
 * @version: 1.0.0
 */
@RestController()
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/get")
    public String get() {
        return "get11";
    }


}
