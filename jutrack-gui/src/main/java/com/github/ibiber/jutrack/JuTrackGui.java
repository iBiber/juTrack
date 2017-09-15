package com.github.ibiber.jutrack;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;
import com.github.ibiber.jutrack.util.Strings;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

@Component
public class JuTrackGui extends GridPane implements GetIssueResultItemPresenter {
	private TextArea resultArea;

	public JuTrackGui() {
		super();
	}

	public void init(DefaultValueProvider defProperties, Consumer<GetIssuesParmeter> callback) {
		GridPane gridpane = this;
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(5);
		gridpane.setVgap(5);

		int row = 0;
		TextField jiraRootUrlField = new TextField();
		jiraRootUrlField.setText(defProperties.getJiraRootUrl());
		gridpane.add(new Label("Jira root URL: "), 0, row);
		gridpane.add(jiraRootUrlField, 1, row++);

		TextField userNameField = new TextField();
		userNameField.setText(defProperties.getUserName());
		gridpane.add(new Label("Jira user Name: "), 0, row);
		gridpane.add(userNameField, 1, row++);

		PasswordField passwordField = new PasswordField();
		gridpane.add(new Label("Jira password: "), 0, row);
		gridpane.add(passwordField, 1, row++);

		DatePicker startDatePicker = new DatePicker(LocalDate.now().minusDays(defProperties.dayRange()));
		startDatePicker.getEditor().focusedProperty()
		        .addListener((obs, old, isFocused) -> updateDatePickerStructure(startDatePicker, obs, old, isFocused));
		gridpane.add(new Label("Start date"), 0, row);
		gridpane.add(startDatePicker, 1, row++);

		DatePicker endDatePicker = new DatePicker(LocalDate.now());
		endDatePicker.getEditor().focusedProperty()
		        .addListener((obs, old, isFocused) -> updateDatePickerStructure(startDatePicker, obs, old, isFocused));
		gridpane.add(new Label("End date"), 0, row);
		gridpane.add(endDatePicker, 1, row++);

		Button startAction = new Button("Ok");
		startAction.defaultButtonProperty();
		startAction.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (Strings.isNullOrEmpty(jiraRootUrlField.getText())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Missing jira root URL");
					alert.setHeaderText("The jira root URL is mandatory!");
					alert.setContentText("A jira root URL could look like 'http://jira.your-domain.com'.\n"
					        + "You can set the jira root URL either in the text field or"
					        + "define a default setting in a 'config/application.properties' " + "file with property '"
					        + defProperties.getJiraRootUrlPropertyName() + "'.");

					alert.showAndWait();
				} else {
					GetIssuesParmeter parameter = new GetIssuesParmeter(jiraRootUrlField.getText(),
					        userNameField.getText(), passwordField.getText(), startDatePicker.getValue(),
					        endDatePicker.getValue());
					callback.accept(parameter);
				}
			}
		});
		gridpane.add(startAction, 1, row++);
		GridPane.setValignment(startAction, VPos.TOP);
		GridPane.setHalignment(startAction, HPos.RIGHT);

		resultArea = new TextArea();
		resultArea.setPrefSize(600, 400);
		resultArea.setMaxWidth(Double.MAX_VALUE);
		resultArea.setMaxHeight(Double.MAX_VALUE);
		gridpane.add(resultArea, 2, 0, 1, row);
		GridPane.setHgrow(resultArea, Priority.ALWAYS);
		GridPane.setVgrow(resultArea, Priority.ALWAYS);
	}

	private void updateDatePickerStructure(DatePicker datePicker, ObservableValue<? extends Boolean> observable,
	        Boolean oldValue, Boolean isFocused) {
		// This is an workaround for
		// https://bugs.openjdk.java.net/browse/JDK-8136838
		// (fixed with Java 8u72)
		if (!isFocused) {
			datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
		}
	}

	@Override
	public void presentResults(GetIssuesParmeter parameter, Stream<GetIssueResultItem> resultStream) {
		// Show result to GUI
		this.resultArea.setText(resultStream//
		        .map(item -> item.getDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "\t"
		                + item.key + "\t" + item.changeAction + "\t" + item.summary + "\t" + parameter.jiraRootUrl
		                + "/browse/" + item.key)//
		        .collect(Collectors.joining("\n")));
	}
}