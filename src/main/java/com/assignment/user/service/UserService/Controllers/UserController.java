package com.assignment.user.service.UserService.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.user.service.UserService.DTO.UserDTO;
import com.assignment.user.service.UserService.DTO.UserLoginDTO;
import com.assignment.user.service.UserService.Services.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping(path = "/users/{userID}")
	public ResponseEntity<UserDTO> getUserId(@PathVariable Long userID) {
		UserDTO user = userService.getUserById(userID);
		return ResponseEntity.ok(user);
	}
	
	 @GetMapping(path = "/users")
	    public ResponseEntity<List<UserDTO>> getAllUsers() {
	       List<UserDTO> users = userService.getAllUsers(); 
	       return ResponseEntity.ok(users);
	    }
	 
	 @DeleteMapping(path = "/users/{userID}")
	    public ResponseEntity<String> deleteUser(@PathVariable Long userID) {
	        userService.deleteUserById(userID);
	        return ResponseEntity.ok("User deleted successfully.");
	    }
	
	@PostMapping(path = "/users/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
	
	@PostMapping(path = "/users/login")
	public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO loginRequest) {
	    boolean isAuthenticated = userService.authenticateUser(loginRequest.getUserName(), loginRequest.getPassword());
	    
	    if (isAuthenticated) {
	        return ResponseEntity.ok("Login successful");
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    }
	}
}
