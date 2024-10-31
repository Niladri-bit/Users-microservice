package com.assignment.user.service.UserService.Exceptions;

public class InvalidRoleAssignmentException extends RuntimeException{

	

    /**
	 * 
	 */
	private static final long serialVersionUID = 7219815769924992937L;

	public InvalidRoleAssignmentException(String message) {
        super(message);
    }
}
