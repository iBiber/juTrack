package com.github.iBiber.juTrack.ui;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.github.iBiber.juTrack.data.GetIssuesParmeter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GetIssuesParameterDialog extends Stage {

	public GetIssuesParameterDialog(ApplicationContext appContext, Stage owner,
	        Consumer<GetIssuesParmeter> credentialConsumer) {
		super();
		initOwner(owner);

		Environment env = appContext.getBean(Environment.class);

		setTitle("Get Jira issues");
		Group root = new Group();
		Scene scene = new Scene(root, 300, 300, Color.WHITE);
		setScene(scene);

		GridPane gridpane = new GridPane();
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(5);
		gridpane.setVgap(5);

		String jiraRootUrlProperty = "ui.jira.root.url.default";

		int row = 0;
		TextField jiraRootUrlField = new TextField();
		String jiraRootUrl = appContext.getBean(Environment.class).getProperty(jiraRootUrlProperty);
		if (jiraRootUrl != null) {
			jiraRootUrlField.setText(jiraRootUrl);
		}
		gridpane.add(new Label("Jira root URL: "), 0, row);
		gridpane.add(jiraRootUrlField, 1, row++);

		TextField userNameField = new TextField();
		String defaultUser = env.getProperty("ui.username.default");
		if (defaultUser != null) {
			userNameField.setText(defaultUser);
		}
		gridpane.add(new Label("Jira user Name: "), 0, row);
		gridpane.add(userNameField, 1, row++);

		PasswordField passwordField = new PasswordField();
		gridpane.add(new Label("Jira password: "), 0, row);
		gridpane.add(passwordField, 1, row++);

		int dayRange = env.getProperty("ui.startDate.default.day.range", Integer.class, 1);
		DatePicker startDatePicker = new DatePicker(LocalDate.now().minusDays(dayRange));
		gridpane.add(new Label("Start date"), 0, row);
		gridpane.add(startDatePicker, 1, row++);

		DatePicker endDatePicker = new DatePicker(LocalDate.now());
		gridpane.add(new Label("End date"), 0, row);
		gridpane.add(endDatePicker, 1, row++);

		Button startAction = new Button("Ok");
		startAction.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (jiraRootUrlField.getText() == null || jiraRootUrlField.getText().isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Missing jira root URL");
					alert.setHeaderText("The jira root URL is mandatory!");
					alert.setContentText("A jira root URL could look like 'http://jira.your-domain.com'.\n"
					        + "You can set the jira root URL either in the text field or"
					        + "define a default setting in a 'config/application.properties' " + "file with property '"
					        + jiraRootUrlProperty + "'.");

					alert.showAndWait();
				} else {
					GetIssuesParmeter parameter = new GetIssuesParmeter(jiraRootUrlField.getText(),
					        userNameField.getText(), passwordField.getText(), startDatePicker.getValue(),
					        endDatePicker.getValue());
					credentialConsumer.accept(parameter);

					close();
				}
			}
		});
		gridpane.add(startAction, 1, row++);

		GridPane.setHalignment(startAction, HPos.RIGHT);
		root.getChildren().add(gridpane);
	}
}