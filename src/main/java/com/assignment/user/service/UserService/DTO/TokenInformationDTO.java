package com.assignment.user.service.UserService.DTO;

public class TokenInformationDTO {

	private Long id;
    private String email;
    private String userName;
    
	public TokenInformationDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenInformationDTO(Long id, String email, String userName) {
		super();
		this.id = id;
		this.email = email;
		this.userName = userName;
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
    
    
}
