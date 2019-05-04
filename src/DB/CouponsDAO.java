package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale.Category;

import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Coupon;
import Utils.DateUtils;


/**
 * DB data access object for coupons. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see 		JavaBeans.Coupon
 */public class CouponsDAO implements ICouponsDAO {
	
	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	
	
	/**
	 *  returns true if coupon exists with this ID
	 *
	 * @param  id the ID of the coupon to be searched
	 * @return		true if company exists in DB
	 */
	
	public boolean isCouponExists(long id) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT * AS Count FROM Coupons WHERE couponID = ?'");
	
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,id);

				ResultSet resultSet = preparedStatement.executeQuery();

				if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_ID,"coupon does not exist!");
				}
				else
				{
				return true;
				}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" check coupon exists failed");
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
	
	public long addCoupon(Coupon Coupon) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			
			if (Coupon.getStart_date().isAfter(Coupon.getEnd_date()))
			{
				throw new ApplicationException(ErrorType.INVALID_DATES, "coupon starts after it ends");

			}
			if (LocalDate.now().isAfter(Coupon.getEnd_date()))
			{
				throw new ApplicationException(ErrorType.INVALID_DATES, "coupon already expired");

			}
			String sql = String.format("INSERT INTO Coupons(DESCRIPTION, IMAGE, TITLE, AMOUNT, START_DATE, END_DATE, COMPANY_ID, CATEGORY_ID, PRICE) " + 
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");

			PreparedStatement preparedStatement = 
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,Coupon.getDescription());
			preparedStatement.setString(2,Coupon.getImage());
			preparedStatement.setString(3, Coupon.getTitle());
			preparedStatement.setInt(4,Coupon.getAmount());
			preparedStatement.setDate(5,java.sql.Date.valueOf(Coupon.getStart_date()));
			preparedStatement.setDate(6,java.sql.Date.valueOf(Coupon.getEnd_date()));
			preparedStatement.setLong(7, Coupon.getCompany_id());
			preparedStatement.setInt(8,Coupon.getCategory_id());
			preparedStatement.setDouble(9, Coupon.getPrice());
			
			
				preparedStatement.executeUpdate();

				ResultSet resultSet = preparedStatement.getGeneratedKeys();
					if (!resultSet.next()) {
						long id = resultSet.getInt(1);
						//Coupon.setId(id); // Add the new created id into the Coupon object.
						return id;
						}
						else
						{
						throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to create coupon id");
						}
					
					}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" add coupon failed");
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
	public void updateCoupon(Coupon Coupon) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {

			if (Coupon.getStart_date().isAfter(Coupon.getEnd_date()))
			{
				throw new ApplicationException(ErrorType.INVALID_DATES, "coupon starts after it ends");

			}
			if (LocalDate.now().isAfter(Coupon.getEnd_date()))
			{
				throw new ApplicationException(ErrorType.INVALID_DATES, "coupon already expired");

			}
			
			connection = connectionPool.getConnection();

			String sql = String.format(
					"UPDATE Coupons SET DESCRIPTION=?, IMAGE=?, TITLE=?, AMOUNT=?, START_DATE=?, END_DATE=?, CATEGORY_ID=?, PRICE=? WHERE couponID=?");
			
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1,Coupon.getDescription());
			preparedStatement.setString(2,Coupon.getImage());
			preparedStatement.setString(3, Coupon.getTitle());
			preparedStatement.setInt(4,Coupon.getAmount());
			preparedStatement.setDate(5,java.sql.Date.valueOf(Coupon.getStart_date()));
			preparedStatement.setDate(6,java.sql.Date.valueOf(Coupon.getEnd_date()));
			preparedStatement.setInt(7,Coupon.getCategory_id());
			preparedStatement.setDouble(8, Coupon.getPrice());
			preparedStatement.setLong(9, Coupon.getId());
				preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" update coupon failed");
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
	public void deleteCoupon(long CouponID) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();

			String sql = String.format("DELETE FROM Coupons WHERE couponID=?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, CouponID);

				preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" delete coupon failed");
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
	public ArrayList<Coupon> getAllCoupons() throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = "SELECT * FROM Coupons";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

				ResultSet resultSet = preparedStatement.executeQuery();

					ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     long id = resultSet.getLong("couponID");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate strat_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     long company_id = resultSet.getLong("COMPANYID");
					     int category_id = resultSet.getInt("CATEGORYID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, id,amount,strat_date,end_date,company_id, category_id,price);
						
						allCoupons.add(Coupon);
					}
					
					return allCoupons;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" return all coupons failed");
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
	public ArrayList<Coupon> getExpiredCoupons() throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = "SELECT * FROM Coupons where end_date <"+java.sql.Date.valueOf(LocalDate.now());

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

				ResultSet resultSet = preparedStatement.executeQuery();

					ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     long id = resultSet.getInt("couponID");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate strat_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     long company_id = resultSet.getInt("COMPANYID");
					     int category_id = resultSet.getInt("CATEGORYID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, id,amount,strat_date,end_date,company_id, category_id,price);
						
						allCoupons.add(Coupon);
					}
					
					return allCoupons;
				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" return expired coupons failed");
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
	
	public Coupon getOneCoupon(long CouponID) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM Coupons WHERE ID=?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,CouponID);

				ResultSet resultSet = preparedStatement.executeQuery();
				if (!resultSet.next())
				{
					throw new ApplicationException(ErrorType.INVALID_ID,"coupon does not exist!");
				}

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
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" return coupon failed");
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

			String sql = String.format("SELECT * FROM categories WHERE ID=?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,categoryID);

				ResultSet resultSet = preparedStatement.executeQuery();

					resultSet.next();

					String name = resultSet.getString("NAME");
				    
					Category category = Category.valueOf(name);
					
					return category;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" return coupon category failed");
		} 	
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	
	

	

}