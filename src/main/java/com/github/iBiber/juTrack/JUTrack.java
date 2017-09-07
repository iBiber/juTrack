package com.github.iBiber.juTrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;

import com.github.iBiber.juTrack.ui.GetIssuesParameterDialog;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Lazy
@SpringBootApplication
public class JUTrack extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(JUTrack.class);

	private ConfigurableApplicationContext appContext;

	@Autowired
	private GetIssuesParameterDialog parameterDialog;

	@Override
	public void start(final Stage primaryStage) {
		LOGGER.info("Open UI");
		primaryStage.setTitle("Get Jira issues");
		primaryStage.setScene(new Scene(parameterDialog));
		primaryStage.setResizable(true);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		primaryStage.show();
		LOGGER.info("UI opened");
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		appContext.close();
	}

	@Override
	public void init() throws Exception {
		appContext = SpringApplication.run(JUTrack.class);
		appContext.getAutowireCapableBeanFactory().autowireBean(this);
	}

	public static void main(String args[]) {
		Application.launch(JUTrack.class, args);
	}
}
