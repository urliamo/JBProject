package com.avi.coupons.utils;

import java.util.regex.Pattern;

import com.avi.coupons.enums.ErrorType;
import com.avi.coupons.exceptions.CouponsProjectExceptions;

public class EmailUtils {

	/**
	 * @param email This function receive a email and check if valid (by some
	 *              definitions)
	 * @throws ApplicationException Throw an exception by name
	 */
	public static void isValidEmail(String email) throws CouponsProjectExceptions {
			Pattern ptr = Pattern.compile("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$");
					if (!ptr.matcher(email).matches())
						throw new CouponsProjectExceptions(ErrorType.INVALID_EMAIL.getMessage());
		
		if (email == null || email.isEmpty())
			throw new CouponsProjectExceptions(ErrorType.EMPTY.getMessage());
	}

}
