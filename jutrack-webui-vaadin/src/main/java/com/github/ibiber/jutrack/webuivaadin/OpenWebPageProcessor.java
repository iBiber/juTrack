package com.github.ibiber.jutrack.webuivaadin;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OpenWebPageProcessor implements ApplicationContextInitializer<ConfigurableApplicationContext>,
		ApplicationListener<WebServerInitializedEvent> {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		applicationContext.addApplicationListener(this);
	}

	@Override
	public void onApplicationEvent(WebServerInitializedEvent event) {
		try {
			Desktop.getDesktop().browse(new URI("http://localhost:" + event.getWebServer().getPort()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
