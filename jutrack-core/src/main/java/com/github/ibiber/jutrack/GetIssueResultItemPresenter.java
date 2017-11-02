package com.github.ibiber.jutrack;

import java.util.stream.Stream;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.JiraQueryParmeter;

public interface GetIssueResultItemPresenter {
	public void presentResults(JiraQueryParmeter parameter, Stream<GetIssueResultItem> resultStream);
}
