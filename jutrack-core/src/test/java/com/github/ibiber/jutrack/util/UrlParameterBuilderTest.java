package com.github.ibiber.jutrack.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class UrlParameterBuilderTest {
	private UrlParameterBuilder testee;

	@Before
	public void setUp() {
		testee = new UrlParameterBuilder();
	}

	@Test
	public void shouldBuildWithoutParameter() {
		assertThat(testee.build(), is(""));
	}

	@Test
	public void shouldBuildWithOneParameter() {
		testee.add("k5", "v5");
		assertThat(testee.build(), is("k5=v5"));
	}

	@Test
	public void shouldBuildWithEmptyKey() {
		testee.add("", "v5");
		assertThat(testee.build(), is("=v5"));
	}

	@Test
	public void shouldBuildWithEmptyValue() {
		testee.add("k5", "");
		assertThat(testee.build(), is("k5="));
	}

	@Test
	public void shouldBuildWithManyParametersAsTheyWereAdded() {
		testee.add("k5", "v5").add("k1", "v1");
		assertThat(testee.build(), is("k5=v5&k1=v1"));
	}

	@Test
	public void shouldBuildWithSameKeys() {
		testee.add("k5", "v5").add("k1", "v1").add("k5", "v1");
		assertThat(testee.build(), is("k5=v5&k1=v1&k5=v1"));
	}

}
