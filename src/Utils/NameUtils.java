package Utils;

import Enums.ErrorType;
import Exceptions.ApplicationException;

public class NameUtils {

	/**
	 * @param name This function checks String name as valid
	 * @throws ApplicationException Throw an exception by name
	 */
	public static void isValidName(String name) throws ApplicationException {

		if (name == null || name.isEmpty())
			throw new ApplicationException(ErrorType.EMPTY, ErrorType.EMPTY.getInternalMessage());

		if (name.length() < 2)
			throw new ApplicationException(ErrorType.INVALID_NAME,ErrorType.INVALID_NAME.getInternalMessage());

	}
}
