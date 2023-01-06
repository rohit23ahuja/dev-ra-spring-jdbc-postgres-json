package dev.ra.spring.postgres.api;

import lombok.Data;

@Data
public class GenerateDifference implements Event {

	private Long releaseId;
	
	private String fileName;
	
}
