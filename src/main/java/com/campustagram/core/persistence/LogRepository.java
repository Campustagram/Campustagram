package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
	public List<Log> findByUserId(Long user_id);

	@Query(value = "SELECT * FROM log t ORDER BY process_date DESC", nativeQuery = true)
	public List<Log> findAllNotDeleted();
}
