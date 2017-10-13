package com.github.ibiber.jutrack;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;
import com.github.ibiber.jutrack.util.Strings;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

@Component
public class JuTrackGui extends GridPane implements GetIssueResultItemPresenter {
	private TextArea resultArea;
	private TableView table;

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
		        .addListener((obs, old, isFocused) -> updateDatePickerStructure(startDatePicker, isFocused));
		gridpane.add(new Label("Start date"), 0, row);
		gridpane.add(startDatePicker, 1, row++);

		DatePicker endDatePicker = new DatePicker(LocalDate.now());
		endDatePicker.getEditor().focusedProperty()
		        .addListener((obs, old, isFocused) -> updateDatePickerStructure(startDatePicker, isFocused));
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

		TabPane tabPane = new TabPane();
		tabPane.setMaxWidth(Double.MAX_VALUE);
		tabPane.setMaxHeight(Double.MAX_VALUE);
		tabPane.setPrefSize(600, 400);

		Tab tableTab = new Tab("Table View");
		table = new TableView();
		tableTab.setContent(table);

		Tab textTab = new Tab("Text View");
		resultArea = new TextArea();
		textTab.setContent(resultArea);
		tabPane.getTabs().addAll(tableTab, textTab);

		gridpane.add(tabPane, 2, 0, 1, row);
		GridPane.setHgrow(tabPane, Priority.ALWAYS);
		GridPane.setVgrow(tabPane, Priority.ALWAYS);
	}

	private void updateDatePickerStructure(DatePicker datePicker, Boolean isFocused) {
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
		table.getColumns().clear();
		createHeaderColumns(parameter);

		// Fill table data and result text area
		Rows rows = new Rows();
		String resultText = resultStream//
		        .peek(item -> rows.markAction(item.key, item.created)) // Collect data for table
		        .map(item -> item.created.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) + "\t"
		                + item.key + "\t" + item.changeAction + "\t" + item.summary + "\t" + parameter.jiraRootUrl
		                + "/browse/" + item.key)//
		        .collect(Collectors.joining("\n"));
		this.resultArea.setText(resultText);

		table.setItems(FXCollections.observableArrayList(rows.getRows()));
	}

	class Rows {
		private Map<String, Row> rowsByIssueKey = new TreeMap<>();

		public void markAction(String key, LocalDateTime dateTime) {
			Row row = rowsByIssueKey.get(key);
			if (row == null) {
				row = new Row(key);
				rowsByIssueKey.put(key, row);
			}
			row.markDate(dateTime);
		}

		public Collection<Row> getRows() {
			return rowsByIssueKey.values();
		}
	}

	class Row {
		private final String issueKey;
		private Map<String, String> columnValuesByColumnTitle = new HashMap<>();

		public Row(String issueKey) {
			this.issueKey = issueKey;
		}

		public void markDate(LocalDateTime dateTime) {
			columnValuesByColumnTitle.put(getColumnHeader(dateTime), "X");
		}

		public String getIssueKey() {
			return issueKey;
		}

		public String getValueByColumnTitle(String columnTitle) {
			return columnValuesByColumnTitle.get(columnTitle);
		}
	}

	private void createHeaderColumns(GetIssuesParmeter parameter) {
		// Build first column
		List<TableColumn> columns = new ArrayList<>();
		TableColumn<Row, String> issuesColumn = new TableColumn<>("Issue");
		issuesColumn.setCellValueFactory(row -> new ReadOnlyObjectWrapper<>(row.getValue().getIssueKey()));
		columns.add(issuesColumn);

		// Create one column per day
		long daysBetween = ChronoUnit.DAYS.between(parameter.startDate, parameter.endDate);
		IntStream.range(0, (int) daysBetween + 1).forEach(index -> {
			LocalDate d = parameter.startDate.plusDays(index);
			String columnTitle = getColumnHeader(d);
			TableColumn<Row, String> col = new TableColumn<>(columnTitle);
			col.setCellValueFactory(
			        row -> new ReadOnlyObjectWrapper<>(row.getValue().getValueByColumnTitle(columnTitle)));
			columns.add(col);
		});
		table.getColumns().addAll(columns);
	}

	private String getColumnHeader(Temporal dateTime) {
		return DateTimeFormatter.ofPattern("dd.MM").format(dateTime);
	}
}
