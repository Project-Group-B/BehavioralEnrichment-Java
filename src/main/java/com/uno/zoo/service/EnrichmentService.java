package com.uno.zoo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uno.zoo.dao.DAO;
import com.uno.zoo.dto.CategoryInfo;
import com.uno.zoo.dto.CompleteRequestForm;
import com.uno.zoo.dto.DepartmentInfo;
import com.uno.zoo.dto.ItemForm;
import com.uno.zoo.dto.ItemInfo;
import com.uno.zoo.dto.SpeciesInfo;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserListInfo;
import com.uno.zoo.dto.UserLogIn;
import com.uno.zoo.dto.UserSignUp;

@Service
public class EnrichmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnrichmentService.class);
	private DAO dao;
	
	public EnrichmentService(DAO dao) {
		this.dao = dao;
	}

	/**
	 * Checks the database for the user's passed in credentials, returning the user information if the 
	 * credentials are verified (username + password is a valid user).
	 * @param user {@link UserLogIn} information
	 * @return If user credentials are verified, returns {@link UserInfo} with user information, else returns
	 * without the user information.
	 */
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
	
	/**
	 * Signs the user up to use the system. User information must be provided, including username, first
	 * name, last name, etc.
	 * @param user {@link UserSignUp} information
	 * @return {@link StandardReturnObject} with success/error message, depending on if there was an error
	 */
	public StandardReturnObject addUser(UserSignUp user) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.addUser(user);
			if(!ret.isError()) {
				LOGGER.info("user '{}' added", user.getUsername());
			} else {
				LOGGER.info("error adding user '{}': {}", user.getUsername(), ret.getErrorMsg());
			}
		} catch(Exception e) {
			LOGGER.error("Error adding user:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error add user. Please try again.");
		}
		
		return ret;
	}

	/**
	 * Adds the enrichment request information to the database.
	 * @param form {@link CompleteRequestForm} - enrichment request form information
	 * @return {@link StandardReturnObject} with success/error message depending on if there was an error
	 */
	public StandardReturnObject submitEnrichmentRequest(CompleteRequestForm form) {
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
	
	public StandardReturnObject submitNewItem(ItemForm form) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.insertNewItem(form);
		} catch(Exception e) {
			LOGGER.info("Error inserting new item into database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error inserting item (with thrown exception)");
		}
		
		return ret;
	}
	
	public StandardReturnObject removeUsers(List<UserListInfo> users) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.removeUsers(users);
			ret.setMessage("Successfully removed user(s)!");
		} catch(Exception e) {
			LOGGER.info("Error removing users from database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error removing user(s) - with thrown exception");
		}
		
		return ret;
	}
	
	public StandardReturnObject resetPasswords(List<UserListInfo> users) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.resetPasswords(users);
			ret.setMessage("Successfully reset passwords for selected users.");
		} catch(Exception e) {
			LOGGER.info("Error resetting passwords in database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error resetting passwords - with thrown exception");
		}
		
		return ret;
	}
	
	public StandardReturnObject addNewDepartment(DepartmentInfo dept) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.addNewDepartment(dept);
			ret.setMessage("Successfully added department '" + dept.getDepartmentName() + "'");
		} catch(Exception e) {
			LOGGER.info("Error adding department into database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error adding department - with thrown exception");
		}
		
		return ret;
	}
	
	public StandardReturnObject removeDepartment(int deptId) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.removeDepartment(deptId);
			ret.setMessage("Successfully removed department");
		} catch(Exception e) {
			LOGGER.info("Error removing department from database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error removing department - with thrown exception");
		}
		
		return ret;
	}
	
	/**
	 * Gets a list of all the departments in the database.
	 * @return A {@link java.util.ArrayList} of {@link DepartmentInfo}
	 */
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
	
	/**
	 * Gets a list of all item categories from the database.
	 * @return A {@link java.util.ArrayList} of {@link CategoryInfo}
	 */
	public List<CategoryInfo> getCategories() {
		List<CategoryInfo> categories = new ArrayList<>();
		
		try {
			categories = dao.getCategories();
		} catch(Exception e) {
			LOGGER.error("Error getting departments:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return categories;
	}

	/**
	 * Gets a list of all species from the database.
	 * @return A {@link java.util.ArrayList} of {@link SpeciesInfo}
	 */
	public List<SpeciesInfo> getSpecies() {
		List<SpeciesInfo> species = new ArrayList<>();
		
		try {
			species = dao.getSpecies();
		} catch(Exception e) {
			LOGGER.error("Error getting species:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return species;
	}
	
	public List<ItemInfo> getItems() {
		List<ItemInfo> items = new ArrayList<>();
		
		try {
			items = dao.getItems();
		} catch(Exception e) {
			LOGGER.error("Error getting items:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return items;
	}
	
	public List<UserListInfo> getUsers() {
		List<UserListInfo> users = new ArrayList<>();
		
		try {
			users = dao.getUsers();
		} catch(Exception e) {
			LOGGER.error("Error getting users:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return users;
	}
}
