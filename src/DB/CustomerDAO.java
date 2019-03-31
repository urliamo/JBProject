package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import Exceptions.ApplicationException;
import Enums.ErrorType;
import JavaBeans.Coupon;
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

private ConnectionPool connectionPool = ConnectionPool.getInstance();
	

/**
 * compares input mail and pass to customers DB and returns true if found customer with this combination in the DB.
 *
 * @param  email mail used to login
 * @param password password used to login
 * @return true if customer with email\pass combination was found in DB, otherwise false.
 */
/*should be moved to UsersDAO?
public boolean isCustomerExists(String email, String password) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
			//set sql string to count amount of customers with mail\pass combination
			String sql = String.format(
					"SELECT * FROM users WHERE EMAIL = '%s' AND PASSWORD = '%s' AND Type='customer'",
					email,password);
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql); 
				//execute sql statement
			ResultSet resultSet = preparedStatement.executeQuery();
				//return true if customer was found
			if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
				}
				else
				{
				return true;
				}
		}
		catch (SQLException e) {
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException( e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Failed to check if customer exists by mail\pass");
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	*/
/**
 * find the ID of customer with input mail and pass.
 *
 * @param  email mail used to login
 * @param password password used to login
 * @return long containing the customer ID
 * @throws customer does not exist!
 */
	public long getCustomerID(String firstName, String lastName) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
			//set sql string to find a customer with the mail\pass combination
			String sql = String.format(
					"SELECT ID FROM Customers WHERE firstName = '%s' AND lastName = '%s'",
					firstName,lastName);

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
				//execute sql statement
				ResultSet resultSet = preparedStatement.executeQuery();

					
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
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 * compares input mail to customers DB and returns true if found customer with this mail in the DB.
	 *
	 * @param  email mail used to login
	 * @return true if customer with email was found in DB
	 * @throws InterruptedException 
	 */
	/*should be moved to UsersDAO?

	public boolean isCustomerExistsByMail(String email) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
			
			//set sql string to count customers with the mail specified
			String sql = String.format(
					"SELECT * FROM users WHERE EMAIL = '%s' AND Type='customer'",
					email);

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
				//execute sql statement
				ResultSet resultSet = preparedStatement.executeQuery();

					//return true if any items are returned
					if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
				}
				else
				{
				return true;
				}				
			
		}
		catch (SQLException e) {
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException( e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" Failed to check if customer exists by mail");
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	*/

		/**
		 *adds a new customer to the DB.
		 *
		 * @param  customer the new customer to be added to the DB.
		 * @throws InterruptedException 
		 * @throws ApplicationException 
		 * @see 		JavaBeans.customer
		 */
	 
	 public void addCustomer(Customer customer) throws InterruptedException, ApplicationException  {

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
				connectionPool.restoreConnection(connection);
			}
				
	}

	
	 /**
		 *updates an existing customer in the DB.
		 *
		 * @param  customer the customer data to be updated in the DB.
	 * @throws InterruptedException 
		 * @see 		JavaBeans.Customer
		 */	
	public void updateCustomer(Customer Customer) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			//set sql string to include the new properties of the customer to be updated in table (by ID)

			String sql = String.format(
					"UPDATE Customers SET FIRST_NAME='%s', LAST_NAME='%s', WHERE CustomerID=%d",
					Customer.getFirstName(),Customer.getLastName(), Customer.getCustomerId());

			//execute sql statement
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
			connectionPool.restoreConnection(connection);
		}
	}

	/**
	 *removes an existing customer from the DB.
	 *<p>
	 *
	 * @param  customer the customer data to be removed from the DB.
	 * @see 		JavaBeans.Customer
	 */
	public void deleteCustomer(int CustomerID) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			//set sql string to include customer ID to be deleted
			String sql = String.format("DELETE FROM Customers WHERE CustomerID=%d", CustomerID);

			//execute sql statement
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
			connectionPool.restoreConnection(connection);
		}
	}

	/**
	 * returns a list of all customers
	 * 
	 * @see			JavaBeans.Customer
	 * @return ArrayList of all Customers
	 */
	
	public ArrayList<Customer> getAllCustomers() throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			//set sql string to return all data from customers table
			String sql = "SELECT * FROM Customers";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

				//execute sql statement
				ResultSet resultSet = preparedStatement.executeQuery();

					//create arraylist to include customers and be returned
					ArrayList<Customer> allCustomers = new ArrayList<Customer>();
					//scan each item in the result set
					while(resultSet.next()) {

						int id = resultSet.getInt("CustomerID");
						String firstname = resultSet.getString("FIRST_NAME");
						String lastname = resultSet.getString("LAST_NAME");
						String email = resultSet.getString("EMAIL");
						String password = resultSet.getString("PASSWORD");
			
						Customer Customer = new Customer(lastname,firstname, email, password, id);
						//add item from result set to customer arraylist

						allCustomers.add(Customer);
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
			connectionPool.restoreConnection(connection);
		}
	}
	
	
	/**
	 * returns the DB data of any customer by ID as a company object.
	 * 
	 * @see			JavaBeans.Customer
	 * @return		Company object with the specified company data.
	 */
	
	public Customer getOneCustomer(int CustomerID) throws ApplicationException, InterruptedException {

		Connection connection = null;
		Customer customer = null;
		try {
			connection = connectionPool.getConnection();
			//set sql string to find customer with selected ID from customers table
			String sql = String.format("SELECT * FROM Customers WHERE ID=%d", CustomerID);

			PreparedStatement preparedStatement = connection.prepareStatement(sql) ;

				ResultSet resultSet = preparedStatement.executeQuery();

					resultSet.next();

					int id = resultSet.getInt("ID");
					String firstname = resultSet.getString("FIRST_NAME");
					String lastname = resultSet.getString("LAST_NAME");
					String email = resultSet.getString("EMAIL");
					String password = resultSet.getString("PASSWORD");
		
					//
					 customer = new Customer(lastname,firstname, email, password, id);

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
			
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 * returns a list of the IDs of all customer coupons with specified ID
	 * 
	 * @param customerID the ID of the customer whose coupons are to be returned
	 * @see			JavaBeans.Coupon
	 * @return 		ArrayList of coupons IDs belonging to this customer	
	 */
	
	public ArrayList<Coupon> getAllCouponsByCustomer(int customerID) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM Coupons WHERE coupon_id =(SELECT coupon_id FROM Purchases WHERE customer_id=?)");
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,customerID);

				ResultSet resultSet = preparedStatement.executeQuery();

					ArrayList<Coupon> customerCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     long coupon_id = resultSet.getLong("coupon_id");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate strat_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     long company_id = resultSet.getLong("COMPANY_ID");
					     int category_id = resultSet.getInt("CATEGORY_ID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, coupon_id,amount,strat_date,end_date,company_id, category_id,price);
						
						customerCoupons.add(Coupon);
					}
					
					return customerCoupons;
				}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find customer coupons failed");
		} 
		
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

}
