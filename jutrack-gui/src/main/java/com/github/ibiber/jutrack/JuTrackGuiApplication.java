package com.github.ibiber.jutrack;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.github.ibiber")
public class JuTrackGuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JuTrackGuiApplication.class, args);
    }

    @Bean
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }
}
