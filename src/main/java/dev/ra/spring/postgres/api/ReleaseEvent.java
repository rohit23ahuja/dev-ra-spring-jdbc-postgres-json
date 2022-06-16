package dev.ra.spring.postgres.api;

import org.postgresql.util.PGobject;

import lombok.Data;

@Data
public class ReleaseEvent {

	private Long id;
	
	private Long releaseId;
	
	private String eventCode;
	
    private PGobject eventData;	
	
}
