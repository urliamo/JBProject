package Clients;

/**
 * Parent class from which other facades inherit.
 *
 * @param  password hard-coded admin password for test
 * @param email hard-coded admin email for test
 * @see         DB.CompaniesDBDAO
 * @see 		DB.CustomerDBDAO
 * @see			DB.CouponsDBDAO
 */
public abstract class ClientFacade {
	
	protected  DB.CompaniesDBDAO companiesDBDAO= new DB.CompaniesDBDAO();
	protected  DB.CustomerDBDAO customerDBDAO= new DB.CustomerDBDAO();
	protected  DB.CouponsDBDAO couponsDBDAO= new DB.CouponsDBDAO();

	/**
	 * login function to be implemented by each facade type.
	 *
	 * @param  mail mail used to login
	 * @param pass password used to login
	 * @throws wrong mail\password
	 */
	public void login(String email, String password) {
		
	}

}
