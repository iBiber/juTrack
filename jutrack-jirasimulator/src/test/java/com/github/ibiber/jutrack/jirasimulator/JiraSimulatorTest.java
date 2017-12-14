package com.github.ibiber.jutrack.jirasimulator;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JiraSimulatorTest {

	@Mock
	private CurrentDateTimeProvider currentDateTimeProvider;

	@InjectMocks
	private JiraSimulator testee;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(currentDateTimeProvider.currentDateTime()).thenReturn(LocalDateTime.of(2017, 12, 17, 13, 40));
	}

	@Test
	public void updateDate_ShouldOnlyUpdateDatesNoDateContained() throws Exception {
		assertThat(testee.updateDate("No Date"), is("No Date"));
	}

	@Test
	public void updateDate_ShouldOnlyUpdateDates() throws Exception {
		assertThat(testee.updateDate(" \"created\": \"2017-08-25T21:50:00.484+0200\","),
		        is(" \"created\": \"2017-12-14T13:40:00.000+0200\","));
	}
}
