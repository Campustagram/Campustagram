package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query(value = "SELECT * FROM notification t WHERE inform_user_id = :inform_user_id ORDER BY process_date DESC", nativeQuery = true)
	public List<Notification> findAllNotificationOfUserByID(@Param("inform_user_id") Long id);

	@Query(value = "SELECT * FROM notification ORDER BY process_date DESC", nativeQuery = true)
	public List<Notification> findAllNotification();

	@Query(value = "SELECT * FROM notification t WHERE is_seen = false AND inform_user_id = :inform_user_id ORDER BY process_date DESC", nativeQuery = true)
	public List<Notification> findUserUnSeenNotificationById(@Param("inform_user_id") Long id);

	@Query(value = "SELECT COUNT(t) FROM notification t WHERE is_seen = false AND inform_user_id = :inform_user_id", nativeQuery = true)
	public int countUnSeenNotificationById(@Param("inform_user_id") Long id);

}
