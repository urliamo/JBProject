package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Company;
import JavaBeans.Coupon;
import Utils.DateUtils;


/**
 * DB data access object for companies. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Coupon
 */
public class CompaniesDAO implements ICompaniesDAO {
	
	private ConnectionPool connectionPool = ConnectionPool.getInstance();
	/**
	 * compares input mail and pass to companies DB and returns true if company exists with this combination
	 *
	 * @param  email mail used to search
	 * @param password password used to search
	 * @return		true if company exists in DB
	 */
	public boolean isCompanyExists(String email, String password) throws Exception {

		Connection connection = null;

		try {
			//create SQL statement

			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT * FROM COMPANIES WHERE EMAIL = ? AND PASSWORD = ?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,email);
			preparedStatement.setString(2,password);
				ResultSet resultSet = preparedStatement.executeQuery();

				if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
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
					+" check company exists failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	/**
	 * compares input mail and pass to companies DB and get the selected company ID 
	 * 
	 * @throws company does not exist!
	 * @param  email mail used to search
	 * @param password password used to search
	 * @return	int containing the found companyID
	 * @throws InterruptedException 
	 */
	public long getCompanyID(String email, String password) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT ID FROM COMPANIES WHERE EMAIL = ? AND PASSWORD = ?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1,email);
				preparedStatement.setString(2,password);
				ResultSet resultSet = preparedStatement.executeQuery();

					
					if(!resultSet.next())
					{
							throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
					}
					
					long id = resultSet.getLong("CompanyID");

					return id;
							
				
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" get company failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	/**
	 * compares input mail and name to companies DB and returns true if company exists with either combination
	 * 
	 * @throws company does not exist!	 
	 * @param  email mail belonging to the company
	 * @param name name of company to be searched 
	 * @return		true if company exists in DB
	 */
	public boolean isCompanyExistsByMailOrName(String email, String name) throws ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT * FROM COMPANIES WHERE EMAIL = ? OR NAME = ?");
			

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,email);
			preparedStatement.setString(2,name);
				ResultSet resultSet = preparedStatement.executeQuery();

					if(!resultSet.next())
					{
							throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
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
					+" get company failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	
	/**
	 *adds a new company to the DB using the DBDAO.
	 *<p>
	 *this also generates a new sequential ID for the company in the DB and adds it to the input company object.
	 *
	 * @param  company the new company to be added to the DB.
	 * @see 		JavaBeans.Company
	 */
	
	public long addCompany(Company company) throws  ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			//create SQL statement

			connection = connectionPool.getConnection();

			String sql = String.format("INSERT INTO COMPANIES(COMPANYID, EMAIL, NAME) " + 
					"VALUES(?, ?, ?)");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,company.getCompanyID());
			preparedStatement.setString(2,company.getEmail());
			preparedStatement.setString(3,  company.getName());
				preparedStatement.executeUpdate();

				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					long id = resultSet.getLong(1);
					return id;
				}
				else
				{
				throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to create company id");
				}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" add company failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	/**
	 *updates an existing company in the DB.
	 *
	 * @param  company the company to be updated
	 * @see 		JavaBeans.Company
	 */
	public void updateCompany(Company company) throws  ApplicationException, InterruptedException {

		Connection connection = null;

		try {
			//create SQL statement

			connection = connectionPool.getConnection();

			String sql = String.format("UPDATE COMPANIES SET EMAIL = ? where companyID= ?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,company.getEmail());
			preparedStatement.setLong(2,company.getCompanyID());
				preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" update company failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	 /**removes an existing company from the DB.
	 *
	 * @param  companyID the ID of the company to be removed
	 */

	public void deleteCompany(long companyID) throws ApplicationException, InterruptedException  {

		Connection connection = null;
		try {
			//create SQL statement

			connection = connectionPool.getConnection();

			String sql = String.format("DELETE FROM COMPANIES WHERE ID=?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,companyID);

			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" delete company failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

	/**
	 *	returns an ArrayList of Company objects with all companies.
	 *
	 * @see 		companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @return		ArrayList of all companies
	 */
	public ArrayList<Company> getAllCompanies() throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = "SELECT * FROM COMPANIES";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

				ResultSet resultSet = preparedStatement.executeQuery();

					ArrayList<Company> allCompanies = new ArrayList<Company>();
					
					while(resultSet.next()) {

						long id = resultSet.getLong("companyID");
						String name = resultSet.getString("NAME");
						String email = resultSet.getString("EMAIL");
						String password = resultSet.getString("PASSWORD");
						ArrayList<Coupon> coupons = getCouponsByCompanyID(id);
	
						Company company = new Company(name, email, password, id,coupons);
						
						allCompanies.add(company);
					}
					
					return allCompanies;
				
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find all companies failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	/**
	 * returns all coupons belonging to company with specified ID.
	 * 
	 * @param  companyID the ID of the company whose coupons are to be returned
	 * @return ArrayList of all coupon objects belonging to company with specified ID
	 */
	
	public ArrayList<Coupon> getCouponsByCompanyID(long companyID) throws Exception {
		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM Coupons WHERE COMPANY_ID=?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,companyID);
			ResultSet resultSet = preparedStatement.executeQuery();

			ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
			while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     long coupon_id = resultSet.getLong("couponID");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate start_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     long company_id = resultSet.getLong("COMPANYID");
					     int category_id = resultSet.getInt("CATEGORYID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, coupon_id,amount,start_date,end_date,company_id, category_id,price);
						
						allCoupons.add(Coupon);
					}
					
					return allCoupons;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find all company coupons failed");
		} 
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	/**
	 *	returns a company of the specified ID
	 *
	 * @param		companyID int containing the ID of the company to be returned
	 * @return		Company object with the company data of the specified ID.
	 */
	public Company getCompanyByID(long companyID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM COMPANIES WHERE ID=?");

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setLong(1,companyID);

				ResultSet resultSet = preparedStatement.executeQuery();
				if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
				}
				else
				{

					String name = resultSet.getString("NAME");
					String email = resultSet.getString("EMAIL");
					String password = resultSet.getString("PASSWORD");
					ArrayList<Coupon> coupons = getCouponsByCompanyID(companyID);

					Company company = new Company(name, email, password,companyID, coupons);

					return company;
				
		}	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			//If there was an exception in the "try" block above, it is caught here and notifies a level above.
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find company failed");
		} 
		
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

}