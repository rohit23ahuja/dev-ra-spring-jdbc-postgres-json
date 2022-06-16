package dev.ra.spring.postgres.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dev.ra.spring.postgres.api.ReleaseEvent;

public class ReleaseEventMapper implements RowMapper<ReleaseEvent> {

	private final static ReleaseEventMapper MAPPER = new ReleaseEventMapper();
	
	private ReleaseEventMapper() {
		super();
	}
	
	public static ReleaseEventMapper getMAPPER() {
		return MAPPER;
	}
	
	@Override
	public ReleaseEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReleaseEvent releaseEvent = new ReleaseEvent();
		releaseEvent.setId(rs.getLong("id"));
		releaseEvent.setReleaseId(rs.getLong("release_id"));
		releaseEvent.setEventCode(rs.getString("event_code"));
		releaseEvent.setEventData(rs.getString("event_data"));
		return releaseEvent;
	}

}
