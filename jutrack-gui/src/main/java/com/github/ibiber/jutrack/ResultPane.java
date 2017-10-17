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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;

@Component
public class ResultPane extends TabPane implements GetIssueResultItemPresenter {
	private TextArea resultArea;
	private TableView issue;

	public void init() {
		double preferredWidth = Screen.getPrimary().getVisualBounds().getWidth() * 0.5;

		TabPane tabPane = this;
		tabPane.setMaxWidth(Double.MAX_VALUE);
		tabPane.setMaxHeight(Double.MAX_VALUE);
		tabPane.setPrefSize(preferredWidth, 400);

		Tab tableTab = new Tab("Table View");
		issue = new TableView();
		tableTab.setContent(issue);

		Tab textTab = new Tab("Text View");
		resultArea = new TextArea();
		textTab.setContent(resultArea);
		tabPane.getTabs().addAll(tableTab, textTab);
	}

	@Override
	public void presentResults(GetIssuesParmeter parameter, Stream<GetIssueResultItem> resultStream) {
		// Show result to GUI
		issue.getColumns().clear();
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

		issue.setItems(FXCollections.observableArrayList(rows.getRows()));
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
		issue.getColumns().addAll(columns);
	}

	private String getColumnHeader(Temporal dateTime) {
		return DateTimeFormatter.ofPattern("dd.MM").format(dateTime);
	}
}
