package com.assignment.user.service.UserService.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.assignment.user.service.UserService.Exceptions.InvalidRoleAssignmentException;
import com.assignment.user.service.UserService.utils.enumerations.Role;

public class Util {

	public static boolean isValidRole(Role role) {
	    return Arrays.stream(Role.values()).anyMatch(r -> r.name().equals(role.toString()));
	}
	
	public static Set<Role> validateRoles(Set<String> roles){
		Set<Role> validatedRoles = new HashSet<>();
		{
			for (String roleStr : roles) {
				try {
					validatedRoles.add(Role.valueOf(roleStr.toUpperCase())); 
				} catch (IllegalArgumentException e) {
					throw new InvalidRoleAssignmentException("Invalid role: " + roleStr);
				}
			}
		}
		return validatedRoles;
	}
	
	
	
}
