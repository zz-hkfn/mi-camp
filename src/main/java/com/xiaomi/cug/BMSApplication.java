package com.xiaomi.cug;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
@MapperScan("com.xiaomi.cug.mapper")
public class BMSApplication {
        public static void main(String[] args) {
                SpringApplication.run(BMSApplication.class, args);
        }
}
