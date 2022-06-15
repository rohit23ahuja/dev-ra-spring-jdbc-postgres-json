package dev.ra.spring.postgres.api;

import lombok.Data;

@Data
public class DeployDifference {
	
	private Long releaseId;
	
	private String fileName;
	
	private String ticketNumber;

}
