package dev.ra.spring.postgres.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.jdbc.core.RowMapper;

import dev.ra.spring.postgres.api.ReleaseEvent;

public class ReleaseEventMapper implements RowMapper<ReleaseEvent> {

    public static final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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
        releaseEvent.setCreatedOn(rs.getTimestamp("created_on", tzUTC));
        releaseEvent.setReleaseTime(rs.getTimestamp("execution_time", tzUTC)
                .toInstant());
        return releaseEvent;
    }

}
