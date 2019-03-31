package Exceptions;

import Enums.ErrorType;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;
	private ErrorType errorType;

//	public ApplicationException(Exception e) {
//		super(e);
//	}

//	public ApplicationException(Exception e, String message) {
//		super(message, e);
//	}

	public ApplicationException(Exception innerException, ErrorType errorType, String message) {
		super(message, innerException);
		this.errorType=errorType;
	}

	public ApplicationException(ErrorType errorType, String message) {
		super(message);
		this.errorType=errorType;
	}
	
	public ErrorType getErrorType(){
		return this.errorType;
	}
}

