package com.assignment.user.service.UserService.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.user.service.UserService.DTO.TokenInformationDTO;
import com.assignment.user.service.UserService.Services.ValidationService;

@RestController
public class ValidationController {

	 	
	 	@Autowired
	 	private ValidationService validationService;

	    @PostMapping("/validateToken")
	    public ResponseEntity<TokenInformationDTO> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
	        
	       String token = authorizationHeader.replace("Bearer ", "");
	       TokenInformationDTO tokenInformationDTO = validationService.retriveUserDetailsFromToken(token);
	       return ResponseEntity.ok().body(tokenInformationDTO);
	    }
}
