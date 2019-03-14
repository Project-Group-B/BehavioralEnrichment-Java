package com.uno.zoo.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.uno.zoo.dto.CategoryInfo;
import com.uno.zoo.dto.DepartmentInfo;
import com.uno.zoo.dto.ItemForm;
import com.uno.zoo.dto.ItemInfo;
import com.uno.zoo.dto.CompleteRequestForm;
import com.uno.zoo.dto.SpeciesInfo;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserListInfo;
import com.uno.zoo.dto.UserLogIn;
import com.uno.zoo.dto.UserSignUp;
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
	
	@PostMapping(path = "loginUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserInfo login(@RequestBody UserLogIn user) {
		sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		UserInfo ret = new UserInfo();
		
		if(!usernameValid(user.getUsername())) {
			ret.setLoggedIn(false);
			ret.setUsername(user.getUsername());
			ret.setErrorMsg(USERNAME_LENGTH_ERROR_MSG);
		} else {
			ret = service.login(user);
			
			if(ret.isLoggedIn()) {
				ret.setSessionId(sessionId);
			} else {
				ret.setErrorMsg(USERNAME_PASSWORD_ERROR);
			}
		}
		
		return ret;
	}

	@PostMapping(path = "addUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject addUser(@RequestBody UserSignUp user) {
		StandardReturnObject validation = new StandardReturnObject();
		
		if(!usernameValid(user.getUsername())) {
			validation.setError(true, USERNAME_LENGTH_ERROR_MSG);
		} else {
			validation = service.addUser(user);
		}
		
		return validation;
	}
	
	@PostMapping(path = "enrichmentRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject submitEnrichmentRequest(@RequestBody CompleteRequestForm form) {
		// Override given date of submission to ensure it's set to today's date
		java.util.Date now = new java.util.Date();
		java.sql.Date nowSql = new java.sql.Date(now.getTime());
		form.setDateOfSubmission(nowSql);
		
		return service.submitEnrichmentRequest(form);
	}
	
	@PostMapping(path = "newItem", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject submitNewItem(@RequestBody ItemForm form) {
		return service.submitNewItem(form);
	}
	
	@PostMapping(path = "removeUsers", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StandardReturnObject removeUsers(@RequestBody List<UserListInfo> users) {
		return service.removeUsers(users);
	}
	
	@GetMapping(path = "departments", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DepartmentInfo> getDepartmentsFromDb() {
		return service.getDepartments();
	}

	@GetMapping(path = "categories", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CategoryInfo> getCategoriesFromDb() {
		return service.getCategories();
	}
	
	@GetMapping(path = "species", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SpeciesInfo> getSpeciesFromDb() {
		return service.getSpecies();
	}
	
	@GetMapping(path = "items", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ItemInfo> getItemsFromDb() {
		return service.getItems();
	}
	
	@GetMapping(path = "userList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserListInfo> getUserList() {
		return service.getUsers();
	}

	private boolean usernameValid(String username) {
		return !(username.length() < 1 || username.length() > 25);
	}
}
