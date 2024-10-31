package com.assignment.user.service.UserService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.user.service.UserService.DTO.TokenInformationDTO;
import com.assignment.user.service.UserService.Exceptions.TokenValidationException;
import com.assignment.user.service.UserService.entities.UserEntity;
import com.assignment.user.service.UserService.repository.UserRepository;
import com.assignment.user.service.UserService.utils.JwtUtil;

@Service
public class ValidationService {
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;
	
	public TokenInformationDTO retriveUserDetailsFromToken(String token) {
		 try {
	            String username = jwtUtil.extractUsername(token);
	            if (jwtUtil.validateToken(token, username)) {
	            	UserEntity userEntity = userRepository.findByUserName(username);
	            	TokenInformationDTO tokenInformationDTO = new TokenInformationDTO(userEntity.getId(),userEntity.getEmail(),username);
	                return tokenInformationDTO;
	            } else {
	                throw new TokenValidationException("Invalid token");
	            }
	        } catch (Exception e) {
	        	throw new TokenValidationException("Token validation failed");
	        }
	}

}
