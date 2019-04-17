package com.uno.zoo.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uno.zoo.dao.DAO;
import com.uno.zoo.dto.AnimalForm;
import com.uno.zoo.dto.AnimalInfo;
import com.uno.zoo.dto.CategoryInfo;
import com.uno.zoo.dto.ChangePasswordForm;
import com.uno.zoo.dto.CompleteRequestForm;
import com.uno.zoo.dto.DepartmentInfo;
import com.uno.zoo.dto.EditUserInfo;
import com.uno.zoo.dto.ImageInfo;
import com.uno.zoo.dto.ItemForm;
import com.uno.zoo.dto.ItemInfo;
import com.uno.zoo.dto.LocationInfo;
import com.uno.zoo.dto.SpeciesInfo;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserListInfo;
import com.uno.zoo.dto.UserLogIn;
import com.uno.zoo.dto.UserSignUp;

@Service
public class EnrichmentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnrichmentService.class);
	/* //UNO//Spring 2019//Capstone//Project//Homepage_Image// */
	private static final String HOMEPAGE_IMAGE_FOLDER_FIRST_PART = "D:";
	private static final String HOMEPAGE_IMAGE_FILE_NAME = "homepage_image.jpg";
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
			LOGGER.info("Enrichment request successfully submitted.");
		} catch(Exception e) {
			LOGGER.info("Error inserting enrichment request form into database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "ERROR: Insertion failed - exception generated");
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
	
	public StandardReturnObject submitNewAnimal(AnimalForm form) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.insertNewAnimal(form);
		} catch(Exception e) {
			LOGGER.info("Error inserting new animal into database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "ERROR: exception when inserting animal");
		}
		
		return ret;
	}
	
	public StandardReturnObject deactivateUsers(List<UserListInfo> users) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.deactivateUsers(users);
			ret.setMessage("Successfully deactivated user(s)!");
		} catch(Exception e) {
			LOGGER.info("Error deactivating users in database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "ERROR: exception encountered when deactivating user(s)");
		}
		
		return ret;
	}
	
	public StandardReturnObject reactivateUsers(List<UserListInfo> users) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.reactivateUsers(users);
			ret.setMessage("Successfully reactivated user(s)!");
		} catch(Exception e) {
			LOGGER.info("Error reactivating users in database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "ERROR: exception encountered when reactivating user(s)");
		}
		
		return ret;
	}
	
	public StandardReturnObject editUser(EditUserInfo user) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.editUser(user);
			ret.setMessage("Successfully updated user info!");
			LOGGER.info("Successfully updated user with id '{}'", user.getUserId());
		} catch(Exception e) {
			LOGGER.info("Error updating user in database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "ERROR: exception encountered when updating user info");
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
	
	public StandardReturnObject changePassword(ChangePasswordForm form) {
		StandardReturnObject ret = new StandardReturnObject();
		
		try {
			ret = dao.changePassword(form);
			ret.setMessage("Successfully changed password.");
		} catch(Exception e) {
			LOGGER.info("Error changing password in database:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "Error changing password - with thrown exception");
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

	public List<AnimalInfo> getAnimals() {
		List<AnimalInfo> animals = new ArrayList<>();
		
		try {
			animals = dao.getAnimals();
		} catch(Exception e) {
			LOGGER.error("Error getting animals:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return animals;
	}

	public List<LocationInfo> getLocations() {
		List<LocationInfo> locations = new ArrayList<>();
		
		try {
			locations = dao.getLocations();
		} catch(Exception e) {
			LOGGER.error("Error getting locations:");
			LOGGER.error(e.getMessage(), e);
		}
		
		return locations;
	}

	public StandardReturnObject changeHomepageImage(MultipartFile image) {
		StandardReturnObject ret = new StandardReturnObject();
		
		// Get the file and save it somewhere
		try {
	        dao.saveFileToFileSystem(HOMEPAGE_IMAGE_FOLDER_FIRST_PART, HOMEPAGE_IMAGE_FILE_NAME, image);
	        ret.setMessage("Successfully uploaded image.");
		} catch (Exception e) {
			LOGGER.error("Error uploading file:");
			LOGGER.error(e.getMessage(), e);
			ret.setError(true, "ERROR: exception encountered when uploading image");
		}
		
		return ret;
	}

	public ImageInfo getHomepageImage() {
		Path filePath = Paths.get(
				HOMEPAGE_IMAGE_FOLDER_FIRST_PART,
				HOMEPAGE_IMAGE_FILE_NAME);
		ImageInfo ret = new ImageInfo();
        try {
        	ret.setBase64EncodedImage(dao.getImageFromFileSystemAsBase64String(filePath));
		} catch (Exception e) {
			LOGGER.error("Error getting homepage image from file system:");
			LOGGER.error(e.getMessage(), e);
		}
        
        return ret;
	}
}
