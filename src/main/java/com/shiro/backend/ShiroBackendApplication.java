package com.shiro.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.shiro.backend.mapper")
public class ShiroBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroBackendApplication.class, args);
    }

}
