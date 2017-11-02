package com.github.ibiber.common.general;

public class Throwables {
	public static PropagatedException propagate(Throwable e) {
		throw new PropagatedException(e);
	}
}
