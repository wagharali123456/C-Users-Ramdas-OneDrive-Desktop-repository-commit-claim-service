package com.org.fms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClaimApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClaimApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Started: " );
    }
}
