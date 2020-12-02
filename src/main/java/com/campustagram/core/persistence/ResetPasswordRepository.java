package com.campustagram.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campustagram.core.model.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
	public ResetPassword findByUserId(Long user_id);

	public ResetPassword findByHash(String hash);
}
