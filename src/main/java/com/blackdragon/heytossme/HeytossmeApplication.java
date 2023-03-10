package com.blackdragon.heytossme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class HeytossmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeytossmeApplication.class, args);
    }

}
