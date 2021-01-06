package com.campustagram.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.app.model.Image;
import com.campustagram.core.model.User;

public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query(value = "SELECT * FROM images t WHERE is_deleted=false ORDER BY date_of_update DESC", nativeQuery = true)
	public List<Image> findAllNotDeleted();
	
	@Query(value = "SELECT * FROM images t WHERE  user_id = :userId AND is_deleted=false ORDER BY date_of_update DESC", nativeQuery = true)
	public List<Image> findAllByUserIdNotDeleted(@Param("userId") Long userId);
	
	@Query(value = "SELECT * FROM users t WHERE is_deleted=false AND email = :email", nativeQuery = true)
	public User findByEmailNotDeleted(@Param("email") String email);

}
