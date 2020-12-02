package com.campustagram.core.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.campustagram.core.model.Language;
import com.campustagram.core.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public List<User> findByEmail(String email);

	@Query(value = "SELECT * FROM users t WHERE is_deleted=false AND email = :email", nativeQuery = true)
	public User findByEmailNotDeleted(@Param("email") String email);

	@Query(value = "SELECT * FROM users t WHERE is_deleted=false ORDER BY name, email", nativeQuery = true)
	public List<User> findAllNotDeleted();

	@Query(value = "SELECT COUNT(t) FROM users t WHERE is_deleted=false ", nativeQuery = true)
	public int countAllNotDeleted();

	@Query(value = "SELECT COUNT(t) FROM users t WHERE is_deleted=false AND is_online=true AND  last_seen > CURRENT_TIMESTAMP - INTERVAL '5 minutes'", nativeQuery = true)
	public int countAllOnline();

	@Query(value = "SELECT * FROM users t WHERE is_deleted=false AND is_online=true AND  last_seen > CURRENT_TIMESTAMP - INTERVAL '5 minutes'", nativeQuery = true)
	public List<User> allOnlineUsers();

	@Query(value = "SELECT * FROM users t WHERE is_deleted=false AND id = :id", nativeQuery = true)
	public User findByIdNotDeleted(@Param("id") Long id);

	@Modifying
	@Query("update User u set u.lastSeen = :last_seen where u.id = :id and isDeleted=false")
	@Transactional
	int updateUserLastSeen(@Param("id") Long id, @Param("last_seen") Date lastSeen);

	@Modifying
	@Query("update User u set u.language = :language where u.id = :id and isDeleted=false")
	@Transactional
	int updateUserLanguage(@Param("id") Long id, @Param("language") Language language);

	@Modifying
	@Query("update User u set u.isOnline = :isOnline where u.id = :id and isDeleted=false")
	@Transactional
	int updateUserOnlineStatus(@Param("id") Long id, @Param("isOnline") boolean isOnline);
}
