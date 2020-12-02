package com.campustagram.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.SystemProperties;

public interface SystemPropertiesRepository extends JpaRepository<SystemProperties, Long> {
	@Query(value = "SELECT * FROM systemproperties t LIMIT 1 ", nativeQuery = true)
	public SystemProperties getSystemProperties();
}
