package com.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lms")
public class SmartLmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartLmsApplication.class, args);
    }
}
