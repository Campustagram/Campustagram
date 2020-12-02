package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.campustagram.core.model.Role;

public interface RoleRepository extends JpaRepository<Role , Long> {
	@Query(value = "SELECT * FROM role t WHERE is_deleted = false", nativeQuery = true)
	public List<Role> findAllNotDeleted();

	@Query(value = "SELECT * FROM role t WHERE is_deleted = false AND name=?1", nativeQuery = true)
	public Role findByNameNotDeleted(String roleName);
}