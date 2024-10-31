package com.assignment.user.service.UserService.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.assignment.user.service.UserService.utils.enumerations.Role;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users", schema = "userdb")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    
    @Column(unique = true)
    private String userName;
    
    private String password; 
    private String firstName;
    private String lastName;
    private String address;
    private LocalDateTime dateOfJoining;
    
    @ElementCollection(fetch = FetchType.EAGER) // This indicates a collection of simple types
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role") // This is the column name for roles in the user_roles table
    private Set<String> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(LocalDateTime dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public UserEntity(Long id, String email, String userName, String password, String firstName, String lastName,
			String address, LocalDateTime dateOfJoining, Set<String> roles) {
		super();
		this.id = id;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.dateOfJoining = dateOfJoining;
		this.roles = roles;
	}

	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	} 

    
}
