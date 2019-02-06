package com.uno.zoo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.uno.zoo.dto.StandardReturnObject;
import com.uno.zoo.dto.UserLogin;

@Component
public class DAO extends NamedParameterJdbcDaoSupport {
	private static final String LOGIN_USER_SQL = "SELECT EXISTS (SELECT 1 FROM demo.users WHERE username = :username AND password = sha2(:password, 256)) as doesExist";
	private static final String USERNAME_EXISTS_SQL = "SELECT EXISTS (SELECT 1 FROM demo.users WHERE username = :username) as doesExist";
	private static final String ADD_USER_SQL = "INSERT INTO demo.users (username, password) VALUES (:username, sha2(:password, 256))";
	
	public DAO(DataSource dataSource) {
		super.setDataSource(dataSource);
	}
	
	// can also place this in the function itself; I'm placing it here to avoid code re-use between the
	// login() and signUpUser() functions
	ResultSetExtractor<Boolean> existsRowMapper = new ResultSetExtractor<Boolean>() {
		@Override public Boolean extractData(ResultSet rs) throws SQLException {
			if(rs.next()) {
				return Boolean.valueOf(rs.getBoolean("doesExist"));
			} else {
				return Boolean.FALSE;
			}
		}
	};

	public boolean login(UserLogin user) throws DataAccessException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", user.getUsername());
		params.addValue("password", user.getPassword());
		
		return getNamedParameterJdbcTemplate().query(LOGIN_USER_SQL, params, existsRowMapper).booleanValue();
	}

	public StandardReturnObject signUp(UserLogin user) throws DataAccessException, SQLException {
		StandardReturnObject retObject = new StandardReturnObject();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("username", user.getUsername());
		
		boolean usernameExists = getNamedParameterJdbcTemplate().query(USERNAME_EXISTS_SQL, params, existsRowMapper).booleanValue();
		if(usernameExists) {
			retObject.setError(true);
			retObject.setErrorMsg("Username taken");
		} else {
			params.addValue("password", user.getPassword());
			
			@SuppressWarnings("unused")
			int rowsAffected = getNamedParameterJdbcTemplate().update(ADD_USER_SQL, params);
			
			retObject.setMessage("You're all signed up! Returning to login...");
		}
		
		return retObject;
	}
}
