package com.uno.zoo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.uno.zoo.dto.RequestForm;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserLogin;
import com.uno.zoo.service.EnrichmentService;

@RestController
@CrossOrigin
public class EnrichmentController {
	private EnrichmentService service;
	private static String sessionId;
	private static final String USERNAME_LENGTH_ERROR_MSG = "Username must be between 1 and 25 characters.";
	private static final String USERNAME_PASSWORD_ERROR = "Username or password invalid.";
	
	public EnrichmentController(EnrichmentService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/loginUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserInfo login(@RequestBody UserLogin user) {
		sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		UserInfo ret = new UserInfo();
		
		if(!usernameValid(user.getUsername())) {
			ret.setLoggedIn(false);
			ret.setUsername(user.getUsername());
			ret.setErrorMsg(USERNAME_LENGTH_ERROR_MSG);
		} else {
			ret.setLoggedIn(service.login(user));
			ret.setUsername(user.getUsername());
			
			if(ret.isLoggedIn()) {
				// TODO: change to take attributes from the database
				ret.setSessionId(sessionId);
				ret.setAdmin(true);
			} else {
				ret.setErrorMsg(USERNAME_PASSWORD_ERROR);
			}
		}
		
		return ret;
	}

	@PostMapping(path = "/signUpUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject signUp(@RequestBody UserLogin user) {
		StandardReturnObject validation = new StandardReturnObject();
		
		if(!usernameValid(user.getUsername())) {
			validation.setError(true, USERNAME_LENGTH_ERROR_MSG);
		} else {
			validation = service.signUp(user);
		}
		
		return validation;
	}
	
	@PostMapping(path = "/enrichmentRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject submitEnrichmentRequest(@RequestBody RequestForm form) {
		// Override given date of submission to ensure it's set to today's date
		java.util.Date now = new java.util.Date();
		java.sql.Date nowSql = new java.sql.Date(now.getTime());
		form.setDateOfSubmission(nowSql);
		
		return service.submitEnrichmentRequest(form);
	}
	
	private boolean usernameValid(String username) {
		return !(username.length() < 1 || username.length() > 25);
	}
}
