package Logic;

import java.util.Collection;

import DB.CompaniesDAO;
import Enums.ClientType;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.User;
import JavaBeans.UserData;
import Utils.NameUtils;
import Utils.PasswordUtils;

//import com.avi.coupons.beans.User;
//import com.avi.coupons.beans.UserData;
//import com.avi.coupons.dao.CustomersDAO;
//import com.avi.coupons.dao.UsersDAO;
//import com.avi.coupons.enums.ClientType;
//import com.avi.coupons.enums.ErrorType;
//import com.avi.coupons.exceptions.ApplicationException;
//import com.avi.coupons.utils.IdUtils;
//import com.avi.coupons.utils.NameUtils;
//import com.avi.coupons.utils.PasswordUtils;
//import com.avi.coupons.utils.TypeUtils;

@Controller
public class UsersController {
	
	@Autowired
	private DB.UsersDAO usersDao;
	
	@Autowired
	private DB.CompaniesDAO companiesDAO;
	
	@Autowired
	private ICacheManager cacheManager;

	public UsersController() {
		this.usersDao = new DB.UsersDAO();
	}
	
	public ClientType login(String username, String password) throws ApplicationException {
		ClientType clientType = usersDao.login(username, password);
		
		int token = generateEncryptedToken(username);
		UserData userData = new UserData(username, password, clientType);
		cacheManager.put(token, userData);
		
		return clientType;
	}

	

	private int generateEncryptedToken(String username) {
		String token = "this too" + username + "shall hash";
		
		return token.hashCode();
	}

	public long createUser(User user) throws ApplicationException {
		if (user == null) {
			throw new ApplicationException(ErrorType.EMPTY, ErrorType.EMPTY.getInternalMessage());
		}
		
		NameUtils.isValidName(user.getUserName());
		PasswordUtils.isValidPassword(user.getPassword());

		if (usersDao.getUserByMail(user.getEmail()) != null) {
			throw new ApplicationException(ErrorType.EXISTING_EMAIL, ErrorType.EXISTING_EMAIL.getInternalMessage());
		}
		if ((user.getCompanyId() != 0 && user.getType().equals(ClientType.Customer))) {
			throw new ApplicationException(ErrorType.COMPANY_ID_NOT_TYPE, ErrorType.COMPANY_ID_NOT_TYPE.getInternalMessage());
		}
		if (user.getCompanyId() == 0 && user.getType().equals(ClientType.Company)) {
			throw new ApplicationException(ErrorType.COMPANY_TYPE_NO_ID, ErrorType.COMPANY_TYPE_NO_ID.getInternalMessage());
		}
		return usersDao.createUser(user);
	}

	

	public void updateUser(User user) throws ApplicationException {

		NameUtils.isValidName(user.getEmail());
		PasswordUtils.isValidPassword(user.getPassword());

		if (usersDao.getUserByID(user.getId()) == null) {
			throw new ApplicationException(ErrorType.USER_ID_DOES_NOT_EXIST, ErrorType.USER_ID_DOES_NOT_EXIST.getInternalMessage());
		}
		usersDao.updateUser(user);
	}

	public void deleteUser(long userId) throws ApplicationException {


		if (usersDao.getUserByID(userId) == null) {
			throw new ApplicationException(ErrorType.USER_ID_DOES_NOT_EXIST, ErrorType.USER_ID_DOES_NOT_EXIST.getInternalMessage());
		}
		usersDao.deleteUserByID(userId);

	}

	public void deleteUsersByCompanyId(long companyId) throws ApplicationException {


		if ( companiesDAO.getCompanyByID(companyId) == null) {
			throw new ApplicationException(ErrorType.COMPANY_ID_DOES_NOT_EXIST, ErrorType.COMPANY_ID_DOES_NOT_EXIST.getInternalMessage());
		}
		usersDao.deleteCompanysUsers(companyId);

	}

	public Collection<User> getAllUsers() throws ApplicationException
	{

		return usersDao.getAllUsers();

	}

}
