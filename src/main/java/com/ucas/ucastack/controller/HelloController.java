package com.ucas.ucastack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ucastack
 * @description: 这个controller主要用于初始化测试
 * @author: Liang Yu
 */
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String helloworld() {
        return "Hello Spring boot";
    }
}
