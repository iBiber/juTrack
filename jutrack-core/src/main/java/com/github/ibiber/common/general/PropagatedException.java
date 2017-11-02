package com.github.ibiber.common.general;

public class PropagatedException extends RuntimeException {
	public PropagatedException(Throwable cause) {
		super(cause);
	}
}
