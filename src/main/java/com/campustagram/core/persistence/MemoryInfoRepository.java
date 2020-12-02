package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.MemoryInfo;

public interface MemoryInfoRepository extends JpaRepository<MemoryInfo, Long> {
	@Query(value = "SELECT * FROM memory_info t ORDER BY create_date DESC", nativeQuery = true)
	public List<MemoryInfo> findAllNotDeleted();
}
