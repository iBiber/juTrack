package com.github.ibiber.jutrack.webuivaadin;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.github.ibiber.jutrack.external.GetIssueResultItemPresenter;
import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.ThemableLayout;

@Tag("result-panel")
@Component
@Scope("prototype")
public class ResultPanel extends com.vaadin.flow.component.Component
		implements GetIssueResultItemPresenter, HasComponents, HasSize, ThemableLayout {
	private Grid<JiraQueryResultItem> grid;

	public ResultPanel() {
		grid = new Grid<JiraQueryResultItem>();
		add(grid);

		setWidth("100%");
		setSpacing(true);
		setPadding(true);

	}

	class QueryResult {
		String issue;

	}

	@Override
	public void presentResults(JiraQueryParmeter parameter, Stream<JiraQueryResultItem> resultStream) {
		grid.removeAllColumns();
		grid.setItems(resultStream);
		grid.addColumn(JiraQueryResultItem::getKey).setHeader("Issue");

		long daysBetween = ChronoUnit.DAYS.between(parameter.startDate, parameter.endDate);
		IntStream.range(0, (int) daysBetween + 1).mapToObj(index -> {
			return DateTimeFormatter.ofPattern("dd.MM")//
					.format(parameter.startDate.plusDays(index));

		}).forEach(columnTitle -> grid.addColumn(item -> mapToColumn(columnTitle, item)).setHeader(columnTitle));
	}

	public String mapToColumn(String columnHeader, JiraQueryResultItem item) {
		String columnTitle = DateTimeFormatter.ofPattern("dd.MM").format(item.getCreated());
		if (columnTitle.contentEquals(columnHeader)) {
			return item.getChangeAction();
		}

		return null;
	}
}
