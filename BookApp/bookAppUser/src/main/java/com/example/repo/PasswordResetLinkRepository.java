package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.PasswordResetLink;

public interface PasswordResetLinkRepository extends JpaRepository<PasswordResetLink, String> {

	PasswordResetLink findByUserIdAndExpiresAtGreaterThan(Integer userId, Long currentTime);
	
	void deletePasswordResetLinkByUserId(Integer userId);

}
