package com.example.databasedesign.controller;

import com.example.databasedesign.mapper.UserMapper;
import com.example.databasedesign.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Jeff.zheng
 * @description:
 * @date: 2023/10/26 15:45
 * @version: 1.0.0
 */
@RestController()
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @GetMapping("/get")
    public String get() {
        return "get11";
    }


    @GetMapping("/data")
    public String data() {
        List<User> users = userMapper.selectList(null);
        return users.toString();
    }


}
