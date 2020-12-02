package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.DiscInfo;

public interface DiscInfoRepository extends JpaRepository<DiscInfo, Long> {
	@Query(value = "SELECT * FROM disc_info t ORDER BY create_date DESC", nativeQuery = true)
	public List<DiscInfo> findAllNotDeleted();
}
