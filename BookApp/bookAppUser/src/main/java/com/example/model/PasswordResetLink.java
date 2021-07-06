package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "password_reset_link")
public class PasswordResetLink {
	
	@Id
	private String resetLinkId;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "created_at")
	private Long createdAt;
	
	@Column(name = "expires_at")
	private Long expiresAt;

	public String getResetLinkId() {
		return resetLinkId;
	}

	public void setResetLinkId(String resetLinkId) {
		this.resetLinkId = resetLinkId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Long expiresAt) {
		this.expiresAt = expiresAt;
	}

}
