package com.ucas.ucastack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ucas.ucastack.dao")
public class UcastackApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcastackApplication.class, args);
    }

}
