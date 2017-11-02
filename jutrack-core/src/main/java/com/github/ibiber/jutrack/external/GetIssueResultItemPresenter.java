package com.github.ibiber.jutrack.external;

import java.util.stream.Stream;

import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;
import com.github.ibiber.jutrack.external.data.JiraQueryResultItem;

public interface GetIssueResultItemPresenter {
	public void presentResults(JiraQueryParmeter parameter, Stream<JiraQueryResultItem> resultStream);
}
