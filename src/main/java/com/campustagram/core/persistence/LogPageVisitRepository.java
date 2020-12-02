package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.LogPageVisit;

public interface LogPageVisitRepository extends JpaRepository<LogPageVisit, Long> {
	public List<LogPageVisit> findByUserId(Long user_id);

	@Query(value = "SELECT * FROM logpagevisit t ORDER BY process_date DESC", nativeQuery = true)
	public List<LogPageVisit> findAllNotDeleted();
}
