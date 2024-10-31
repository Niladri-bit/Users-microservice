package com.assignment.user.service.UserService.Controllers;

import java.util.List;
import java.util.Set;

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
import com.assignment.user.service.UserService.utils.JwtUtil;
import com.assignment.user.service.UserService.utils.Util;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	

    @Autowired
    private JwtUtil jwtUtil;


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
	
	@PostMapping("/users/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO loginRequest) {
        boolean isAuthenticated = userService.authenticateUser(loginRequest.getUserName(), loginRequest.getPassword());
        if (isAuthenticated) {
            String jwt = jwtUtil.generateToken(loginRequest.getUserName());
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
	
	@PostMapping("/users/{userId}/roles")
	public ResponseEntity<String> assignRoles(@PathVariable Long userId, @RequestBody Set<String> roles) {
		userService.assignRolesToUser(userId, Util.validateRoles(roles));
	    return new ResponseEntity<>("Roles assigned successfully", HttpStatus.OK);
	}
	
	

}
