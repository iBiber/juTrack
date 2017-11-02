package com.github.ibiber.common.general;

public class Strings {
	public static boolean isNullOrEmpty(String string) {
		if (string == null) {
			return true;
		}
		return string.isEmpty();
	}
}
