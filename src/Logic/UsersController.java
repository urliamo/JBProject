package Logic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.avi.coupons.utils.IdUtils;

import Enums.ClientType;
import Enums.ErrorType;
import Exceptions.ApplicationException;
import JavaBeans.User;
import JavaBeans.UserData;

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
		if (user == null)
			throw new ApplicationException(ErrorType.EMPTY.getMessage());

		NameUtils.isValidName(user.getEmail());
		PasswordUtils.isValidPassword(user.getPassword());
		TypeUtils.isValidType(user.getType());

		if (usersDao.getUserByUserEmail(user.getEmail()) != null) {
			throw new ApplicationException(ErrorType.NAME_IS_ALREADY_EXISTS.getMessage());
		}
		if ((user.getCompanyId() != 0 && user.getType().equals(ClientType.CUSTOMER))) {
			throw new ApplicationException(ErrorType.USER_IS_CUSTOMER.getMessage());
		}
		if (user.getCompanyId() == 0 && user.getType().equals(ClientType.COMPANY)) {
			throw new ApplicationException(ErrorType.USER_IS_COMPNAY.getMessage());
		}
		return usersDao.createUser(user);
	}

	

	public void updateUser(User user) throws ApplicationException {

		IdUtils.isValidId(user.getId());
		NameUtils.isValidName(user.getEmail());
		PasswordUtils.isValidPassword(user.getPassword());
		TypeUtils.isValidType(user.getType());

		if (usersDao.(user.getId()) == null) {
			throw new ApplicationException(ErrorType.USER_IS_NOT_EXISTS.getMessage());
		}
		usersDao.updateUser(user);
	}

	public void deleteUser(long userId) throws ApplicationException {

		IdUtils.isValidId(userId);

		if (usersDao.getUserByUserID(userId) == null) {
			throw new ApplicationException(ErrorType.USER_IS_NOT_EXISTS.getMessage());
		}
		usersDao.deleteUser(userId);

	}

	public void deleteUserByCompanyId(long companyId) throws ApplicationException {

		IdUtils.isValidId(companyId);

		if (usersDao.getUserByUserID(companyId) == null) {
			throw new ApplicationException(ErrorType.USER_IS_NOT_EXISTS.getMessage());
		}
		usersDao.deleteCompanysUsers(companyId);

	}

	public Collection<User> getAllUsers() throws ApplicationException
	{

		return usersDao.getAllUsers();

	}

}
