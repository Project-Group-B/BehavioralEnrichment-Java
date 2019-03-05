package com.uno.zoo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uno.zoo.dao.DAO;
import com.uno.zoo.dto.DepartmentInfo;
import com.uno.zoo.dto.RequestForm;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserLogIn;
import com.uno.zoo.dto.UserSignUp;

@Service
public class EnrichmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnrichmentService.class);
	private DAO dao;
	
	public EnrichmentService(DAO dao) {
		this.dao = dao;
	}

	public UserInfo login(UserLogIn user) {
		UserInfo result = new UserInfo();
		
		try {
			result = dao.login(user);
			LOGGER.info("user '{}'{} signed in.", user.getUsername(), result.isLoggedIn() ? "" : " not");
		} catch(Exception e) {
			LOGGER.info("Error checking if user exists:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return result;
	}
	
	public StandardReturnObject signUp(UserSignUp user) {
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
			ret.setError(true, "Error signing up. Please try again.");
		}
		
		return ret;
	}
	
	public List<DepartmentInfo> getDepartments() {
		List<DepartmentInfo> departments = new ArrayList<>();
		
		try {
			departments = dao.getDepartments();
		} catch(Exception e) {
			LOGGER.error("Error getting departments:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return departments;
	}

	public StandardReturnObject submitEnrichmentRequest(RequestForm form) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.insertRequestForm(form);
		} catch(Exception e) {
			LOGGER.info("Error inserting enrichment request form into database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, e.getMessage());
		}
		
		return ret;
	}
}
