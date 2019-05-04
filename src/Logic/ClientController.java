package Logic;

import Enums.ClientType;
import Exceptions.ApplicationException;

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
	protected  DB.UsersDAO usersDBDAO= new DB.UsersDAO();
	protected  DB.PurchasesDAO purchasesDBDAO= new DB.PurchasesDAO();


	/**
	 * login function to be implemented by each facade type.
	 *
	 * @param  mail mail used to login
	 * @param pass password used to login
	 * @throws ApplicationException 
	 * @throws wrong mail\password
	 */
	public ClientType login(String email, String password) throws ApplicationException {
		return usersDBDAO.login(email, password);
	}

}
