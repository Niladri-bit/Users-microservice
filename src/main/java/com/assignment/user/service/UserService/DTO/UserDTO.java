package com.assignment.user.service.UserService.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UserDTO {

	private Long id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private String address;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
	
    
    public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	
	public UserDTO(Long id, String email, String userName, String firstName, String lastName, String address,
			String password) {
		super();
		this.id = id;
		this.email = email;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.password = password;
	}
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
	
}
