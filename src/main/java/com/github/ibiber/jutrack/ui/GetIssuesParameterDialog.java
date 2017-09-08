package com.github.ibiber.jutrack.ui;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.JiraIssuesProcessor;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

@Component
public class GetIssuesParameterDialog extends GridPane {
	@Autowired
	public GetIssuesParameterDialog(Environment env,
			JiraIssuesProcessor callback) {
		super();

		GridPane gridpane = this;
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(5);
		gridpane.setVgap(5);

		String jiraRootUrlProperty = "ui.jira.root.url.default";

		int row = 0;
		TextField jiraRootUrlField = new TextField();
		String jiraRootUrl = env.getProperty(jiraRootUrlProperty);
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

		int dayRange = env.getProperty("ui.startDate.default.day.range",
				Integer.class, 1);
		DatePicker startDatePicker = new DatePicker(
				LocalDate.now().minusDays(dayRange));
		startDatePicker.getEditor().focusedProperty()
				.addListener((obs, old, isFocused) -> updateDatePickerStructure(
						startDatePicker, obs, old, isFocused));
		gridpane.add(new Label("Start date"), 0, row);
		gridpane.add(startDatePicker, 1, row++);

		DatePicker endDatePicker = new DatePicker(LocalDate.now());
		endDatePicker.getEditor().focusedProperty()
				.addListener((obs, old, isFocused) -> updateDatePickerStructure(
						startDatePicker, obs, old, isFocused));
		gridpane.add(new Label("End date"), 0, row);
		gridpane.add(endDatePicker, 1, row++);

		Button startAction = new Button("Ok");
		startAction.defaultButtonProperty();
		startAction.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (jiraRootUrlField.getText() == null
						|| jiraRootUrlField.getText().isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Missing jira root URL");
					alert.setHeaderText("The jira root URL is mandatory!");
					alert.setContentText(
							"A jira root URL could look like 'http://jira.your-domain.com'.\n"
									+ "You can set the jira root URL either in the text field or"
									+ "define a default setting in a 'config/application.properties' "
									+ "file with property '"
									+ jiraRootUrlProperty + "'.");

					alert.showAndWait();
				} else {
					GetIssuesParmeter parameter = new GetIssuesParmeter(
							jiraRootUrlField.getText(), userNameField.getText(),
							passwordField.getText(), startDatePicker.getValue(),
							endDatePicker.getValue());
					callback.getIssues(parameter);
				}
			}
		});
		gridpane.add(startAction, 1, row++);

		GridPane.setHalignment(startAction, HPos.RIGHT);
	}

	private void updateDatePickerStructure(DatePicker datePicker,
			ObservableValue<? extends Boolean> observable, Boolean oldValue,
			Boolean isFocused) {
		// This is an workaround for
		// https://bugs.openjdk.java.net/browse/JDK-8136838
		// (fixed with Java 8u72)
		if (!isFocused) {
			datePicker.setValue(datePicker.getConverter()
					.fromString(datePicker.getEditor().getText()));
		}
	}
}
