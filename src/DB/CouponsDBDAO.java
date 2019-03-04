package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale.Category;
import JavaBeans.Coupon;


/**
 * DB data access object for coupons. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see 		JavaBeans.Coupon
 */public class CouponsDBDAO implements CouponsDAO {
	
	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	
	
	/**
	 *  returns true if coupon exists with this ID
	 *
	 * @param  id the ID of the coupon to be searched
	 * @return		true if company exists in DB
	 */
	
	public boolean isCouponExists(int id) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT Count(*) AS Count FROM Coupons WHERE ID = '%d'",
					id);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					resultSet.next();

					int count = resultSet.getInt("Count");

					return count == 1;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 *  returns true if coupon  with specified ID belonging to customer with specified ID exists
	 *
	 * @param  coupondId the ID of the coupon to be searched
	 * @param  customerId the ID of the customer to be searched
	 * @return		true if coupon belongs to customer
	 */
	
	public boolean isCouponPurchaseExists(int customerID,int couponID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT Count(*) AS Count FROM Customers_VS_Coupons WHERE CUSTOMER_ID = '%d' && COUPON_ID = '%d'",
					customerID,couponID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					resultSet.next();

					int count = resultSet.getInt("Count");

					return count == 1;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 * adds a new coupon to the DB.
	 *<p>
	 *this also generates a new sequential ID for the coupon and adds it to it the coupon Java object.
	 *
	 * @param  coupon the new coupon to be added to the DB
	 * @see			JavaBeans.Coupon
	 */
	
	public void addCoupon(Coupon Coupon) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			

			String sql = String.format("INSERT INTO Coupons(DESCRIPTION, IMAGE, TITLE, AMOUNT, START_DATE, END_DATE, COMPANY_ID, CATEGORY_ID, PRICE) " + 
					"VALUES('%s', '%s', '%s', '%d', '%tF', '%tF', '%d', '%d', '%f')",
					Coupon.getDescription(), Coupon.getImage(), Coupon.getTitle(), Coupon.getAmount(), java.sql.Date.valueOf(Coupon.getStart_date()), java.sql.Date.valueOf(Coupon.getEnd_date()), Coupon.getCompany_id(), Coupon.getCategory_id(), Coupon.getPrice());

			try(PreparedStatement preparedStatement = 
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

				preparedStatement.executeUpdate();

				try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
					resultSet.next();
					int id = resultSet.getInt(1);
					Coupon.setId(id); // Add the new created id into the Coupon object.
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	
	/**
	 * updates an existing coupon in the DB.
	 *
	 * @param  coupon the  coupon to be added updated in the DB
	 * @see			JavaBeans.Coupon
	 */
	public void updateCoupon(Coupon Coupon) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();

			String sql = String.format(
					"UPDATE Coupons SET DESCRIPTION='%s', IMAGE='%s', TITLE='%s', AMOUNT='%d', START_DATE='%tF', END_DATE='%tF', CATEGORY_ID='%d', PRICE='%f' WHERE ID=%d",
					 Coupon.getDescription(), Coupon.getImage(), Coupon.getTitle(), Coupon.getAmount(), java.sql.Date.valueOf(Coupon.getStart_date()), java.sql.Date.valueOf(Coupon.getEnd_date()), Coupon.getCategory_id(), Coupon.getPrice(), Coupon.getId());
			
			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	/**
	 * removes a coupon from the DB.
	 * 
	 * @param  couponID the Id of the coupon to be removed from the DB
	 * @see			JavaBeans.Coupon
	 */
	public void deleteCoupon(int CouponID) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();

			String sql = String.format("DELETE FROM Coupons WHERE ID=%d", CouponID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	
	/**
	 * returns all coupons as a list.
	 * 
	 * @see			JavaBeans.Coupon
	 * @return ArrayList of all coupons as coupon objects.
	 */
	public ArrayList<Coupon> getAllCoupons() throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = "SELECT * FROM Coupons";

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     int id = resultSet.getInt("ID");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate strat_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     int company_id = resultSet.getInt("COMPANY_ID");
					     int category_id = resultSet.getInt("CATEGORY_ID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, id,amount,strat_date,end_date,company_id, category_id,price);
						
						allCoupons.add(Coupon);
					}
					
					return allCoupons;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	/**
	 * returns all expired coupons as a list.
	 * 
	 * @see			JavaBeans.Coupon
	 * @return ArrayList of all expired coupons as coupon objects.
	 */
	public ArrayList<Coupon> getExpiredCoupons() throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = "SELECT * FROM Coupons where end_date <"+java.sql.Date.valueOf(LocalDate.now());

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     int id = resultSet.getInt("ID");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate strat_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     int company_id = resultSet.getInt("COMPANY_ID");
					     int category_id = resultSet.getInt("CATEGORY_ID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, id,amount,strat_date,end_date,company_id, category_id,price);
						
						allCoupons.add(Coupon);
					}
					
					return allCoupons;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 * returns coupon with specified ID.
	 * 
	 * @param		couponID the ID of the coupon to be returned.
	 * @return 		coupon object with the data of the coupon with specified ID.
	 */
	
	public Coupon getOneCoupon(int CouponID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM Coupons WHERE ID=%d", CouponID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					resultSet.next();

					String description = resultSet.getString("DESCRIPTION");
				     String image = resultSet.getString("IMAGE");
				     String title = resultSet.getString("TITLE");
				     int id = resultSet.getInt("ID");
				     int amount = resultSet.getInt("AMOUNT");
				     LocalDate strat_date = resultSet.getDate("START_DATE").toLocalDate();
				     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
				     int company_id = resultSet.getInt("COMPANY_ID");
				     int category_id = resultSet.getInt("CATEGORY_ID");
				     double price = resultSet.getDouble("PRICE");

					Coupon Coupon = new Coupon(description, image, title, id,amount,strat_date,end_date,company_id, category_id,price);
					
					return Coupon;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	/**
	 * returns the category of the coupon with the specified ID.
	 * 
	 * @see			JavaBeans.Coupon
	 * @param		couponID the ID of the coupon to be returned.
	 * @return 		ID of the category of the coupon with specified ID.
	 */
	
	public Category getCouponCategory(int categoryID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM categories WHERE ID=%d", categoryID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					resultSet.next();

					String name = resultSet.getString("NAME");
				    
					Category category = Category.valueOf(name);
					
					return category;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 * removes a coupon purchase from the DB.
	 * 
	 * @param  couponID the ID of the coupon to be removed from the DB
	 * @param  customerID the ID of the customer the coupon should be removed from
	 */
	public void deleteCouponPurchase(int couponID, int customerID) throws Exception {
		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			String sql;
			
			//set string for all customers with coupon
			if (customerID==-1)
			{
				 sql = String.format("DELETE FROM CUSTOMERS_VS_COUPONS WHERE COUPON_ID=%d", couponID);

			}
			//set string for all coupons of customer
			else if(couponID==-1) {
				 sql = String.format("DELETE FROM CUSTOMERS_VS_COUPONS WHERE CUSTOMER_ID=%d", customerID); 
			}
			//set string for single customer coupon
			else {
			 sql = String.format("DELETE FROM CUSTOMERS_VS_COUPONS WHERE CUSTOMER_ID=%d && COUPON_ID=%d", customerID, couponID);
			}
			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
		
	}

	/**
	 * adds a new coupon purchase to the customer.
	 *
	 * @param couponID the ID of the coupon to be purchased by the customer
	 * @param customerID the ID of the customer the coupon should be added to
	 */
	public void addCouponPurchase(int CouponID, int CustomerID) throws Exception {
		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			
			String sql = String.format("INSERT INTO CUSTOMERS_VS_COUPONS(CUSTOMER_ID, COUPON_ID) " + 
					"VALUES('%d', '%d')",
					CustomerID, CouponID);

			try(PreparedStatement preparedStatement = 
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

				preparedStatement.executeUpdate();

				
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
		
	}

	

}