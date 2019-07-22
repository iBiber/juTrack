package com.github.ibiber.jutrack.webuivaadin;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "com.github.ibiber.jutrack")
public class JutrackWebuiVaadinApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(JutrackWebuiVaadinApplication.class).headless(false).run(args);
	}
}
