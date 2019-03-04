package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import JavaBeans.Company;
import JavaBeans.Coupon;


/**
 * DB data access object for companies. 
 *
 * @param  connectionPool connection pool assigned to this instance.
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Coupon
 */
public class CompaniesDBDAO implements CompaniesDAO {
	
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
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT Count(*) AS Count FROM COMPANIES WHERE EMAIL = '%s' AND PASSWORD = '%s'",
					email,password);

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
	 * compares input mail and pass to companies DB and get the selected company ID 
	 * 
	 * @throws company does not exist!
	 * @param  email mail used to search
	 * @param password password used to search
	 * @return	int containing the found companyID
	 */
	public int getCompanyID(String email, String password) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT ID FROM COMPANIES WHERE EMAIL = '%s' AND PASSWORD = '%s'",
					email,password);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					
					if(!resultSet.next())
					{
							throw new Exception("company does not exist!");
					}
					
					int id = resultSet.getInt("ID");

					return id;
							
				}
			
			}
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
	public boolean isCompanyExistsByMailOrName(String email, String name) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format(
					"SELECT Count(*) AS Count FROM COMPANIES WHERE EMAIL = '%s' OR NAME = '%s'",
					email,name);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					if(!resultSet.next())
					{
							throw new Exception("company does not exist!");
					}

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
	 *adds a new company to the DB using the DBDAO.
	 *<p>
	 *this also generates a new sequential ID for the company in the DB and adds it to the input company object.
	 *
	 * @param  company the new company to be added to the DB.
	 * @see 		JavaBeans.Company
	 */
	
	public void addCompany(Company company) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();

			String sql = String.format("INSERT INTO COMPANIES(NAME, EMAIL, PASSWORD) " + 
					"VALUES('%s', '%s', '%s')",
					company.getName(), company.getEmail(), company.getPassword());

			try(PreparedStatement preparedStatement = 
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

				preparedStatement.executeUpdate();

				try(ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
					resultSet.next();
					int id = resultSet.getInt(1);
					company.setId(id); // Add the new created id into the company object.
				}
			}
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
	public void updateCompany(Company company) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();

			String sql = String.format(
					"UPDATE COMPANIES SET , EMAIL='%s', PASSWORD='%s' WHERE ID=%d",
					 company.getEmail(), company.getPassword(), company.getId());

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}
	
	 /**removes an existing company from the DB.
	 *
	 * @param  companyID the ID of the company to be removed
	 */

	public void deleteCompany(int companyID) throws Exception {

		Connection connection = null;

		try {

			connection = connectionPool.getConnection();

			String sql = String.format("DELETE FROM COMPANIES WHERE ID=%d", companyID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.executeUpdate();
			}
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

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					ArrayList<Company> allCompanies = new ArrayList<Company>();
					
					while(resultSet.next()) {

						int id = resultSet.getInt("ID");
						String name = resultSet.getString("NAME");
						String email = resultSet.getString("EMAIL");
						String password = resultSet.getString("PASSWORD");
						ArrayList<Coupon> coupons = getCouponsByCompanyID(id);
	
						Company company = new Company(name, email, password, id,coupons);
						
						allCompanies.add(company);
					}
					
					return allCompanies;
				}
			}
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
	
	public ArrayList<Coupon> getCouponsByCompanyID(int CompanyID) throws Exception {
		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM Coupons WHERE COMPANY_ID=%d", CompanyID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					ArrayList<Coupon> allCoupons = new ArrayList<Coupon>();
					
					while(resultSet.next()) {
						 String description = resultSet.getString("DESCRIPTION");
					     String image = resultSet.getString("IMAGE");
					     String title = resultSet.getString("TITLE");
					     int id = resultSet.getInt("ID");
					     int amount = resultSet.getInt("AMOUNT");
					     LocalDate start_date = resultSet.getDate("START_DATE").toLocalDate();
					     LocalDate end_date = resultSet.getDate("END_DATE").toLocalDate();
					     int company_id = resultSet.getInt("COMPANY_ID");
					     int category_id = resultSet.getInt("CATEGORY_ID");
					     double price = resultSet.getDouble("PRICE");
						
	
						Coupon Coupon = new Coupon(description, image, title, id,amount,start_date,end_date,company_id, category_id,price);
						
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
	 *	returns a company of the specified ID
	 *
	 * @param		companyID int containing the ID of the company to be returned
	 * @return		Company object with the company data of the specified ID.
	 */
	public Company getOneCompany(int companyID) throws Exception {

		Connection connection = null;

		try {
			connection = connectionPool.getConnection();

			String sql = String.format("SELECT * FROM COMPANIES WHERE ID=%d", companyID);

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				try(ResultSet resultSet = preparedStatement.executeQuery()) {

					resultSet.next();

					String name = resultSet.getString("NAME");
					String email = resultSet.getString("EMAIL");
					String password = resultSet.getString("PASSWORD");
					ArrayList<Coupon> coupons = getCouponsByCompanyID(companyID);

					Company company = new Company(name, email, password,companyID, coupons);

					return company;
				}
			}
		}
		finally {
			connectionPool.restoreConnection(connection);
		}
	}

}