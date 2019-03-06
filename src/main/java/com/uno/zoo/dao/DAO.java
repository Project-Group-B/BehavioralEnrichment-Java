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
import org.springframework.stereotype.Component;

import com.uno.zoo.dto.CategoryInfo;
import com.uno.zoo.dto.DepartmentInfo;
import com.uno.zoo.dto.RequestForm;
import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserInfo;
import com.uno.zoo.dto.UserLogIn;
import com.uno.zoo.dto.UserSignUp;

@Component
public class DAO extends NamedParameterJdbcDaoSupport {
	private static final String LOGIN_USER_SQL = "SELECT User_Name, User_Status, User_FirstName, User_LastName, User_Department from user WHERE User_Name = :username AND User_Pass = sha2(:password, 256)";
	private static final String USERNAME_EXISTS_SQL = "SELECT EXISTS (SELECT 1 FROM user WHERE User_Name = :username) as doesExist";
	private static final String ADD_USER_SQL = "INSERT INTO user (User_Name, User_Pass, User_Status, User_Department, User_FirstName, User_LastName) VALUES (:username, sha2(:password, 256), :status, :department, :firstName, :lastName)";
	private static final String GET_DEPARTMENTS_SQL = "SELECT Department_Id, Department_Name from department";
	private static final String GET_CATEGORIES_SQL = "SELECT Category_Id, Category_Name, Category_Description from category";
	
	public DAO(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	public UserInfo login(UserLogIn user) throws DataAccessException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", user.getUsername());
		params.addValue("password", user.getPassword());
		
		ResultSetExtractor<UserInfo> rowMapper = new ResultSetExtractor<UserInfo>() {
			@Override public UserInfo extractData(ResultSet rs) throws SQLException {
				UserInfo info = new UserInfo();
				if(rs.next()) {
					info.setLoggedIn(true);
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

	public StandardReturnObject signUp(UserSignUp user) throws DataAccessException, SQLException, Exception {
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
				throw new Exception("Rows affected when signing up was less than 1.");
			}
			
			retObject.setMessage(rowsAffected > 0 ? "You're all signed up! Returning to login..." : "Error signing up");
		}
		
		return retObject;
	}
	
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
	
	public List<CategoryInfo> getCategories() throws NumberFormatException, SQLException, DataAccessException {
		ResultSetExtractor<List<CategoryInfo>> rowMapper = new ResultSetExtractor<List<CategoryInfo>>() {
			@Override public List<CategoryInfo> extractData(ResultSet rs) throws SQLException {
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
	
	public StandardReturnObject insertRequestForm(RequestForm form) throws DataAccessException {
		StandardReturnObject retObject = new StandardReturnObject();
		// Convert array of categories into a single string
		@SuppressWarnings("unused")
		String str = String.join(",", form.getEnrichmentCategory());
		
		retObject.setMessage("Successfully entered form.");
		return retObject;
	}
	
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
