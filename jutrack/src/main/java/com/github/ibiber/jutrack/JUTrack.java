package com.github.ibiber.jutrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Lazy
@SpringBootApplication
public class JUTrack extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(JUTrack.class);

	private ConfigurableApplicationContext appContext;
	@Autowired
	private JuTrackGui parameterDialog;
	@Autowired
	private DefaultValueProvider defaultValueProvider;
	@Autowired
	private JiraIssuesProcessor jiraIssuesProcessor;

	@Override
	public void start(final Stage primaryStage) {
		LOGGER.info("Open UI");
		parameterDialog.init(defaultValueProvider, jiraIssuesProcessor::getIssues);
		primaryStage.setTitle("Get Jira issues");
		primaryStage.setScene(new Scene(parameterDialog));
		primaryStage.setResizable(true);

		Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");

			Text text = new Text(throwable.getMessage());
			text.setWrappingWidth(600);

			alert.getDialogPane().setContent(text);
			alert.showAndWait();
		});

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
