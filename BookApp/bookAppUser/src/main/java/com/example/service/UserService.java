package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.model.PasswordResetLink;
import com.example.model.User;

public interface UserService {
	public List<User> login(String username, String password);

	public User registeruser(User re);

	public User getUserByUsername(String username);

	public Optional<User> getUserByUserId(Integer userId);

	public PasswordResetLink getPasswordResetLink(Integer userId, Long currentTime);

	public PasswordResetLink savePasswordResetLink(PasswordResetLink passwordResetLink);

	public Optional<PasswordResetLink> getPasswordResetLink(String passwordResetLinkId);
	
	public void delePasswordResetLinkByUserId(Integer userId);

}