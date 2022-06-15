package dev.ra.spring.postgres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.ra.spring.postgres.api.DeployDifference;
import dev.ra.spring.postgres.api.GenerateDifference;
import dev.ra.spring.postgres.api.ReleaseEvent;
import dev.ra.spring.postgres.util.CommonUtil;

@RestController
public class ReleaseController {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public ReleaseController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostMapping("/difference/")
	public ResponseEntity<String> saveDifference(@RequestBody GenerateDifference generateDifference) {
		int insertCount = jdbcTemplate.update(
				"insert into release_event(release_id, event_code, event_data) values(?, ?, to_json(?)) ",
				generateDifference.getReleaseId(), "GENERATE_DIFFERENCE",
				CommonUtil.convertObjectToJsonString(generateDifference));
		return ResponseEntity.ok(String.format("%d row inserted", insertCount));
	}

	@PostMapping("/deploy/")
	public ResponseEntity<String> saveDeploy(@RequestBody DeployDifference deployDifference) {
		int insertCount = jdbcTemplate.update(
				"insert into release_event(release_id, event_code, event_data) values(?, ?, to_json(?)) ",
				deployDifference.getReleaseId(), "DEPLOY_DIFFERENCE",
				CommonUtil.convertObjectToJsonString(deployDifference));
		return ResponseEntity.ok(String.format("%d row inserted", insertCount));
	}
	
	@GetMapping("/events/{releaseId}")
	public ResponseEntity<List<ReleaseEvent>> getEvents(@PathVariable Long releaseId){
		String sql = "select * from release_event where release_id = ?";
		List<ReleaseEvent> releaseEvents = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ReleaseEvent.class), releaseId);
		return ResponseEntity.ok(releaseEvents);
	}

}
