package Logic;

/**
 * Parent class from which other facades inherit.
 *
 * @param  password hard-coded admin password for test
 * @param email hard-coded admin email for test
 * @see         DB.CompaniesDAO
 * @see 		DB.CustomerDAO
 * @see			DB.CouponsDAO
 */
public abstract class ClientController {
	
	protected  DB.CompaniesDAO companiesDBDAO= new DB.CompaniesDAO();
	protected  DB.CustomerDAO customerDBDAO= new DB.CustomerDAO();
	protected  DB.CouponsDAO couponsDBDAO= new DB.CouponsDAO();

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
