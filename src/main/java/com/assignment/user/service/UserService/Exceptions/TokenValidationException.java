package com.assignment.user.service.UserService.Exceptions;

public class TokenValidationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7191831110450703478L;

	public TokenValidationException(String message) {
        super(message);
    }
}
