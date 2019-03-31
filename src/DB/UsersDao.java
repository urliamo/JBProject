package DB;

//import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Enums.ClientType;
import JavaBeans.User;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import Utils.DateUtils;
//import com.avi.coupons.utils.JdbcUtils;

public class UsersDao {
	
	private ConnectionPool connectionPool = ConnectionPool.getInstance();

	public long createUser(User user) throws ApplicationException, InterruptedException {
		//Turn on the connections
		Connection connection=null;
		PreparedStatement preparedStatement=null;

		try {
			//Establish a connection from the connection manager
			connection=connectionPool.getConnection();

			//Creating the SQL query
			//CompanyID is defined as a primary key and auto incremented
			
			String sqlStatement = null;

			// 2 types of users, we insert the companyId parameter for a "company" type user
			if (user.getCompanyId() == null){
				sqlStatement="INSERT INTO Users (user, password, type) VALUES(?,?,?)";
			}
			else {
				sqlStatement="INSERT INTO Users (user, password, type, CompanyID ) VALUES(?,?,?, ?)";
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
			
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				long id = resultSet.getLong(1);
				
				return id;
			}
			else
			{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to create user id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Create company failed");
		} 
		finally {
			//Closing the resources
			connectionPool.restoreConnection(connection);
		}
	}

	public boolean isUserExistsByName(String userName) throws ApplicationException, InterruptedException {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet result=null;

		try {
			//Establish a connection from the connection manager
			connection=connectionPool.getConnection();

			//Creating the SQL query
			String sqlStatement="SELECT * FROM Users WHERE user_name = ?";

			//Combining between the syntax and our connection
			preparedStatement=connection.prepareStatement(sqlStatement);

			//Replacing the question marks in the statement above with the relevant data
			preparedStatement.setString(1, userName);

			//Executing the query and saving the DB response in the resultSet.
			result=preparedStatement.executeQuery();

			if (!result.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException( e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Failed to check if user exists by name");
		}
		finally {
			//Closing the resources
			connectionPool.restoreConnection(connection);
		}

	}
	
	
	public ClientType login(String user, String password) throws ApplicationException, InterruptedException {
		//Turn on the connections
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet result=null;

		try {
			//Establish a connection from the connection manager
			connection=connectionPool.getConnection();

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
		} catch (SQLException e) {
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException( e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Get company has failed");
		}
		finally {
			//Closing the resources
			connectionPool.restoreConnection(connection);
		}
	}

}
