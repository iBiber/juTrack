package com.github.ibiber.common.http;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UrlParameterBuilder {
	private List<ParameterEntry> parameterList = new ArrayList<>();

	private class ParameterEntry {
		private final String key;
		private final String value;

		public ParameterEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key + '=' + value;
		}
	}

	public UrlParameterBuilder add(String key, String value) {
		parameterList.add(new ParameterEntry(key, value));
		return this;
	}

	public String build() {
		return parameterList.stream().map(ParameterEntry::toString).collect(Collectors.joining("&"));
	}
}
