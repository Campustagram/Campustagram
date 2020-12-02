package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.RememberMe;

public interface RememberMeRepository extends JpaRepository<RememberMe, Long> {
	public List<RememberMe> findByUserId(Long user_id);

	@Query(value = "SELECT * FROM rememberme t WHERE t.hash = :hash AND t.hash_of_user_id = :hash_of_user_id", nativeQuery = true)
	public RememberMe findByHashAndHashId(@Param("hash") String hash, @Param("hash_of_user_id") String HashOfUserId);
}
