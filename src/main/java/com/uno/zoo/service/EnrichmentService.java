package com.uno.zoo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uno.zoo.dao.DAO;
import com.uno.zoo.dto.RequestForm;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserLogin;

@Service
public class EnrichmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnrichmentService.class);
	private DAO dao;
	
	public EnrichmentService(DAO dao) {
		this.dao = dao;
	}

	public boolean login(UserLogin user) {
		boolean result = false;
		
		try {
			result = dao.login(user);
			LOGGER.info("user '{}'{} signed in.", user.getUsername(), result ? "" : " not");
		} catch(Exception e) {
			LOGGER.info("Error checking if user exists:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	public StandardReturnObject signUp(UserLogin user) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.signUp(user);
			if(!ret.isError()) {
				LOGGER.info("user '{}' signed up", user.getUsername());
			} else {
				LOGGER.info("error signing up user '{}': {}", user.getUsername(), ret.getErrorMsg());
			}
		} catch(Exception e) {
			LOGGER.error("Error signing user up:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true);
			ret.setErrorMsg(e.getMessage());
		}
		
		return ret;
	}

	public StandardReturnObject submitEnrichmentRequest(RequestForm form) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret.setMessage("Successfully entered form.");
		} catch(Exception e) {
			LOGGER.info("Error inserting enrichment request form into database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true);
			ret.setErrorMsg(e.getMessage());
		}
		
		return ret;
	}
}
