package com.shiro.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shiro.backend.mapper")
public class ShiroBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroBackendApplication.class, args);
    }

}
