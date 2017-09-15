package com.github.ibiber.jutrack;

import java.util.stream.Stream;

import com.github.ibiber.jutrack.data.GetIssueResultItem;
import com.github.ibiber.jutrack.data.GetIssuesParmeter;

public interface GetIssueResultItemPresenter {
	public void presentResults(GetIssuesParmeter parameter, Stream<GetIssueResultItem> resultStream);
}
