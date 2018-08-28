package com.environment.program;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.net.UnknownHostException;

@MapperScan("com.environment.program.dao")
@SpringBootApplication
public class ProgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramApplication.class, args);
        try {
            Test.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
