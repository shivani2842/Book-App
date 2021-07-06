package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.PasswordResetLink;
import com.example.model.User;
import com.example.repo.PasswordResetLinkRepository;
import com.example.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordResetLinkRepository passwordResetLinkRepository;

	@Override
	public User registeruser(User re) {
		return repo.save(re);
	}

	@Override
	public List<User> login(String username, String password) {
		List<User> list = null;
		try {
			list = repo.findByUsernameAndPassword(username, password);
		} catch (Exception e) {
			logger.error("error while login");
		}
		return list;
	}

	@Override
	public User getUserByUsername(String username) {
		return repo.findByUsername(username);
	}

	@Override
	public Optional<User> getUserByUserId(Integer userId) {
		return repo.findById(userId);
	}

	@Override
	public PasswordResetLink getPasswordResetLink(Integer userId, Long currentTime) {
		return passwordResetLinkRepository.findByUserIdAndExpiresAtGreaterThan(userId, currentTime);
	}

	@Override
	public Optional<PasswordResetLink> getPasswordResetLink(String passwordResetLinkId) {
		return passwordResetLinkRepository.findById(passwordResetLinkId);
	}

	@Override
	public PasswordResetLink savePasswordResetLink(PasswordResetLink passwordResetLink) {
		return passwordResetLinkRepository.save(passwordResetLink);
	}

	@Transactional
	@Override
	public void delePasswordResetLinkByUserId(Integer userId) {
		passwordResetLinkRepository.deletePasswordResetLinkByUserId(userId);

	}

}
