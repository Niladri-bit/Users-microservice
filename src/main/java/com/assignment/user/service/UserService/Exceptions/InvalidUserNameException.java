package com.assignment.user.service.UserService.Exceptions;

public class InvalidUserNameException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2734199866414984124L;

	public InvalidUserNameException(String message) {
        super(message);
    }

}
