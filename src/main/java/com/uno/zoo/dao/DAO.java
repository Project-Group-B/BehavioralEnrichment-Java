package com.uno.zoo.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.uno.zoo.dto.AnimalForm;
import com.uno.zoo.dto.AnimalInfo;
import com.uno.zoo.dto.CategoryInfo;
import com.uno.zoo.dto.ChangePasswordForm;
import com.uno.zoo.dto.CompleteRequestForm;
import com.uno.zoo.dto.DepartmentInfo;
import com.uno.zoo.dto.ItemForm;
import com.uno.zoo.dto.ItemInfo;
import com.uno.zoo.dto.LocationInfo;
import com.uno.zoo.dto.SpeciesInfo;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserListInfo;
import com.uno.zoo.dto.UserLogIn;
import com.uno.zoo.dto.UserSignUp;

@Component
public class DAO extends NamedParameterJdbcDaoSupport {
	private static final String LOGIN_USER_SQL = "SELECT a.User_Id, a.User_Name, a.User_Status, a.User_FirstName, a.User_LastName, b.Department_Name from user as a INNER JOIN department as b ON a.User_Department=b.Department_Id WHERE User_Name = :username AND User_Pass = sha2(:password, 256) AND User_Status != 2";
	private static final String USERNAME_EXISTS_SQL = "SELECT EXISTS (SELECT 1 FROM user WHERE User_Name = :username) AS doesExist";
	private static final String CHECK_IS_ADMIN_SQL = "SELECT User_Status from user WHERE User_Name = :username";
	private static final String ADD_USER_SQL = "INSERT INTO user (User_Name, User_Pass, User_Status, User_Department, User_FirstName, User_LastName) VALUES (:username, sha2(:password, 256), :status, :department, :firstName, :lastName)";
	private static final String GET_DEPARTMENTS_SQL = "SELECT Department_Id, Department_Name FROM department ORDER BY Department_Name asc";
	private static final String GET_CATEGORIES_SQL = "SELECT Category_Id, Category_Name, Category_Description FROM category ORDER BY Category_Name asc";
	private static final String GET_SPECIES_SQL = "SELECT Species_Id, Species_Name, Species_Description FROM species ORDER BY Species_Name ASC";
	private static final String GET_ITEMS_SQL = "SELECT Item_Id, Item_Name FROM item ORDER BY Item_Name ASC";
	private static final String GET_USERS_SQL = "SELECT a.User_Id, a.User_Name, a.User_FirstName, a.User_LastName, a.User_Status, b.Department_Name FROM user AS a INNER JOIN department AS b ON a.User_Department=b.Department_Id ORDER BY User_Id ASC";
	private static final String GET_ANIMALS_SQL = "SELECT animal.Animal_Id, animal.Animal_IsisNumber, species.Species_Name FROM animal INNER JOIN species ON animal.Animal_Species=species.Species_Id ORDER BY Animal_Id";
	private static final String GET_LOCATIONS_SQL = "SELECT Location_Id, Location_Name, Location_Description FROM location ORDER BY Location_Name ASC";
	private static final String ENRICHMENT_REQUEST_FORM_SQL = "INSERT INTO enrichment_experience "
			+ "(Enrichment_DateSubmitted, Enrichment_Name, Enrichment_Submittor, Enrichment_Department, "
			+ "Enrichment_Item, Enrichment_Species, Enrichment_Animal, Enrichment_Description, "
			+ "Enrichment_Location, Enrichment_PresentationMethod, Enrichment_TimeStart, Enrichment_TimeEnd, "
			+ "Enrichment_Frequency, Enrichment_LifeStrategies, Enrichment_PreviousUse, "
			+ "Enrichment_Contact, Enrichment_SafetyQuestions, Enrichment_RisksHazards, Enrichment_Goal, "
			+ "Enrichment_Source, Enrichment_TimeRequired, Enrichment_Construction, Enrichment_Volunteers, "
			+ "Enrichment_Inventory, Enrichment_Concerns, Enrichment_ApprovedBy, Enrichment_IsApproved) values "
			+ "(:dateSubmitted, :name, :submitter, :department, "
			+ ":item, :species, :animal, :description, "
			+ ":location, :presentationMethod, :timeStart, :timeEnd, "
			+ ":frequency, :lifeStrategies, :previousUse, "
			+ ":contact, :safetyQuestions, :risksHazards, :goal, "
			+ ":source, :timeRequired, :construction, :volunteers, "
			+ ":inventory, :concerns, :approvedBy, :isApproved)";
	private static final String INSERT_NEW_ITEM_SQL = "INSERT INTO item (Item_Name, Item_Photo, Item_ApprovalStatus, Item_Comments, Item_SafetyNotes, Item_Exceptions) VALUES (:name, :photo, :approval, :comments, :safetyNotes, :exceptions)";
	private static final String INSERT_NEW_ANIMAL_SQL = "INSERT INTO animal (Animal_IsisNumber, Animal_Species, Animal_Location, Animal_Housing, Animal_ActivityCycle, Animal_Age) VALUES (:isis, :species, :location, :housing, :activityCycle, :age)";
	private static final String DEACTIVATE_USER_SQL = "UPDATE user SET User_Status = 2 WHERE User_Id = :userId AND User_Name = :username";
	private static final String REACTIVATE_USER_SQL = "UPDATE user SET User_Status = 0 WHERE User_Id = :userId AND User_Name = :username";
	private static final String RESET_USER_PASSWORD_SQL = "UPDATE user SET User_Pass = sha2(:defaultPassword, 256) WHERE User_Id :id";
	private static final String CHANGE_PASSWORD_SQL = "UPDATE user SET User_Pass = sha2(:newPassword, 256) WHERE User_Id = :id AND User_Name = :username AND User_Pass = sha2(:oldPassword, 256)";
	private static final String ADD_NEW_DEPARTMENT_SQL = "INSERT INTO department (Department_Name) VALUES (:deptName)";
	private static final String REMOVE_DEPARTMENT_BY_ID_SQL = "DELETE FROM department WHERE Department_Id = :id";
	
	public DAO(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	/**
	 * Accesses the database and retrieves the user information from valid credentials are provided.
	 * User_Status column values are defined as follows:<br/>
	 * <strong>0</strong>: regular user (not admin), active<br/>
	 * <strong>1</strong>: admin user, active<br/>
	 * <strong>2</strong>: inactive user (will not appear in user table and user is unable to log in)
	 * @param user {@link UserLogIn} information
	 * @return {@link UserInfo} completely filled out if logged in. If not logged in, object parameters will
	 * be null except {@link UserInfo#setLoggedIn(boolean)} will be false
	 * @throws DataAccessException
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	public UserInfo login(UserLogIn user) throws DataAccessException, SQLException, NumberFormatException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", user.getUsername());
		params.addValue("password", user.getPassword());
		
		ResultSetExtractor<UserInfo> rowMapper = new ResultSetExtractor<UserInfo>() {
			@Override public UserInfo extractData(ResultSet rs) throws SQLException, NumberFormatException {
				UserInfo info = new UserInfo();
				if(rs.next()) {
					info.setLoggedIn(true);
					info.setId(Integer.parseInt(rs.getString("User_Id")));
					info.setUsername(rs.getString("User_Name"));
					info.setDepartment(rs.getString("Department_Name"));
					info.setFirstName(rs.getString("User_FirstName"));
					info.setLastName(rs.getString("User_LastName"));
					info.setAdmin(rs.getString("User_Status").equalsIgnoreCase("1"));
				} else {
					info.setLoggedIn(false);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(LOGIN_USER_SQL, params, rowMapper);
	}

	/**
	 * Accesses the database and signs up the user with the information provided.
	 * @param user {@link UserSignUp} information
	 * @return {@link StandardReturnObject} message/error message
	 * @throws DataAccessException
	 * @throws SQLException
	 * @throws Exception
	 */
	public StandardReturnObject addUser(UserSignUp user) throws DataAccessException, SQLException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		if(usernameExists(user.getUsername())) {
			retObject.setError(true, "Username taken");
		} else {
			final String defaultPassword = getUserDefaultPassword(user.getFirstName(), user.getLastName());
			params.addValue("username", user.getUsername());
			params.addValue("password", defaultPassword);
			params.addValue("status", user.getStatus());
			params.addValue("department", user.getDepartment().getDepartmentId());
			params.addValue("firstName", user.getFirstName());
			params.addValue("lastName", user.getLastName());
			
			int rowsAffected = getNamedParameterJdbcTemplate().update(ADD_USER_SQL, params);
			if(rowsAffected <= 0) {
				throw new Exception("Number rows affected when adding user was less than 1.");
			}
			
			retObject.setMessage(rowsAffected > 0 ? "User successfully added" : "Error adding user");
		}
		
		return retObject;
	}
	
	/**
	 * Accesses the database and inserts the complete item enrichment request form
	 * @param form {@link CompleteRequestForm}
	 * @return {@link StandardReturnObject} with no parameters filled out - calling function responsible for
	 * that.
	 * @throws DataAccessException
	 * @throws SQLException 
	 */
	public StandardReturnObject insertRequestForm(CompleteRequestForm form) throws DataAccessException, SQLException {
		// TODO: changed db enrichment.Enrichment_Inventory to varchar(75)
		java.util.Date now = new java.util.Date();
		java.sql.Date nowSql = new java.sql.Date(now.getTime());
		
		boolean userIsAdmin = checkIsAdmin(form.getNameOfSubmitter().getUsername());
		
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("department", form.getDepartment().getDepartmentId());
		params.addValue("species", form.getSpecies().getSpeciesId());
		params.addValue("animal", form.getAnimalId());

		params.addValue("item", form.getItemId());
		params.addValue("name", form.getEnrichmentName());
		params.addValue("description", form.getEnrichmentDescription());
		params.addValue("location", form.getEnrichmentLocation());
		params.addValue("presentationMethod", form.getEnrichmentPresentationMethod());
		params.addValue("timeStart", nowSql); // TODO: use timepicker
		params.addValue("timeEnd", nowSql); // TODO: use timepicker
		params.addValue("frequency", Integer.parseInt(form.getEnrichmentFrequency()));

		params.addValue("lifeStrategies", form.isLifeStrategiesWksht() ? 1 : 0);
		params.addValue("previousUse", form.isAnotherDeptZoo() ? 1 : 0);
		params.addValue("contact", form.isAnotherDeptZooMoreInfo());
		params.addValue("safetyQuestions", form.isSafetyQuestion() ? 1 : 0);
		params.addValue("risksHazards", form.isRisksQuestion() ? 1 : 0);
		params.addValue("concerns", form.getSafetyComment());

		params.addValue("goal", form.getNaturalBehaviors());

		params.addValue("source", StringUtils.isBlank(form.getOtherSource()) ? form.getSource() : form.getOtherSource());
		params.addValue("timeRequired", form.getTimeRequired());
		params.addValue("construction", form.getWhoConstructs());
		params.addValue("volunteers", form.isVolunteerDocentUtilized() ? 1 : 0);
		params.addValue("inventory", form.getEnrichmentCategory());
		params.addValue("submitter", form.getNameOfSubmitter().getId());
		params.addValue("dateSubmitted", form.getDateOfSubmission());

		params.addValue("approvedBy", userIsAdmin ? form.getNameOfSubmitter().getId() : 0);
		params.addValue("isApproved", userIsAdmin ? 1 : 0);
		
		int rowsAffected = getNamedParameterJdbcTemplate().update(ENRICHMENT_REQUEST_FORM_SQL, params);
		
		if(rowsAffected >= 1) {
			retObject.setMessage("Successfully inserted enrichment request!");
		} else {
			retObject.setError(true, "ERROR: 0 rows affected");
		}
		return retObject;
	}
	
	/**
	 * Accesses the database and inserts the new item.
	 * @param form {@link ItemForm} with all fields filled out
	 * @return {@link StandardReturnObject}
	 */
	public StandardReturnObject insertNewItem(ItemForm form) throws DataAccessException, Exception {
		// TODO: submit image options:
	    // https://stackoverflow.com/questions/1665730/images-in-mysql
	    // https://stackoverflow.com/questions/3014578/storing-images-in-mysql
	    // https://stackoverflow.com/questions/6472233/can-i-store-images-in-mysql
	    // https://www.quora.com/What-is-the-best-way-to-store-100-images-in-a-MySQL-database-in-this-case
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", form.getItemName());
		params.addValue("photo", form.getPhoto());
		params.addValue("approval", 2);
		params.addValue("comments", form.getComments());
		params.addValue("safetyNotes", form.getSafetyNotes());
		params.addValue("exceptions", form.getExceptions());
		
		int rowsAffected = getNamedParameterJdbcTemplate().update(INSERT_NEW_ITEM_SQL, params);
		if(rowsAffected <= 0) {
			throw new Exception("Number rows affected when inserting new item was less than 1.");
		}
		
		retObject.setMessage(rowsAffected > 0 ? "Item successfully entered!" : "Error entering item");
		return retObject;
	}
	
	public StandardReturnObject insertNewAnimal(AnimalForm form) throws Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("isis", form.getIsisNumber());
		params.addValue("species", form.getSpecies());
		params.addValue("location", form.getLocation());
		params.addValue("housing", form.getHoused());
		params.addValue("activityCycle", form.getActivityCycle());
		params.addValue("age", form.getAge());
		
		int rowsAffected = getNamedParameterJdbcTemplate().update(INSERT_NEW_ANIMAL_SQL, params);
		if(rowsAffected <= 0) {
			throw new Exception("Number rows affected when inserting new animal was less than 1.");
		}
		
		retObject.setMessage(rowsAffected > 0 ? "Animal successfully entered!" : "ERROR: 0 rows affected");
		return retObject;
	}
	
	public StandardReturnObject deactivateUsers(List<UserListInfo> users) throws DataAccessException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(users.toArray());
		int[] rowsAffected = getNamedParameterJdbcTemplate().batchUpdate(DEACTIVATE_USER_SQL, batchArgs);
		
		for(int i : rowsAffected) {
			if(i == 0) {
				throw new Exception("Number rows affected when removing users was less than 1");
			}
		}
		return retObject;
	}
	
	public StandardReturnObject reactivateUsers(List<UserListInfo> users) throws Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(users.toArray());
		int[] rowsAffected = getNamedParameterJdbcTemplate().batchUpdate(REACTIVATE_USER_SQL, batchArgs);
		
		for(int i : rowsAffected) {
			if(i == 0) {
				throw new Exception("Number rows affected when reactivating users was less than 1");
			}
		}
		return retObject;
	}
	
	public StandardReturnObject resetPasswords(List<UserListInfo> users) throws DataAccessException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		for(UserListInfo singleUser : users) {
			params.addValue("id", singleUser.getUserId());
			params.addValue("defaultPassword", getUserDefaultPassword(singleUser.getFirstName(), singleUser.getLastName()));
			int rowsAffected = getNamedParameterJdbcTemplate().update(RESET_USER_PASSWORD_SQL, params);
			if(rowsAffected <= 0) {
				retObject.setError(true, "Unable to reset password for user " + singleUser.getUsername());
			}
		}

		return retObject;
	}
	
	public StandardReturnObject changePassword(ChangePasswordForm form) throws DataAccessException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", form.getUserId());
		params.addValue("username", form.getUserName());
		params.addValue("oldPassword", form.getOldPassword());
		params.addValue("newPassword", form.getNewPassword());
		
		int rowsAffected = getNamedParameterJdbcTemplate().update(CHANGE_PASSWORD_SQL, params);
		
		if(rowsAffected <= 0) {
			retObject.setError(true, "Unable to change password");
		}
		return retObject;
	}
	
	public StandardReturnObject addNewDepartment(DepartmentInfo dept) throws DataAccessException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("deptName", dept.getDepartmentName());
		
		int rowsAffected = getNamedParameterJdbcTemplate().update(ADD_NEW_DEPARTMENT_SQL, params);
		
		if(rowsAffected <= 0) {
			throw new Exception("Number rows affected when inserting new department was less than 1.");
		}
		return retObject;
	}
	
	public StandardReturnObject removeDepartment(int deptId) throws DataAccessException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", deptId);
		
		int rowsAffected = getNamedParameterJdbcTemplate().update(REMOVE_DEPARTMENT_BY_ID_SQL, params);
		
		if(rowsAffected <= 0) {
			throw new Exception("Number rows affected when removing department was less than 1.");
		}
		return retObject;
	}
	
	/**
	 * Accesses the database to retrieve all departments.
	 * @return A {@link java.util.ArrayList} of {@link DepartmentInfo}
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<DepartmentInfo> getDepartments() throws NumberFormatException, SQLException, DataAccessException {
		ResultSetExtractor<List<DepartmentInfo>> rowMapper = new ResultSetExtractor<List<DepartmentInfo>>() {
			@Override public List<DepartmentInfo> extractData(ResultSet rs) throws SQLException {
				List<DepartmentInfo> info = new ArrayList<>();
				while(rs.next()) {
					DepartmentInfo newDept = new DepartmentInfo();
					newDept.setDepartmentId(Integer.parseInt(rs.getString("Department_Id")));
					newDept.setDepartmentName(rs.getString("Department_Name"));
					info.add(newDept);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_DEPARTMENTS_SQL, rowMapper);
	}
	
	/**
	 * Accesses the database to retrieve all categories.
	 * @return A {@link java.util.ArrayList} of {@link CategoryInfo}
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<CategoryInfo> getCategories() throws NumberFormatException, SQLException, DataAccessException {
		ResultSetExtractor<List<CategoryInfo>> rowMapper = new ResultSetExtractor<List<CategoryInfo>>() {
			@Override public List<CategoryInfo> extractData(ResultSet rs) throws SQLException, NumberFormatException {
				List<CategoryInfo> info = new ArrayList<>();
				while(rs.next()) {
					CategoryInfo newCat = new CategoryInfo();
					newCat.setCategoryId(Integer.parseInt(rs.getString("Category_Id")));
					newCat.setCategoryName(rs.getString("Category_Name"));
					newCat.setCategoryDescription(rs.getString("Category_Description"));
					info.add(newCat);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_CATEGORIES_SQL, rowMapper);
	}
	
	/**
	 * Accesses the database to retrieve all species' info.
	 * @return A {@link java.util.ArrayList} of {@link SpeciesInfo}
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws DataAccessException
	 */
	public List<SpeciesInfo> getSpecies() throws NumberFormatException, SQLException, DataAccessException {
		ResultSetExtractor<List<SpeciesInfo>> rowMapper = new ResultSetExtractor<List<SpeciesInfo>>() {
			@Override public List<SpeciesInfo> extractData(ResultSet rs) throws SQLException, NumberFormatException {
				List<SpeciesInfo> info = new ArrayList<>();
				while(rs.next()) {
					SpeciesInfo newSpecies = new SpeciesInfo();
					newSpecies.setSpeciesId(Integer.parseInt(rs.getString("Species_Id")));
					newSpecies.setSpeciesName(rs.getString("Species_Name"));
					newSpecies.setSpeciesDescription(rs.getString("Species_Description"));
					info.add(newSpecies);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_SPECIES_SQL, rowMapper);
	}
	
	/**
	 * Accesses the database to retrieve all items' ids and names.
	 * @return A {@link java.util.ArrayList} of {@link ItemInfo}
	 * @throws NumberFormatException When converting item id from String to int
	 * @throws SQLException When traversing the returned rows
	 * @throws DataAccessException When running the SQL on the database
	 */
	public List<ItemInfo> getItems() throws NumberFormatException, SQLException, DataAccessException {
		ResultSetExtractor<List<ItemInfo>> rowMapper = new ResultSetExtractor<List<ItemInfo>>() {
			@Override public List<ItemInfo> extractData(ResultSet rs) throws SQLException, NumberFormatException {
				List<ItemInfo> info = new ArrayList<>();
				while(rs.next()) {
					ItemInfo newItem = new ItemInfo();
					newItem.setId(Integer.parseInt(rs.getString("Item_Id")));
					newItem.setName(rs.getString("Item_Name"));
					info.add(newItem);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_ITEMS_SQL, rowMapper);
	}
	
	public List<UserListInfo> getUsers() throws SQLException, DataAccessException {
		ResultSetExtractor<List<UserListInfo>> rowMapper = new ResultSetExtractor<List<UserListInfo>>() {
			@Override public List<UserListInfo> extractData(ResultSet rs) throws SQLException {
				List<UserListInfo> info = new ArrayList<>();
				while(rs.next()) {
					UserListInfo newUser = new UserListInfo();
					newUser.setUserId(rs.getString("User_Id"));
					newUser.setUsername(rs.getString("User_Name"));
					newUser.setFirstName(rs.getString("User_FirstName"));
					newUser.setLastName(rs.getString("User_LastName"));
					newUser.setDepartment(rs.getString("Department_Name"));
					newUser.setUserStatus(rs.getString("User_Status"));
					info.add(newUser);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_USERS_SQL, rowMapper);
	}
	
	public List<AnimalInfo> getAnimals() throws SQLException, DataAccessException, NumberFormatException {
		ResultSetExtractor<List<AnimalInfo>> rowMapper = new ResultSetExtractor<List<AnimalInfo>>() {
			@Override public List<AnimalInfo> extractData(ResultSet rs) throws SQLException {
				List<AnimalInfo> info = new ArrayList<>();
				while(rs.next()) {
					AnimalInfo newAnimal = new AnimalInfo();
					newAnimal.setId(Integer.parseInt(rs.getString("Animal_Id")));
					newAnimal.setIsisNumber(Integer.parseInt(rs.getString("Animal_IsisNumber")));
					newAnimal.setSpecies(rs.getString("Species_Name"));
					info.add(newAnimal);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_ANIMALS_SQL, rowMapper);
	}
	
	public List<LocationInfo> getLocations() {
		ResultSetExtractor<List<LocationInfo>> rowMapper = new ResultSetExtractor<List<LocationInfo>>() {
			@Override public List<LocationInfo> extractData(ResultSet rs) throws SQLException {
				List<LocationInfo> info = new ArrayList<>();
				while(rs.next()) {
					LocationInfo newLocation = new LocationInfo();
					newLocation.setId(Integer.parseInt(rs.getString("Location_Id")));
					newLocation.setName(rs.getString("Location_Name"));
					newLocation.setDescription(rs.getString("Location_Description"));
					info.add(newLocation);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_LOCATIONS_SQL, rowMapper);
	}
	
	/**
	 * Saves the MultipartFile to the file system at the path specified, named according to the file name
	 * passed in.<br/>
	 * Example: The file with name "image1.jpg" will be saved to D:/test/abc.jpg if the paramaters are ("D:/test/", "abc.jpg", image1.jpg).
	 * @param filesystemPath The complete path (with trailing "/") to save the file to
	 * @param fileName The name the given file will be saved to the file system as
	 * @param fileToSave The {@link MultipartFile} to save
	 * @throws IOException If there's a problem getting the byte array of the file or writing the file to the file system.
	 */
	public void saveFileToFileSystem(String filesystemPath, String fileName, MultipartFile fileToSave) throws IOException {
		byte[] bytes = fileToSave.getBytes();
		String extension = FilenameUtils.getExtension(fileToSave.getOriginalFilename());
        Path path = Paths.get(filesystemPath + fileName + "." + extension);
        Files.write(path, bytes);
	}
	
	/**
	 * Checks if the given username belongs to an admin user.
	 * @param userName The string username of the user to check.
	 * @return {@code true} if the username is an admin's username<br/> {@code false} otherwise
	 * @throws SQLException
	 */
	private boolean checkIsAdmin(String userName) throws SQLException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", userName);
		
		ResultSetExtractor<Boolean> existsRowMapper = new ResultSetExtractor<Boolean>() {
			@Override public Boolean extractData(ResultSet rs) throws SQLException {
				if(rs.next()) {
					return Boolean.valueOf(rs.getBoolean("User_Status"));
				} else {
					return Boolean.FALSE;
				}
			}
		};
		
		return getNamedParameterJdbcTemplate().query(CHECK_IS_ADMIN_SQL, params, existsRowMapper).booleanValue();
	}
	
	/**
	 * Accesses the database and returns whether the given username is taken by another user.
	 * @param username String username to check the existence of in the database.
	 * @return {@code true} if the username exists in the database, {@code false} otherwise
	 */
	private boolean usernameExists(String username) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", username);
		
		ResultSetExtractor<Boolean> existsRowMapper = new ResultSetExtractor<Boolean>() {
			@Override public Boolean extractData(ResultSet rs) throws SQLException {
				if(rs.next()) {
					return Boolean.valueOf(rs.getBoolean("doesExist"));
				} else {
					return Boolean.FALSE;
				}
			}
		};
		
		return getNamedParameterJdbcTemplate().query(USERNAME_EXISTS_SQL, params, existsRowMapper).booleanValue();
	}
	
	/**
	 * Returns a default password in the format of 
	 * {@literal <lowercase first letter of first name><lowercase first 5 letters of last name>}.
	 * Function {@link String#toLowerCase()} is used on both first and last names before getting the characters.
	 * <br>Example: "John Doe" -> "jdoe"
	 * <br>Example: "Keanu Reeves" -> "kreeve"
	 * @param firstName User first name
	 * @param lastName User last name
	 * @return User default password as a string of length 6
	 */
	private String getUserDefaultPassword(String firstName, String lastName) {
		StringBuilder builder = new StringBuilder();
		builder.append(StringUtils.left(firstName.toLowerCase(), 1));
		builder.append(StringUtils.left(lastName.toLowerCase(), 5));
		
		return builder.toString();
	}
}
