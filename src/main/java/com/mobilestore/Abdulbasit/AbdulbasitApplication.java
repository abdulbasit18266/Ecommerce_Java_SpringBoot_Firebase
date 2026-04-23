package com.mobilestore.Abdulbasit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mobilestore.Abdulbasit") // Ye line add karo
public class AbdulbasitApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbdulbasitApplication.class, args);
    }
}