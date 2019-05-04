package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.Company;
import Utils.DateUtils;
import Utils.JdbcUtils;


/**
 * DB data access object for companies. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Coupon
 */
public class CompaniesDAO implements ICompaniesDAO {
	
	/**
	 * compares input mail and pass to companies DB and returns true if company exists with this combination
	 *
	 * @param  email mail used to search
	 * @param password password used to search
	 * @return		true if company exists in DB
	 */
	
	public boolean isCompanyExists(String email, String password) throws Exception {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			//create SQL statement

			connection =JdbcUtils.getConnection();

			String sql = String.format(
					"SELECT * FROM COMPANIES WHERE EMAIL = ? AND PASSWORD = ?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,email);
			preparedStatement.setString(2,password);
			resultSet = preparedStatement.executeQuery();

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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
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
	public long getCompanyID(String email, String password) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format("SELECT ID FROM COMPANIES WHERE EMAIL = ? AND PASSWORD = ?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1,email);
			preparedStatement.setString(2,password);
			resultSet = preparedStatement.executeQuery();

					
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
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
	public boolean isCompanyExistsByMailOrName(String email, String name) throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;


		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format(
					"SELECT * FROM COMPANIES WHERE EMAIL = ? OR NAME = ?");
			

			preparedStatement = connection.prepareStatement(sql);
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
			JdbcUtils.closeResources(connection, preparedStatement);
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
	
	public long addCompany(Company company) throws  ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection =JdbcUtils.getConnection();

			String sql = String.format("INSERT INTO COMPANIES(COMPANYID, EMAIL, NAME) " + 
					"VALUES(?, ?, ?)");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,company.getCompanyID());
			preparedStatement.setString(2,company.getEmail());
			preparedStatement.setString(3,  company.getName());
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			
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
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	/**
	 *updates an existing company in the DB.
	 *
	 * @param  company the company to be updated
	 * @see 		JavaBeans.Company
	 */
	public void updateCompany(Company company) throws  ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection =JdbcUtils.getConnection();

			String sql = String.format("UPDATE COMPANIES SET EMAIL = ? where companyID= ?");

			preparedStatement = connection.prepareStatement(sql);
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
			JdbcUtils.closeResources(connection, preparedStatement);
		}
	}
	
	 /**removes an existing company from the DB.
	 *
	 * @param  companyID the ID of the company to be removed
	 */

	public void deleteCompany(long companyID) throws ApplicationException  {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format("DELETE FROM COMPANIES WHERE ID=?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,companyID);

			preparedStatement.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" delete company failed");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement);
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
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection =JdbcUtils.getConnection();

			String sql = "SELECT * FROM COMPANIES";

			preparedStatement = connection.prepareStatement(sql);

			resultSet = preparedStatement.executeQuery();

			ArrayList<Company> allCompanies = new ArrayList<Company>();
					
			while(resultSet.next()) {
						Company company = extractCompanyFromResultSet(resultSet);
						allCompanies.add(company);
					}
					
			return allCompanies;
				
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find all companies failed");
		} 
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	
	/**
	 *	returns a company of the specified ID
	 *
	 * @param		companyID long containing the ID of the company to be returned
	 * @return		Company object with the company data of the specified ID.
	 */
	public Company getCompanyByID(long companyID) throws ApplicationException {

		Connection connection = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection =JdbcUtils.getConnection();

			String sql = String.format("SELECT * FROM COMPANIES WHERE ID=?");

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1,companyID);
			resultSet = preparedStatement.executeQuery();
			
				 if(!resultSet.next())
				{
						throw new ApplicationException(ErrorType.INVALID_EMAIL_OR_PASS,"company does not exist!");
				}
				else
				{

					Company company = extractCompanyFromResultSet(resultSet);
					return company;			
				}	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, DateUtils.getCurrentDateAndTime()
					+" find company failed");
		} 
		
		finally {
			JdbcUtils.closeResources(connection, preparedStatement, resultSet);
		}
	}
	
	
	private Company extractCompanyFromResultSet(ResultSet result) throws SQLException {
		Company company = new Company(result.getString("contact_email"), result.getString("comp_name"),result.getLong("comp_id") );


		return company;

	}
}