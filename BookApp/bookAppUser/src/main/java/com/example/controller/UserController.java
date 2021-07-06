package com.example.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mail.MailMethod;
import com.example.model.PasswordReset;
import com.example.model.PasswordResetLink;
import com.example.model.User;
import com.example.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
@EnableSwagger2
public class UserController {

	@Autowired
	private UserService ls;

	private static final String content = ",\n Thank you for registering with BookHub. Now you can get all information about your favourite books and also give your reviews . ";
	private static final String subject = "Thank You";
	private static final String s1 = "Book Hub";
	private static final Long PASSWORD_RESET_EXPIRY_DURATION_MILLISECONDS = 60000000L;
	private static final String PASSWORD_RESET_SUBJECT = "Password Reset Link";
	private static final String PASSWORD_RESET_MAIL_HEADING = "<a href=\"%s/%s\">Click here to Reset Password</a>";
	private static final String PASSWORD_RESET_MAIL_CONTENT = ", \nLink to reset your password is below.\nThis link expires at %s";

	@Value("${forgot.password.base.url}")
	private String forgotPasswordaseURL;

	@PostMapping("/users")
	public ResponseEntity<?> logIn(@RequestBody User user) {
		User user1 = ls.getUserByUsername(user.getUsername());
		if (user1 == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		Boolean b = BCrypt.checkpw(user.getPassword(), user1.getPassword());
		if (b) {
			String token = Jwts.builder().setId(user1.getUsername()).setIssuedAt(new Date())
					.signWith(SignatureAlgorithm.HS256, "usersecretkey").compact();

			Map<String, Object> tokenMap = new HashMap<>();
			tokenMap.put("token", token);
			tokenMap.put("message", "User Successfully logged in");
			tokenMap.put("image", user1.getImage());
			tokenMap.put("id", user1.getId());
			tokenMap.put("name", user1.getName());
			tokenMap.put("gender", user1.getGender());
			tokenMap.put("city", user1.getCity());
			ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(tokenMap, HttpStatus.OK);

			return response;
		} else {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "/users/create")
	public ResponseEntity<User> registeruser(@RequestBody User user) {
		if (ls.getUserByUsername(user.getUsername()) == null) {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String userHashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
//			System.out.println("gender: " +user.getGender());
//			System.out.println("city: " +user.getCity());
			// System.out.println("username: " +user.getUsername());
			User resm = new User(user.getUsername(), userHashedPassword, user.getName(), user.getGender(),
					user.getCity(), user.getImage());
			User userResp = ls.registeruser(resm);
			MailMethod.sendMail(user.getUsername(), s1, subject, content, user.getName());
			return new ResponseEntity<User>(userResp, HttpStatus.OK);
		} else {
			System.out.println("email id already exist.");
			return new ResponseEntity<User>(user, HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") String id) {
		ResponseEntity<User> rs = null;
		try {
			User user = ls.getUserByUsername(id);
			rs = ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (Exception e) {
			rs = ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return rs;
	}

	@PostMapping(value = "/users/forget-password")
	public ResponseEntity<User> forgetuser(@RequestBody User user) {
		user = ls.getUserByUsername(user.getUsername());
		if (user != null) {
			Long currentTime = System.currentTimeMillis();
			PasswordResetLink passwordResetLink = ls.getPasswordResetLink(user.getId(), currentTime);
			if (passwordResetLink == null) {
				passwordResetLink = new PasswordResetLink();
				passwordResetLink.setResetLinkId(getResetLinkId(user.getId()));
				passwordResetLink.setUserId(user.getId());
				passwordResetLink.setCreatedAt(currentTime);
				passwordResetLink.setExpiresAt(currentTime + PASSWORD_RESET_EXPIRY_DURATION_MILLISECONDS);
				ls.savePasswordResetLink(passwordResetLink);
			}
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("IST"));
			calendar.setTimeInMillis(passwordResetLink.getExpiresAt());
			String resetPasswordMailHeading = String.format(PASSWORD_RESET_MAIL_HEADING, forgotPasswordaseURL,
					passwordResetLink.getResetLinkId());
			String resetPasswordMailContent = String.format(PASSWORD_RESET_MAIL_CONTENT, calendar.getTime());
			MailMethod.sendMail(user.getUsername(), resetPasswordMailHeading, PASSWORD_RESET_SUBJECT,
					resetPasswordMailContent, user.getName());
			return new ResponseEntity<User>(HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}

	private String getResetLinkId(Integer userId) {
		String passwordResetLinkId = UUID.randomUUID().toString().substring(0, 5) + System.nanoTime();
		return passwordResetLinkId;
	}

	@GetMapping(value = "/users/validate-link/{passwordResetLinkId}")
	public ResponseEntity<Integer> validatePasswordLink(
			@PathVariable(name = "passwordResetLinkId") String passwordResetLinkId) {
		Optional<PasswordResetLink> passwordResetLinkOptional = ls.getPasswordResetLink(passwordResetLinkId);
		if (!passwordResetLinkOptional.isPresent()) {
			return new ResponseEntity<Integer>(HttpStatus.NOT_FOUND);
		}
		Long currentTime = System.currentTimeMillis();
		PasswordResetLink passwordResetLink = passwordResetLinkOptional.get();
		if (passwordResetLink.getExpiresAt() <= currentTime) {
			return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Integer>(passwordResetLink.getUserId(), HttpStatus.OK);
	}

	@PostMapping(value = "/users/set-password")
	public ResponseEntity<User> resetPassword(@RequestBody PasswordReset passwordReset) {
		Optional<User> userOptional = ls.getUserByUserId(passwordReset.getUserId());
		if (!userOptional.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		User user = userOptional.get();
		String userHashedPassword = new BCryptPasswordEncoder().encode(passwordReset.getPassword());

		user.setPassword(userHashedPassword);
		ls.registeruser(user);

		ls.delePasswordResetLinkByUserId(passwordReset.getUserId());

		return new ResponseEntity<User>(HttpStatus.OK);

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Integer userId, @RequestBody User userRequest) {
		ResponseEntity<User> rs = null;
		try {
			Optional<User> userOptional = ls.getUserByUserId(userId);
			if (!userOptional.isPresent()) {
				return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
			}
			User user = userOptional.get();
			user.setName(userRequest.getName());
			user.setCity(userRequest.getCity());
			user.setGender(userRequest.getGender());
			user = ls.registeruser(user);
			rs = ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (Exception e) {
			rs = ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return rs;
	}

}
