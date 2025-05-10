package com.assignment.user.service.UserService.Controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assignment.user.service.UserService.DTO.StudentRegisterDTO;
import com.assignment.user.service.UserService.DTO.UserDTO;
import com.assignment.user.service.UserService.DTO.UserLoginDTO;
import com.assignment.user.service.UserService.Services.UserService;
import com.assignment.user.service.UserService.utils.JwtUtil;
import com.assignment.user.service.UserService.utils.Util;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Tag(name = "User APIs", description = "Handles all student and user operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Register a new student", description = "Creates a new student with default STUDENT role")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid username")
    })
    @PostMapping(path = "/students")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody StudentRegisterDTO studentRegisterDTO,@RequestHeader("Authorization") String authorizationHeader) {
        UserDTO createdUser = userService.createStudent(studentRegisterDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Get student by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping(path = "/students/{userID}")
    public ResponseEntity<StudentRegisterDTO> getUserId(@PathVariable Long userID,@RequestHeader("Authorization") String authorizationHeader) {
        StudentRegisterDTO user = userService.getUserById(userID);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update a student")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentRegisterDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRegisterDTO studentDto,@RequestHeader("Authorization") String authorizationHeader) {

        StudentRegisterDTO updatedStudent = userService.updateStudent(id, studentDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(summary = "Get all students (optionally by class)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    @GetMapping(path = "/students")
    public ResponseEntity<List<StudentRegisterDTO>> getAllUsers(
            @RequestParam(value = "studentClass", required = false) String studentClass,@RequestHeader("Authorization") String authorizationHeader) {

        List<String> classList = studentClass != null
                ? Arrays.asList(studentClass.split(","))
                : null;

        List<StudentRegisterDTO> users = userService.getAllUsers(classList);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Delete student by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping(path = "/students/{userID}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userID,@RequestHeader("Authorization") String authorizationHeader) {
        userService.deleteUserById(userID);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @Operation(summary = "Register a admin")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(path = "/auth/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Login user", description = "Authenticate and receive JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO loginRequest) {
        boolean isAuthenticated = userService.authenticateUser(loginRequest.getUserName(), loginRequest.getPassword());
        if (isAuthenticated) {
            String jwt = jwtUtil.generateToken(loginRequest.getUserName());
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Operation(summary = "Assign roles to a user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid roles provided"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<String> assignRoles(@PathVariable Long userId, @RequestBody Set<String> roles,@RequestHeader("Authorization") String authorizationHeader) {
        userService.assignRolesToUser(userId, Util.validateRoles(roles));
        return new ResponseEntity<>("Roles assigned successfully", HttpStatus.OK);
    }

}
