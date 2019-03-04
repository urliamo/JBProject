package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import JavaBeans.Customer;


/**
 * DB data access object for customers. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see         JavaBeans.customer
 * @see         JavaBeans.customer
 */

public class CustomerDBDAO implements CustomerDAO {

private ConnectionPool connectionPool = ConnectionPool.getInstance();
	

/**
 * compares input mail and pass to customers DB and returns true if found customer with this combination in the DB.
 *
 * @param  email mail used to login
 * @param password password used to login
 * @return true if customer with email\pass combination was found in DB
 */

public boolean isCustomerExists(String email, String password) throws Exception {

		Connection connection = null;

		try {
			
			
			connection = connectionPool.getConnection();
			//set sql string to count amount of customers with mail\pass combination
			String sql = String.format(
					"SELECT Count(*) AS Count FROM Customers WHERE EMAIL = '%s' AND PASSWORD = '%s'",
					email,password);
			
			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				//execute sql statement
				try(ResultSet resultSet = preparedStatement.executeQuery()) {
					
					resultSet.next();
					//return true if customer was found
					return resultSet.getInt("Count") == 1 ;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
/**
 * find the ID of customer with input mail and pass.
 *
 * @param  email mail used to login
 * @param password password used to login
 * @return int conaining the customer ID
 * @throws customer does not exist!
 */
	public int getCustomerID(String email, String password) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
			//set sql string to find a customer with the mail\pass combination
			String sql = String.format(
					"SELECT ID FROM Customers WHERE EMAIL = '%s' AND PASSWORD = '%s'",
					email,password);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				//execute sql statement
				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					
					if (!resultSet.next())
					{
						throw new Exception("customer does not exist!");
					}
					//return the id of the selected customer
					return resultSet.getInt("ID");

				}
			}
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
	 */	public boolean isCustomerExists(String email) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();
			
			//set sql string to count customers with the mail specified
			String sql = String.format(
					"SELECT Count(*) AS Count FROM Customers WHERE EMAIL = '%s'",
					email);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				//execute sql statement
				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					resultSet.next();
					//return true if the result is 1.
					return 1 == resultSet.getInt("Count");
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

		/**
		 *adds a new customer to the DB.
		 *
		 * @param  customer the new customer to be added to the DB.
		 * @see 		JavaBeans.customer
		 */
	 
	 public void addCustomer(Customer Customer) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			
			//set sql string to include properties of new customer to be added to table

			String sql = String.format("INSERT INTO Customers(FIRST_NAME,LAST_NAME, EMAIL, PASSWORD) " + 
					"VALUES('%s', '%s', '%s', '%s')",
					Customer.getFirstName(),Customer.getLastName(), Customer.getEmail(), Customer.getPassword());

			try(PreparedStatement preparedStatement = 
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
				//execute sql statement
				preparedStatement.executeUpdate();
				
				
				try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
					resultSet.next();
					// Add the new created id into the Customer object.
					int id = resultSet.getInt(1);
					Customer.setId(id); 
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	
	 /**
		 *updates an existing customer in the DB.
		 *
		 * @param  customer the customer data to be updated in the DB.
		 * @see 		JavaBeans.Customer
		 */	
	public void updateCustomer(Customer Customer) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			//set sql string to include the new properties of the customer to be updated in table (by ID)

			String sql = String.format(
					"UPDATE Customers SET FIRST_NAME='%s', LAST_NAME='%s', EMAIL='%s', PASSWORD='%s' WHERE ID=%d",
					Customer.getFirstName(),Customer.getLastName(), Customer.getEmail(), Customer.getPassword(), Customer.getId());

			//execute sql statement
			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
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
	public void deleteCustomer(int CustomerID) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			//set sql string to include customer ID to be deleted
			String sql = String.format("DELETE FROM Customers WHERE ID=%d", CustomerID);

			//execute sql statement
			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
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
	
	public ArrayList<Customer> getAllCustomers() throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			//set sql string to return all data from customers table
			String sql = "SELECT * FROM Customers";

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				//execute sql statement
				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					//create arraylist to include customers and be returned
					ArrayList<Customer> allCustomers = new ArrayList<Customer>();
					//scan each item in the result set
					while(resultSet.next()) {

						int id = resultSet.getInt("ID");
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
			}
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
	
	public Customer getOneCustomer(int CustomerID) throws Exception {

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
	
	public ArrayList<Integer> getAllCouponsByCustomer(int customerID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT COUPON_ID FROM Customers_VS_Coupons WHERE CUSTOMER_ID=%d",customerID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					ArrayList<Integer> customerCoupons = new ArrayList<Integer>();
					
					while(resultSet.next()) {
						
					     int id = resultSet.getInt("COUPON_ID");
					  
						
	
						customerCoupons.add(id);
					}
					
					return customerCoupons;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
}
