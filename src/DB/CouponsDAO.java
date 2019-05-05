package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale.Category;

import Enums.Categories;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Coupon;
import Utils.DateUtils;
import Utils.JdbcUtils;


/**
 * DB data access object for coupons. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see 		JavaBeans.Coupon
 */public class CouponsDAO implements ICouponsDAO {
	
	
	
	/**
	 *  returns true if coupon exists with this ID
	 *
	 * @param  id the ID of the coupon to be searched
	 * @return		true if coupon exists in DB
	 */
	
	public boolean isCouponExists(long id) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format(
					"SELECT * AS Count FROM Coupons WHERE couponID = ?'");
	
			preparedStatement = connection.prepareStatement(sql);
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
			JdbcUtils.closeResources(connection, preparedStatement);
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
	
	public long addCoupon(Coupon Coupon) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection =JdbcUtils.getConnection();
			
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

			preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,Coupon.getDescription());
			preparedStatement.setString(2,Coupon.getImage());
			preparedStatement.setString(3, Coupon.getTitle());
			preparedStatement.setInt(4,Coupon.getAmount());
			preparedStatement.setDate(5,java.sql.Date.valueOf(Coupon.getStart_date()));
			preparedStatement.setDate(6,java.sql.Date.valueOf(Coupon.getEnd_date()));
			preparedStatement.setLong(7, Coupon.getCompany_id());
			preparedStatement.setString(8,Coupon.getCategory().toString());
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
				JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	
	/**
	 * updates an existing coupon in the DB.
	 *
	 * @param  coupon the  coupon to be added updated in the DB
	 * @see			JavaBeans.Coupon
	 */
	public void updateCoupon(Coupon Coupon) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			
			connection =JdbcUtils.getConnection();

			String sql = String.format(
					"UPDATE Coupons SET DESCRIPTION=?, IMAGE=?, TITLE=?, AMOUNT=?, START_DATE=?, END_DATE=?, CATEGORY_ID=?, PRICE=? WHERE couponID=?");
			
			
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1,Coupon.getDescription());
			preparedStatement.setString(2,Coupon.getImage());
			preparedStatement.setString(3, Coupon.getTitle());
			preparedStatement.setInt(4,Coupon.getAmount());
			preparedStatement.setDate(5,java.sql.Date.valueOf(Coupon.getStart_date()));
			preparedStatement.setDate(6,java.sql.Date.valueOf(Coupon.getEnd_date()));
			preparedStatement.setString(7,Coupon.getCategory().toString());
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
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}

	public void changeCouponAmount(long couponId, int amount) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection =JdbcUtils.getConnection();

			String sql = String.format(
					"UPDATE Coupons SET amount = amount-? where couponID = ?");

			preparedStatement = connection.prepareStatement(sql);	
			preparedStatement.setInt(1,amount);
			preparedStatement.setLong(2,couponId);
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
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	/**
	 * removes a coupon from the DB.
	 * 
	 * @param  couponID the Id of the coupon to be removed from the DB
	 * @see			JavaBeans.Coupon
	 */
	public void deleteCoupon(long CouponID) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {

			connection =JdbcUtils.getConnection();

			String sql = String.format("DELETE FROM Coupons WHERE couponID=?");

			preparedStatement = connection.prepareStatement(sql);
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
			JdbcUtils.closeResources(connection, preparedStatement);
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
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection =JdbcUtils.getConnection();

			String sql = "SELECT * FROM Coupons";

			preparedStatement = connection.prepareStatement(sql);

			resultSet = preparedStatement.executeQuery();

					ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						Coupon Coupon = extractCouponFromResultSet(resultSet);	
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * returns all expired coupons as a list.
	 * 
	 * @see			JavaBeans.Coupon
	 * @return ArrayList of all expired coupons as coupon objects.
	 */
	public ArrayList<Coupon> getExpiredCoupons() throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection =JdbcUtils.getConnection();

			String sql = "SELECT * FROM Coupons where end_date <"+java.sql.Date.valueOf(LocalDate.now());

			preparedStatement = connection.prepareStatement(sql);

			resultSet = preparedStatement.executeQuery();

			ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						
						Coupon Coupon = extractCouponFromResultSet(resultSet);	
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * returns coupon with specified ID.
	 * 
	 * @param		couponID the ID of the coupon to be returned.
	 * @return 		coupon object with the data of the coupon with specified ID.
	 */
	
	public Coupon getOneCoupon(long CouponID) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format("SELECT * FROM Coupons WHERE ID=?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,CouponID);

				resultSet = preparedStatement.executeQuery();
				if (!resultSet.next())
				{
					throw new ApplicationException(ErrorType.INVALID_ID,"coupon does not exist!");
				}

					
					Coupon Coupon = extractCouponFromResultSet(resultSet);
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * returns the category of the coupon with the specified ID.
	 * 
	 * @see			JavaBeans.Coupon
	 * @param		couponID the ID of the coupon to be returned.
	 * @return 		ID of the category of the coupon with specified ID.
	 */
	
	public Category getCouponCategory(int categoryID) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format("SELECT * FROM categories WHERE ID=?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,categoryID);

			resultSet = preparedStatement.executeQuery();

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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	public Collection<Coupon> getAllCouponsByCustomer(long customerID) throws ApplicationException, InterruptedException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection =JdbcUtils.getConnection();
			String sql = String.format("SELECT * FROM Coupons WHERE coupon_id =(SELECT coupon_id FROM Purchases WHERE customer_id=?)");
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,customerID);

			resultSet = preparedStatement.executeQuery();

					Collection<Coupon> customerCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						
						Coupon coupon = extractCouponFromResultSet(resultSet);

						customerCoupons.add(coupon);
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	/**
	 * returns all coupons belonging to company with specified ID.
	 * 
	 * @param  companyID the ID of the company whose coupons are to be returned
	 * @return ArrayList of all coupon objects belonging to company with specified ID
	 */
	
	
	public Collection<Coupon> getCompanyCoupons(long companyID) throws ApplicationException, InterruptedException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection =JdbcUtils.getConnection();
			String sql = String.format("SELECT * FROM Coupons WHERE company_id=?");
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,companyID);

			resultSet = preparedStatement.executeQuery();

					Collection<Coupon> customerCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						
						Coupon coupon = extractCouponFromResultSet(resultSet);

						customerCoupons.add(coupon);
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	public Collection<Coupon> getCompanyCouponsByTitle(long companyID, String title) throws ApplicationException, InterruptedException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection =JdbcUtils.getConnection();
			String sql = String.format("SELECT * FROM Coupons WHERE company_id=? && title=?");
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,companyID);
			preparedStatement.setString(2,title);


			resultSet = preparedStatement.executeQuery();

					Collection<Coupon> customerCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						
						Coupon coupon = extractCouponFromResultSet(resultSet);

						customerCoupons.add(coupon);
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
private Coupon extractCouponFromResultSet(ResultSet result) throws SQLException  {
		
		Coupon coupon = new Coupon(result.getString("description"),result.getString("image"), result.getString("title"),result.getLong("coup_id"), result.getInt("amount"), result.getDate("start_date").toLocalDate(), result.getDate("end_date").toLocalDate(), result.getLong("comp_id"), Categories.valueOf(result.getString("category")), result.getDouble("price"));
	
		return coupon;
	}
	
	

	

}