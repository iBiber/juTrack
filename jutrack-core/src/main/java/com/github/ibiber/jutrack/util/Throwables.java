package com.github.ibiber.jutrack.util;

public class Throwables {
	public static PropagatedException propagate(Throwable e) {
		throw new PropagatedException(e);
	}
}
