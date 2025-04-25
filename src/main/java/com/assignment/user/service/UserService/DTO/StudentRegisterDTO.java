package com.assignment.user.service.UserService.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class StudentRegisterDTO extends UserDTO{

	@NotBlank
    private String studentClass;

    @NotNull
    private LocalDate dateOfBirth;

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public StudentRegisterDTO(Long id, String email, String userName, String firstName, String lastName, String address,
			String password, @NotBlank String studentClass, @NotNull LocalDate dateOfBirth) {
		super(id, email, userName, firstName, lastName, address, password);
		this.studentClass = studentClass;
		this.dateOfBirth = dateOfBirth;
	}

	public StudentRegisterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentRegisterDTO(Long id, String email, String userName, String firstName, String lastName, String address,
			String password) {
		super(id, email, userName, firstName, lastName, address, password);
		// TODO Auto-generated constructor stub
	}
    
    
    
}
