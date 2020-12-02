package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.UserAgent;

/**
 * created on 2018/09/28
 * 
 * @author Ahmet ŞEN
 *
 */
public interface UserAgentRepository extends JpaRepository<UserAgent, Long> {
	@Query(value = "SELECT * FROM user_agent t WHERE is_deleted = false ORDER BY id", nativeQuery = true)
	public List<UserAgent> getAllUserAgent();

	@Query(value = "SELECT * FROM user_agent t WHERE name = :name ORDER BY id", nativeQuery = true)
	public List<UserAgent> getUserAgentByName(@Param("name") String name);

	@Query(value = "SELECT * FROM public.user_agent ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
	public UserAgent getRandomUserAgent();

}
