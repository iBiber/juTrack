package com.github.ibiber.jutrack.external;

import com.github.ibiber.jutrack.external.data.JiraQueryParmeter;

public interface JiraIssuesProcessorService {

	void getIssues(JiraQueryParmeter parameter, GetIssueResultItemPresenter presenter);

}