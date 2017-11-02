package com.github.ibiber.jutrack.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import com.github.ibiber.jutrack.domain.IssuesFilter;
import com.github.ibiber.jutrack.domain.data.jira.Author;
import com.github.ibiber.jutrack.domain.data.jira.History;

public class IssuesFilterTest {
	LocalDate start = LocalDate.of(2017, 9, 1);
	LocalDate end = LocalDate.of(2017, 9, 15);

	Author author = new Author("testUsr", null, null, null);

	IssuesFilter<?> testee = new IssuesFilter<>();

	@Test
	public void filter() {
		History history = new History(null, author, "2017-09-01T08:51:35.055+0200", null);
		assertThat(testee.filterUserAndDate("testUsr", start, end, history), is(true));
	}

	@Test
	public void filterUserDoesNotMatch() {
		History history = new History(null, author, "2017-09-01T08:51:35.055+0200", null);
		assertThat(testee.filterUserAndDate("anotherUsr", start, end, history), is(false));
	}

	@Test
	public void filterBeforeStartDate() {
		History history = new History(null, author, "2017-08-30T08:51:35.055+0200", null);
		assertThat(testee.filterUserAndDate("testUsr", start, end, history), is(false));
	}

	@Test
	public void filterAfterEndDate() {
		History history = new History(null, author, "2017-09-16T08:51:35.055+0200", null);
		assertThat(testee.filterUserAndDate("testUsr", start, end, history), is(false));
	}
}
