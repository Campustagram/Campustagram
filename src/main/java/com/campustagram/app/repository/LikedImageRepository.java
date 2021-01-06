package com.campustagram.app.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.app.model.Image;
import com.campustagram.app.model.LikedImage;
import com.campustagram.core.model.User;

public interface LikedImageRepository extends JpaRepository<LikedImage, Long> {

	@Query(value = "SELECT DISTINCT * FROM liked_image t WHERE user_id = :userId and image_id = :imageId", nativeQuery = true)
	public LikedImage findByUserIdAndImageId(@Param("userId") Long userId, @Param("imageId") Long imageId);
	
	@Query(value = "SELECT * FROM liked_image t WHERE user_id = :userId ", nativeQuery = true)
	public Set<LikedImage> findAllByUserId(@Param("userId") Long userId);
	
	@Query(value = "SELECT * FROM liked_image t WHERE image_id = :imageId", nativeQuery = true)
	public Set<LikedImage> findAllByImageId( @Param("imageId") Long imageId);
	
}
