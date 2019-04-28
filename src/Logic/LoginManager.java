package Logic;

import Enums.ClientType;

/**
 * class responsible to return a client facade of type relevant to use logging in.
 *
 * @see         Logic.ClientController
 */
public class LoginManager {

	private static LoginManager instance = new LoginManager();
	
	public static LoginManager getInstance() {
		return instance;
	}
	
	/**
	 * takes the input email\password and pass and tries to use it to login with the apporpriate client type. 
	 * if successful, returns a ClientFacade of the type relevant to the user.
	 *
	 * @param  email mail used to login
	 * @param password password used to login
	 * @param clientType the type of client trying to login and the facade to be returned if successful.
	 * @see 		administratorFacade
	 * @see			customerFacade
	 * @see 		adminFacade
	 * @throws		wrong mail/password!
	 */
	
	public ClientController login(String email, String password, ClientType clientType) {
		switch (clientType) {
		case Administrator:
			AdminController administratorFacade = new AdminController();
			try {
			administratorFacade.login(email, password);
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
			return administratorFacade;
		case Customer:
			CustomerController customerFacade = new CustomerController();
			try {
				customerFacade.login(email, password);
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
			return customerFacade;
		case Company:
			CompanyController companyFacade = new CompanyController();
			try {
				companyFacade.login(email, password);
			}
			catch(Exception Ex){
				 System.out.println(Ex.getMessage());

			}
			return companyFacade;
		}
		return null;
	}
	
}
