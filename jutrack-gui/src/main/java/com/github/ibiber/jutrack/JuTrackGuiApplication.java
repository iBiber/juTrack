package com.github.ibiber.jutrack;

import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "com.github.ibiber")
public class JuTrackGuiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(JuTrackGuiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(JuTrackGuiApplication.class, args);
    }

    @Bean
    Clock systemClock() {
        return Clock.systemDefaultZone();
    }

    @EventListener(ApplicationReadyEvent.class)
    void logLocalhostUsage(ApplicationReadyEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        String port = environment.getProperty("local.server.port");
        if (port != null) {
            String localUrl = "http://localhost:" + port;
            LOGGER.info("======= juTrack Web Console is running =======");
            LOGGER.info("Open this URL in your browser to use the UI:");
            LOGGER.info("{}", localUrl);
            LOGGER.info("==============================================");
        } else {
            LOGGER.info("juTrack Web Console is running. Open it on the configured host/port.");
        }
    }
}
