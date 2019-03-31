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

public class PurchasesDAO {

	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	/**
	 *  returns true if coupon  with specified ID belonging to customer with specified ID exists
	 *
	 * @param  coupondId the ID of the coupon to be searched
	 * @param  customerId the ID of the customer to be searched
	 * @return		true if coupon belongs to customer
	 */
	
	public boolean isCouponPurchaseExists(long customerID,long couponID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT *  FROM purchases WHERE CUSTOMERID = ? AND COUPONID = ?",
					customerID,couponID);

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

				ResultSet resultSet = preparedStatement.executeQuery();

				if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_ID,"purchase does not exist!");
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
					+" check purchase exists failed");
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
				 sql = String.format("DELETE FROM PURCHASES WHERE COUPON_ID=?", couponID);

			}
			//set string for all coupons of customer
			else if(couponID==-1) {
				 sql = String.format("DELETE FROM PURCHASES WHERE CUSTOMER_ID=?", customerID); 
			}
			//set string for single customer coupon
			else {
			 sql = String.format("DELETE FROM PURCHASES WHERE CUSTOMER_ID=? AND COUPON_ID=?", customerID, couponID);
			}
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			if (customerID==-1)
			{
				preparedStatement.setLong(1,couponID);

			}
			//set string for all coupons of customer
			else if(couponID==-1) {
				preparedStatement.setLong(1,customerID);
			}
			//set string for single customer coupon
			else {
				preparedStatement.setLong(1,customerID);
				preparedStatement.setLong(2,couponID);

			}

				preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" delete purchases failed");
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
	public long addCouponPurchase(long CouponID, long CustomerID, int amount) throws Exception {
		Connection connection = null;

		try {

			connection = connectionPool.getConnection();
			
			String sql = String.format("INSERT INTO CUSTOMERS_VS_COUPONS(CUSTOMER_ID, COUPON_ID, AMOUNT) " + 
					"VALUES(?, ?, ?)");

			PreparedStatement preparedStatement = 
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
				preparedStatement.setLong(1,CustomerID);
				preparedStatement.setLong(2,CouponID);
				preparedStatement.setInt(3,amount);

				preparedStatement.executeUpdate();
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					long id = resultSet.getLong(1);
					
					return id;
				}
				else
				{
				throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to create purchase id");
				}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" add purchase failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
		
	}
}
