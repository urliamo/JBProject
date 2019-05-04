package DB;

import java.sql.Connection;
import Utils.JdbcUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import Exceptions.ApplicationException;
import Enums.ErrorType;
//import JavaBeans.Coupon;
import JavaBeans.Customer;
import Utils.DateUtils;


/**
 * DB data access object for customers. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see         JavaBeans.customer
 * @see         JavaBeans.customer
 */

public class CustomerDAO implements ICustomerDAO {	


/**
 * find the ID of customer with input mail and pass.
 *
 * @param  email mail used to login
 * @param password password used to login
 * @return long containing the customer ID
 * @throws customer does not exist!
 */
	public long getCustomerID(String firstName, String lastName) throws ApplicationException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
				connection =JdbcUtils.getConnection();

				//set sql string to find a customer with the mail\pass combination
				String sql = String.format(
				"SELECT ID FROM Customers WHERE firstName = '%s' AND lastName = '%s'",
				firstName,lastName);
				preparedStatement = connection.prepareStatement(sql);
				//execute sql statement
				resultSet = preparedStatement.executeQuery();

					
					if (!resultSet.next())
					{
						throw new ApplicationException(ErrorType.INVALID_NAME,"customer does not exist!");
					}
					//return the id of the selected customer
					return resultSet.getLong("CustomerID");

				
			
		}
		catch (SQLException e) {
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException( e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Failed to find customerID by name");
		}
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	

		/**
		 *adds a new customer to the DB.
		 *
		 * @param  customer the new customer to be added to the DB.
		 * @throws InterruptedException 
		 * @throws ApplicationException 
		 * @see 		JavaBeans.customer
		 */
	 
	 public void addCustomer(Customer customer) throws ApplicationException  {

			//Turn on the connections
			Connection connection=null;
			PreparedStatement preparedStatement=null;

			try {
				//Establish a connection from the connection manager
				connection =JdbcUtils.getConnection();

				//Creating the SQL query
				//CompanyID is defined as a primary key and auto incremented
				
				String sqlStatement = null;

				// 2 types of users, we insert the companyId parameter for a "company" type user
					sqlStatement="INSERT INTO Customers (CustomerID, FirstName, LastName) VALUES(?,?,?)";
				

				//Combining between the syntax and our connection
				preparedStatement=connection.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);

				//Replacing the question marks in the statement above with the relevant data
				preparedStatement.setLong(1,customer.getCustomerId());
				preparedStatement.setString(2,customer.getFirstName());
				preparedStatement.setString(3, customer.getLastName());
				
				//Executing the update
				preparedStatement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				//If there was an exception in the "try" block above, it is caught here and notifies a level above.
				throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
						+" Create customer failed");
			} 
			finally {
				//Closing the resources
				JdbcUtils.closeResources(connection, preparedStatement);
			}
				
	}

	
	 /**
		 *updates an existing customer in the DB.
		 *
		 * @param  customer the customer data to be updated in the DB.
	 * @throws InterruptedException 
		 * @see 		JavaBeans.Customer
		 */	
	public void updateCustomer(Customer Customer) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {

			connection =JdbcUtils.getConnection();
			//set sql string to include the new properties of the customer to be updated in table (by ID)

			String sql = String.format(
					"UPDATE Customers SET FIRST_NAME='%s', LAST_NAME='%s', WHERE CustomerID=%d",
					Customer.getFirstName(),Customer.getLastName(), Customer.getCustomerId());

			//execute sql statement
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" update customer failed");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	/**
	 *removes an existing customer from the DB.
	 *<p>
	 *
	 * @param  customer the customer data to be removed from the DB.
	 * @see 		JavaBeans.Customer
	 */
	public void deleteCustomer(long CustomerID) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement= null;
		try {

			connection =JdbcUtils.getConnection();
			//set sql string to include customer ID to be deleted
			String sql = String.format("DELETE FROM Customers WHERE CustomerID=%d", CustomerID);

			//execute sql statement
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" delete customer failed");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	/**
	 * returns a list of all customers
	 * 
	 * @see			JavaBeans.Customer
	 * @return ArrayList of all Customers
	 */
	
	public Collection<Customer> getAllCustomers() throws ApplicationException {

		Connection connection = null;
		Customer customer = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection =JdbcUtils.getConnection();

			//set sql string to return all data from customers table
			String sql = "SELECT * FROM Customers";

			preparedStatement = connection.prepareStatement(sql);

				//execute sql statement
				 resultSet = preparedStatement.executeQuery();

					//create arraylist to include customers and be returned
					Collection<Customer> allCustomers = new ArrayList<Customer>();
					//scan each item in the result set
					while(resultSet.next()) {

						customer = extractCustomerFromResultSet(resultSet);

						allCustomers.add(customer);
					}
					//return customer arraylist
					return allCustomers;
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find customers failed");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	
	/**
	 * returns the DB data of any customer by ID as a company object.
	 * 
	 * @see			JavaBeans.Customer
	 * @return		Company object with the specified company data.
	 */
	
	public Customer getOneCustomer(long CustomerID) throws ApplicationException {

		Connection connection = null;
		Customer customer = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			connection =JdbcUtils.getConnection();
			//set sql string to find customer with selected ID from customers table
			String sql = String.format("SELECT * FROM Customers WHERE ID=%d", CustomerID);

			preparedStatement = connection.prepareStatement(sql) ;

			resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {

						customer = extractCustomerFromResultSet(resultSet);	
					}	
					
			return customer;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find customer failed");
		} 
		finally {
			
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * returns a list of the IDs of all customer coupons with specified ID
	 * 
	 * @param customerID the ID of the customer whose coupons are to be returned
	 * @see			JavaBeans.Coupon
	 * @return 		ArrayList of coupons IDs belonging to this customer	
	 */
	
	
	
	
	private Customer extractCustomerFromResultSet(ResultSet result) throws SQLException {
		Customer customer = new Customer(result.getString("firstName"),result.getString("lastName"),result.getLong("id"));

		return customer;
	}


}
