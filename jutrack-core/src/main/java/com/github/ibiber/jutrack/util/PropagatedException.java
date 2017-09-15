package com.github.ibiber.jutrack.util;

public class PropagatedException extends RuntimeException {
	public PropagatedException(Throwable cause) {
		super(cause);
	}
}
