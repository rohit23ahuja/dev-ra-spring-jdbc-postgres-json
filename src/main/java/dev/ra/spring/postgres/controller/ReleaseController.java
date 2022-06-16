package dev.ra.spring.postgres.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.ra.spring.postgres.api.DeployDifference;
import dev.ra.spring.postgres.api.GenerateDifference;
import dev.ra.spring.postgres.api.ReleaseEvent;
import dev.ra.spring.postgres.mapper.ReleaseEventMapper;
import dev.ra.spring.postgres.util.CommonUtil;

@RestController
public class ReleaseController {

	private final JdbcTemplate jdbcTemplate;
	
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public ReleaseController(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@PostMapping("/difference/")
	public ResponseEntity<String> saveDifference(@RequestBody GenerateDifference generateDifference) {
		final String createQuery = "insert into release_event(release_id, event_code, event_data) values(?, ?, to_json(?)) ";
		final Object[] queryParams = {generateDifference.getReleaseId(), "GENERATE_DIFFERENCE", generateDifference};
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < queryParams.length; i++) {
                    ps.setObject(i + 1, queryParams[i]);
                }
                return ps;
            }
        }, holder);
        long id;
        if (holder.getKeys().size() > 1) {
        	id = (Long) holder.getKeys().get("id");
		} else {
	        id = holder.getKey().longValue();
		}

		return ResponseEntity.ok(String.format("%d id inserted", id));
	}

	@PostMapping("/deploy/")
	public ResponseEntity<String> saveDeploy(@RequestBody DeployDifference deployDifference) throws SQLException {
		String sql = "INSERT INTO release_event(release_id, event_code, event_data) VALUES(:releaseId, :eventCode, cast(:eventData AS JSON)) RETURNING id";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("releaseId", deployDifference.getReleaseId());
        parameters.put("eventCode", "DEPLOY_DIFFERENCE");
        parameters.put("eventData", CommonUtil.convertObjectToJsonString(deployDifference));
        Long id = namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
		return ResponseEntity.ok(String.format("%d id inserted", id));
	}
	
	@GetMapping("/events/{releaseId}")
	public ResponseEntity<List<ReleaseEvent>> getEvents(@PathVariable Long releaseId){
		String sql = "select * from release_event where release_id = :releaseId";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("releaseId", releaseId);
        List<ReleaseEvent> releaseEvents = namedParameterJdbcTemplate.query(sql, params, ReleaseEventMapper.getMAPPER());
		return ResponseEntity.ok(releaseEvents);
	}

}
