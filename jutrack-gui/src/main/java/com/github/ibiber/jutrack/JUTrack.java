package com.github.ibiber.jutrack;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ibiber.jutrack.data.JiraQueryResultItem;
import com.github.ibiber.jutrack.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.data.jira.Issue;
import com.github.ibiber.jutrack.data.jira.JiraIssuesQueryResults;
import com.github.ibiber.jutrack.util.ApplicationVersionProvider;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Lazy
@SpringBootApplication
public class JUTrack extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(JUTrack.class);

	/**
	 * this can be activated for easy testing with a pre-filled result
	 * 
	 * @see #PREFILL_EXAMPLE_JSON_PATH_PARAMETER
	 */
	private final boolean prefillDataForFastTestingPurpose = false;
	/**
	 * To pre-fill the result, this program parameter must point to the path to the example.json. Usage:<br>
	 * 
	 * <pre>
	 * --example.json.path="/[project_path]/juTrack/jutrack-core/src/test/resources/example.json"
	 * </pre>
	 */
	private static final String PREFILL_EXAMPLE_JSON_PATH_PARAMETER = "example.json.path";

	private ConfigurableApplicationContext appContext;

	@Autowired
	private ParameterPane parameterPane;
	@Autowired
	private ResultPane resultPane;
	@Autowired
	private DefaultValueProvider defaultValueProvider;
	@Autowired
	private JiraIssuesProcessor jiraIssuesProcessor;
	@Autowired
	private ApplicationVersionProvider versionProvider;

	@Override
	public void start(final Stage primaryStage) {
		LOGGER.info("Open UI");

		if (prefillDataForFastTestingPurpose) {
			prefillParameterDataForFastTestingPurpose();
		}

		parameterPane.init(defaultValueProvider, jiraIssuesProcessor::getIssues);
		resultPane.init();

		// Build main pane
		BorderPane mainPane = new BorderPane();
		mainPane.setLeft(parameterPane);
		mainPane.setCenter(resultPane);

		primaryStage.setTitle("juTrack - " + versionProvider.getApplicationVersion());
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setResizable(true);

		Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");

			Text text = new Text(throwable.getMessage());
			text.setWrappingWidth(600);

			throwable.printStackTrace();

			alert.getDialogPane().setContent(text);
			alert.showAndWait();
		});

		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		primaryStage.show();

		if (prefillDataForFastTestingPurpose) {
			prefillResultDataForFastTestingPurpose();
		}
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

	private DefaultValueProvider prefillParameterDataForFastTestingPurpose() {
		return defaultValueProvider = new DefaultValueProvider(null) {
			@Override
			public String getJiraRootUrl() {
				return "http://localhost:8080";
			}

			@Override
			public String getUserName() {
				return "user_01";
			}

			@Override
			public int dayRange() {
				return 30;
			}

		};
	}

	private void prefillResultDataForFastTestingPurpose() {
		ObjectMapper mapper = new ObjectMapper();
		List<Issue> issues = new ArrayList<>();
		try {
			String exampleJsonPath = getParameters().getNamed().get(PREFILL_EXAMPLE_JSON_PATH_PARAMETER);
			issues = mapper.readValue(new File(exampleJsonPath), JiraIssuesQueryResults.class).issues;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		IssuesFilter<JiraQueryResultItem> filter = new IssuesFilter<>();
		LocalDate startDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2017-08-01"));
		LocalDate endDate = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2017-08-29"));
		List<JiraQueryResultItem> resultList = filter.execute("user_01", issues, startDate, endDate,
		        (issue, history, historyItem, itemType) -> new JiraQueryResultItem(history.getDateTime(), issue.key,
		                issue.getSummary(), itemType + ": " + historyItem.toString));

		resultPane.presentResults(
		        new JiraQueryParmeter("http://localhost:8080", "user_01", "anyPWD", startDate, endDate),
		        resultList.stream());
	}

	@Configuration
	protected class Config {
		@Bean
		public HostServices hostServices() {
			return getHostServices();
		}
	}
}
