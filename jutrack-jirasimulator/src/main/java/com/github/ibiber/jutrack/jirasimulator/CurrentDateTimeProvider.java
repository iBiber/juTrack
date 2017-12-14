package com.github.ibiber.jutrack.jirasimulator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class CurrentDateTimeProvider {
	public LocalDateTime currentDateTime() {
		return LocalDateTime.now();
	}
}
