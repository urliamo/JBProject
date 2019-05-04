package Utils;

import Enums.ErrorType;
import Exceptions.ApplicationException;

public class IdUtils {

	/**
	 * @param id Receive an id
	 * @return This function return true if id is valid or throw exception if didn't
	 * @throws ApplicationException throw exception by name
	 */
	public static void isValidId(long id) throws ApplicationException {

		if (id < 1)
			throw new ApplicationException(ErrorType.INVALID_ID.getMessage());

	}
	public static void isIdCompatible(long customerId, long userId) throws ApplicationException {
		
		if (customerId!=userId) {
			System.out.println("customer Id = " + customerId + "; user Id = " + userId);
			throw new ApplicationException(ErrorType.INVALID_ID_UNCOMPATIBLE.getMessage());
		}
	}
}
