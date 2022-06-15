package dev.ra.spring.postgres.api;

import lombok.Data;

@Data
public class ReleaseEvent {

	private Long id;
	
	private Long releaseId;
	
	private String eventCode;
	
    private String eventData;	
	
}
