package com.uno.zoo.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(EnrichmentController.class);
	private EnrichmentService service;
	private static String sessionId;
	
	public EnrichmentController(EnrichmentService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/loginUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserInfo login(@Valid @RequestBody UserLogin user, BindingResult bindingResult) {
		sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		UserInfo ret = new UserInfo();
		
		if(bindingResult.hasErrors()) {
			ret.setLoggedIn(false);
			printBindingResultErrors(bindingResult);
		} else {
			ret.setLoggedIn(service.login(user));
			ret.setUsername(user.getUsername());
			
			if(ret.isLoggedIn()) {
				ret.setSessionId(sessionId);
				ret.setAdmin(false);
			}			
		}
		
		
		return ret;
	}

	@PostMapping(path = "/signUpUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject signUp(@RequestBody @Valid UserLogin user, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			printBindingResultErrors(bindingResult);
			StandardReturnObject ret = new StandardReturnObject();
			ret.setError(true);
			ret.setErrorMsg("Error validating username/password fields.");
			return ret;
		}
		return service.signUp(user);
	}
	
	@PostMapping(path = "/enrichmentRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject submitEnrichmentRequest(@RequestBody RequestForm form) {
		// TODO: auto-generate/override dateOfSubmission to current date
		return service.submitEnrichmentRequest(form);
	}
	
	private void printBindingResultErrors(BindingResult bindingResult) {
		LOGGER.info("Object fields not valid:");
		for(FieldError error : bindingResult.getFieldErrors()) {
			LOGGER.info(error.getField() + ": " + error.getDefaultMessage());
		}
	}
}
