package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.CpuInfo;

public interface CpuInfoRepository extends JpaRepository<CpuInfo, Long> {
	@Query(value = "SELECT * FROM cpu_info t ORDER BY create_date DESC", nativeQuery = true)
	public List<CpuInfo> findAllNotDeleted();
}
