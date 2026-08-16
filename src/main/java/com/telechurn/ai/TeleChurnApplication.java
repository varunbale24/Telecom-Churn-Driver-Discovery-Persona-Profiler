package com.telechurn.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TeleChurnApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeleChurnApplication.class, args);
    }
}
