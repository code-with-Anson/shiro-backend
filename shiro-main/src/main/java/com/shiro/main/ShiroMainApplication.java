package com.shiro.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.shiro.main.mapper")
public class ShiroMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroMainApplication.class, args);
    }

}
