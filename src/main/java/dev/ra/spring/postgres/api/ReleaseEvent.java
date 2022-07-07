package dev.ra.spring.postgres.api;

import java.sql.Timestamp;
import java.time.Instant;

import lombok.Data;

@Data
public class ReleaseEvent {

    private Long id;

    private Long releaseId;

    private String eventCode;

    private String eventData;

    private Timestamp createdOn;

    private Instant releaseTime;
}
