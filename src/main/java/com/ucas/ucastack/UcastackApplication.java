package com.ucas.ucastack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.ucas.ucastack.dao")
@EnableScheduling
public class UcastackApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcastackApplication.class, args);
    }

}
