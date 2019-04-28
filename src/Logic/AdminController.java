package Logic;

import java.util.ArrayList;

import JavaBeans.Company;
import JavaBeans.Customer;


/**
 * object returned when user logs in as admin. in charge of login and DBDAO actions for admins. 
 *
 * @param  password hard-coded admin password for test
 * @param email hard-coded admin email for test
 * @see         JavaBeans.Company
 * @see 		JavaBeans.Customer
 */

public class AdminController extends ClientController{

	
	private String password = "admin";
	private String email = "admin@admin.com";
	
	/**
	 * compares input mail and pass to hard coded parameters and returns error when fails
	 *
	 * @param  mail mail used to login
	 * @param pass password used to login
	 * @throws wrong mail\password
	 */
	
	public void login(String mail, String pass) {
		try {
			if (pass!=password || mail!=email) {
				throw new Exception("wrong mail/password");
			}
			
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
	} 
	/**
	 *adds a new company to the DB using the DBDAO.
	 *
	 * @param  company the new company to be added to the DB.
	 * @see 		DB.companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @throws company already exists!
	 */
	public void addCompany(JavaBeans.Company company) throws Exception{
		try {
		if (companiesDBDAO.isCompanyExistsByMailOrName(company.getEmail(), company.getName()))
				{
			throw new Exception("Company with this Email or name already exists");
			
		}
		else {
			
			companiesDBDAO.addCompany(company);
		}
	}
		catch(Exception Ex){
			 System.out.println(Ex.getMessage());

		}
	}
	/**
	 *updates an existing company in the DB using the DBDAO.
	 *
	 * @param  company the company to be updates
	 * @see 		companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @throws 		company does not exist!
	 * @throws 		company name cannot be updated!
	 */
	public void updateCompany(JavaBeans.Company company) {
		try {
			
				if (!companiesDBDAO.isCompanyExists(company.getEmail(), company.getName())) {
					throw new Exception("Company does not exist!");
				}
				
			if (companiesDBDAO.getCompanyByID(company.getId()).getName()!= company.getName()) {
				throw new Exception("Company name cannot be changed!");
			}
			else {
				companiesDBDAO.updateCompany(company);
			}
			
		}
		catch(Exception Ex) {
			 System.out.println(Ex.getMessage());

		}
	}
	
	/**
	 *removes an existing company from the DB using the DBDAO.
	 *<P>
	 *this also removes any coupons belonging to the company.
	 *
	 * @param  company the company to be removed
	 * @see 		companiesDBDAO
	 * @see 		JavaBeans.Company
	 * @throws 		company does not exist!
	 */
		public void deleteCompany(JavaBeans.Company company) {
			try {
				if (!companiesDBDAO.isCompanyExists(company.getEmail(), company.getName())) {
					throw new Exception("Company does not exist!");
				}
				//remove company from DB
				companiesDBDAO.deleteCompany(company.getId());
				
				//find company coupons
				ArrayList<JavaBeans.Coupon> coupons = companiesDBDAO.getCouponsByCompanyID(company.getId());
				
				//remove all company coupons and customer purchases
				for (JavaBeans.Coupon c : coupons) {
					couponsDBDAO.deleteCoupon(c.getId());
					couponsDBDAO.deleteCouponPurchase(c.getId(), -1);
				}
				
				
				
			}
			catch(Exception Ex) {
				 System.out.println(Ex.getMessage());

			}
		}
		
		/**
		 *	returns an ArrayList of Company objects with all companies using the DBDAO.
		 *
		 * @see 		companiesDBDAO
		 * @see 		JavaBeans.Company
		 * @return		ArrayList of all companies
		 */
		public ArrayList<Company> getAllCompanies() throws Exception{
			return companiesDBDAO.getAllCompanies();
		}
		
		/**
		 *	returns a company of the specified ID
		 *
		 * @param		companyID int containing the ID of the company to be returned
		 * @see 		companiesDBDAO
		 * @see 		JavaBeans.Company
		 * @return		Company object with the company data of the specified ID.
		 */
		public Company getCompany(int id) throws Exception{
			return companiesDBDAO.getCompanyByID(id);
		}
		/**
		 *adds a new customer to the DB.
		 *
		 * @param  customer the new customer to be added to the DB.
		 * @see 		customersDBDAO
		 * @see 		JavaBeans.customer
		 * @throws customer already exists!
		 */
		public void addCustomer(Customer customer) {
			try {
				if (customerDBDAO.isCustomerExistsByMail(customer.getEmail())){
					throw new Exception("Customer with this email already exists!");
				}
				customerDBDAO.addCustomer(customer);
			
			}
			catch(Exception Ex) {
				 System.out.println(Ex.getMessage());

			}
			
		}
		
		/**
		 *updates an existing customer in the DB.
		 *
		 * @param  customer the customer data to be updates in the DB.
		 * @see 		customersDBDAO
		 * @see 		JavaBeans.Customer
		 */
		public void updateCustomer(Customer customer) throws Exception{
			customerDBDAO.updateCustomer(customer);
		}
		/**
		 *removes an existing customer from the DB.
		 *<p>
		 *also deletes all coupons belonging to the customer
		 *
		 * @param  customer the customer data to be removed from the DB.
		 * @see 		customersDBDAO
		 * @see 		couponsDBDAO
		 * @see 		JavaBeans.Coupons
		 * @see 		JavaBeans.Customer
		 */
		public void deleteCustomer(Customer customer) throws Exception{
			customerDBDAO.deleteCustomer(customer.getId());
			
			couponsDBDAO.deleteCouponPurchase(-1, customer.getId());
		}
		/**
		 * returns a list of all customers
		 * 
		 * @see 		customersDBDAO
		 * @see			JavaBeans.Customer
		 * @return ArrayList of all Customers
		 */
		public ArrayList<Customer> getAllCustomers() throws Exception{
			return customerDBDAO.getAllCustomers();
		}
		/**
		 * returns the DB data of any customer by ID as a company object.
		 * 
		 * @see 		customersDBDAO
		 * @see			JavaBeans.Customer
		 * @return		Company object with the specified company data.
		 */
		public Customer getCustomer(int id) throws Exception{
			return customerDBDAO.getOneCustomer(id);
		}
 	}

