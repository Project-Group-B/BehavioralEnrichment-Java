package com.uno.zoo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

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

@Component
public class DAO extends NamedParameterJdbcDaoSupport {
	private static final String LOGIN_USER_SQL = "SELECT User_Id, User_Name, User_Status, User_FirstName, User_LastName, User_Department from user WHERE User_Name = :username AND User_Pass = sha2(:password, 256)";
	private static final String USERNAME_EXISTS_SQL = "SELECT EXISTS (SELECT 1 FROM user WHERE User_Name = :username) AS doesExist";
	private static final String ADD_USER_SQL = "INSERT INTO user (User_Name, User_Pass, User_Status, User_Department, User_FirstName, User_LastName) VALUES (:username, sha2(:password, 256), :status, :department, :firstName, :lastName)";
	private static final String GET_DEPARTMENTS_SQL = "SELECT Department_Id, Department_Name FROM department ORDER BY Department_Name asc";
	private static final String GET_CATEGORIES_SQL = "SELECT Category_Id, Category_Name, Category_Description FROM category ORDER BY Category_Name asc";
	private static final String GET_SPECIES_SQL = "SELECT Species_Id, Species_Name, Species_Description, Species_IsisNumber FROM species ORDER BY Species_Name ASC";
	private static final String GET_ITEMS_SQL = "SELECT Item_Id, Item_Name FROM item ORDER BY Item_Name ASC";
	private static final String GET_USERS_SQL = "SELECT a.User_Id, a.User_Name, a.User_FirstName, a.User_LastName, b.Department_Name from user as a INNER JOIN department as b ON a.User_Department=b.Department_Id ORDER BY User_Id ASC";
	private static final String ENRICHMENT_REQUEST_FORM_SQL = "INSERT INTO enrichment_experience "
			+ "(Enrichment_Department, Enrichment_species, Enrichment_Name, Enrichment_Description, "
			+ "Enrichment_PresentationMethod, Enrichment_TimeStart, Enrichment_Frequency, "
			+ "Enrichment_LifeStrategies, Enrichment_PreviousUse, Enrichment_SafetyQuestions, "
			+ "Enrichment_RisksHazards, Enrichment_Concerns, Enrichment_ExpectedBehavior, "
			+ "Enrichment_Source, Enrichment_TimeRequired, Enrichment_Construction, Enrichment_Volunteers, "
			+ "Enrichment_Submittor, Enrichment_DateSubmitted) values "
			+ "(:dept, :species, :name, :description, :presentationMethod, :timeStart, :frequency, "
			+ ":lifeStrategies, :previousUse, :safetyQuestions, :risksHazards, :concerns, :expectedBehavior,"
			+ ":source, :timeRequired, :construction, :volunteers, :submitter, :dateSubmitted)";
	private static final String INSERT_NEW_ITEM_SQL = "INSERT INTO item (Item_Name, Item_Photo, Item_ApprovalStatus, Item_Comments, Item_SafetyNotes, Item_Exceptions) VALUES (:name, :photo, :approval, :comments, :safetyNotes, :exceptions)";
	private static final String REMOVE_USER_SQL = "DELETE FROM user WHERE User_Id = :userId AND User_Name = :username";
	
	public DAO(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	/**
	 * Accesses the database and retrieves the user information from valid credentials are provided.
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
					info.setDepartment(rs.getString("User_Department"));
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
			params.addValue("username", user.getUsername());
			params.addValue("password", user.getPassword());
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
	 */
	public StandardReturnObject insertRequestForm(CompleteRequestForm form) throws DataAccessException {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		// TODO: Enrichment_Item - id from 'item' table
		// TODO: Enrichment_Animal - id from 'animal' table, can be null
		// TODO: Enrichment_Location - id from 'location' table
		// TODO: Enrichment_TimeEnd - datetime
		// TODO: Enrichment_Goal - varchar(1000)
		// TODO: Enrichment_Inventory - varchar(10)
		// TODO: Enrichment_IsApproved - int
		params.addValue("dept", form.getDepartment().getDepartmentId()); // id from 'department' table
		params.addValue("species", form.getSpecies().getSpeciesId()); // id from 'species' table
		params.addValue("name", form.getEnrichmentName()); // have
		params.addValue("description", form.getEnrichmentDescription()); // have
		params.addValue("presentationMethod", form.getEnrichmentPresentation()); // have
		params.addValue("timeStart", form.getTimeRequired()); // TODO: needs to be type datetime
		params.addValue("frequency", form.getEnrichmentFrequency()); // TODO: needs to be int
		params.addValue("lifeStrategies", form.isLifeStrategiesWksht()); // TODO: needs to be int
		params.addValue("previousUse", form.isAnotherDeptZoo()); // TODO: needs to be int
		params.addValue("safetyQuestions", form.isSafetyQuestion()); // TODO: needs to be int
		params.addValue("risksHazards", form.isRisksQuestion()); // TODO: needs to be int
		params.addValue("concerns", form.getSafetyComment()); // have
		params.addValue("expectedBehavior", form.getNaturalBehaviors()); // have
		params.addValue("source", form.getSource()); // have
		params.addValue("timeRequired", form.getTimeRequired()); // TODO: needs to be int
		params.addValue("construction", form.getWhoConstructs()); // have
		params.addValue("volunteers", form.isVolunteerDocentUtilized()); // TODO: needs to be int
		params.addValue("submitter", form.getNameOfSubmitter()); // id from 'user' table
		params.addValue("dateSubmitted", form.getDateOfSubmission()); // have
		
		// Convert array of categories into a single string
		@SuppressWarnings("unused")
		String str = String.join(",", form.getEnrichmentCategory());
		
//		getNamedParameterJdbcTemplate().query(ENRICHMENT_REQUEST_FORM_SQL, rowMapper);
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
	
	public StandardReturnObject removeUsers(List<UserListInfo> users) throws DataAccessException, Exception {
		StandardReturnObject retObject = new StandardReturnObject();
		
		SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(users.toArray());
		int[] rowsAffected = getNamedParameterJdbcTemplate().batchUpdate(REMOVE_USER_SQL, batchArgs);
		
		for(int i : rowsAffected) {
			if(i == 0) {
				throw new Exception("Number rows affected when removing users was less than 1");
			}
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
					newSpecies.setSpeciesIsisNumber(Integer.parseInt(rs.getString("Species_IsisNumber")));
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
					info.add(newUser);
				}
				return info;
			}
		};
		
		return getNamedParameterJdbcTemplate().query(GET_USERS_SQL, rowMapper);
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
}
