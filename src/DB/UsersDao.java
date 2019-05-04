package DB;

//import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import Utils.JdbcUtils;

import Enums.ClientType;
import JavaBeans.User;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import Utils.DateUtils;
//import com.avi.coupons.utils.JdbcUtils;

public class UsersDAO {
	

	public long createUser(User user) throws ApplicationException {
		//Turn on the connections
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet = null;
		try {
			//Establish a connection from the connection manager
			connection=JdbcUtils.getConnection();

			//Creating the SQL query
			//CompanyID is defined as a primary key and auto incremented
			
			String sqlStatement = null;

			// 2 types of users, we insert the companyId parameter for a "company" type user
			if (user.getCompanyId() == null){
				sqlStatement="INSERT INTO Users (user, password, type) VALUES(?,?,?)";
			}
			else {
				sqlStatement="INSERT INTO Users (user, password, type, CompanyID ) VALUES(?,?,?,?)";
			}

			//Combining between the syntax and our connection
			preparedStatement=connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);

			//Replacing the question marks in the statement above with the relevant data
			preparedStatement.setString(1,user.getUserName());
			preparedStatement.setString(2,user.getPassword());
			preparedStatement.setString(3, user.getType().name());
			if (user.getCompanyId() == null)
			{
				preparedStatement.setLong(4, user.getCompanyId());

			}

			//Executing the update
			preparedStatement.executeUpdate();
			
		    resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				
				return id;
			}
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to create user id");
		
			
		} catch (SQLException e) {
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime() +" Create User failed");
		} 
		finally {
			//Closing the resources
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	public void deleteUserByMail(String userEmail) throws ApplicationException {

		Connection connection=null;
		PreparedStatement preparedStatement=null;


		try {
			connection = JdbcUtils.getConnection();
			String delete = "DELETE FROM users WHERE user_email=?";
			preparedStatement = connection.prepareStatement(delete);
			preparedStatement.setString(1, userEmail);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime() + "failed to delete the user");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);		
			}
	}
	
	public void deleteUserByID(long userID) throws ApplicationException {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JdbcUtils.getConnection();
			String delete = "DELETE FROM users WHERE user_id=?";
			preparedStatement = connection.prepareStatement(delete);
			preparedStatement.setLong(1, userID);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime() + "failed to delete the user");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	public void updateUser(User user) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			long userID = user.getId();
			String update = "UPDATE users SET user_Email=?, password=?, client_type=?, company_id=? WHERE user_id=?";
			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3, user.getType().name());
			preparedStatement.setLong(4, user.getCompanyId());
			preparedStatement.setLong(5, userID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime() + "dailed to update user information");
		}  finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public void deleteCompanysUsers(long companyID) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = JdbcUtils.getConnection();
			String delete = "DELETE FROM users WHERE company_ID=?";
			preparedStatement = connection.prepareStatement(delete);
			preparedStatement.setLong(1, companyID);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException( e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Failed delete users");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public Collection<User> getAllUsersByType(ClientType type) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;

		try {
			connection = JdbcUtils.getConnection();
			String getAllCompanies = "SELECT * FROM users WHERE client_type=?";
			preparedStatement = connection.prepareStatement(getAllCompanies);
			preparedStatement.setString(1, type.name());
			result = preparedStatement.executeQuery();

			Collection<User> allUsers = new ArrayList<>();

			while (result.next()) {
				allUsers.add(extractUserFromResultSet(result));
			}

			return allUsers;
			}
			catch (SQLException exception)
			{
				exception.printStackTrace();
				throw new ApplicationException( exception, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
						+" get All Users by Type failed");
			} finally
			{
			JdbcUtils.closeResources(connection, preparedStatement);
			}
	}
	public Collection<User> getAllUsers() throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet result = null;

		try {
			connection = JdbcUtils.getConnection();
			String getAllCompanies = "SELECT * FROM users";
			preparedStatement = connection.prepareStatement(getAllCompanies);
			result = preparedStatement.executeQuery();

			Collection<User> allUsers = new ArrayList<>();

			while (result.next()) {
				allUsers.add(extractUserFromResultSet(result));
			}

			return allUsers;

		}catch (SQLException exception) {
			exception.printStackTrace();
			throw new ApplicationException( exception, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" get All Users failed");
		} finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	public ClientType login(String user, String password) throws ApplicationException {
		//Turn on the connections
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet result=null;

		try {
			//Establish a connection from the connection manager
			connection = JdbcUtils.getConnection();

			//Creating the SQL query
			String sqlStatement="SELECT * FROM Users WHERE user_name = ? && password = ?";

			//Combining between the syntax and our connection
			preparedStatement=connection.prepareStatement(sqlStatement);

			//Replacing the question marks in the statement above with the relevant data
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, password);

			//Executing the query and saving the DB response in the resultSet.
			result=preparedStatement.executeQuery();

			// Login had failed (!!!!!!!!!!!!!!!)
			if (!result.next()) {
				throw new ApplicationException(ErrorType.LOGIN_FAILED, "Failed login");
			}
			
			ClientType clientType = ClientType.valueOf(result.getString("type"));
			return clientType;
		} catch (SQLException exception) {
			exception.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException( exception, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" login failed");
		}
		finally {
			//Closing the resources
			JdbcUtils.closeResources(connection, preparedStatement, result);
		}
	}
	
	private User extractUserFromResultSet(ResultSet result) throws SQLException {
		User user = new User();
		user.setId(result.getLong("user_id"));
		user.setEmail(result.getString("user_email"));
		user.setPassword(result.getString("password"));
		user.setType(ClientType.valueOf(result.getString("client_type")));
		user.setCompanyId(result.getLong("company_id"));

		return user;
	}
}
