package com.assignment.user.service.UserService.Services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.user.service.UserService.DTO.StudentRegisterDTO;
import com.assignment.user.service.UserService.DTO.UserDTO;
import com.assignment.user.service.UserService.Exceptions.InvalidRoleAssignmentException;
import com.assignment.user.service.UserService.Exceptions.InvalidUserNameException;
import com.assignment.user.service.UserService.Exceptions.UserNotFoundException;
import com.assignment.user.service.UserService.entities.UserEntity;
import com.assignment.user.service.UserService.repository.UserRepository;
import com.assignment.user.service.UserService.utils.enumerations.Role;
import com.assignment.user.service.UserService.utils.Util;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VaccinationCommunicationService vaccinationCommunicationService;
	
	
	 
	public void assignRolesToUser(Long userId, Set<Role> roles) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        for (Role role : roles) {
        	if (role == Role.ROLE_SERVICE_COMMUNICATOR) {
                throw new InvalidRoleAssignmentException("Cannot assign ROLE_SERVICE_COMMUNICATOR via this service.");
            }
            if (!Util.isValidRole(role)) {
                throw new InvalidRoleAssignmentException("Invalid role: " + role);
            }
            user.getRoles().add(role.toString());
        }
        userRepository.save(user);
    }

	  
	public StudentRegisterDTO getUserById(Long userId) {
		UserEntity userEntity = userRepository.findById(userId).orElse(null);

		if(userEntity == null ) {
			throw new UserNotFoundException(userId);
		}
		

		 boolean isStudent = userEntity.getRoles().stream()
		            .anyMatch(role -> role.equalsIgnoreCase("STUDENT") );

		 if(!isStudent) {
				throw new UserNotFoundException(userId);
			}
		return modelMapper.map(userEntity, StudentRegisterDTO.class);
	}
	
	public UserDTO createUser(UserDTO userDTO) {
		validateUserName(userDTO.getUserName());
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setDateOfJoining(LocalDateTime.now()); 
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.getRoles().add(Role.ADMIN.toString());
        userEntity = userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserDTO.class);
    }
	
	public UserDTO createStudent(StudentRegisterDTO studentDTO) {
		validateUserName(studentDTO.getUserName());
        UserEntity userEntity = modelMapper.map(studentDTO, UserEntity.class);
        userEntity.setDateOfJoining(LocalDateTime.now()); 
        userEntity.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        userEntity.getRoles().add(Role.STUDENT.toString());
        userEntity = userRepository.save(userEntity);
        return modelMapper.map(userEntity, UserDTO.class);
    }

	public boolean authenticateUser(String username, String password) {
	    UserEntity userEntity = userRepository.findByUserName(username);
	    
	    if (userEntity != null && passwordEncoder.matches(password, userEntity.getPassword())) {
	        return true;
	    }
	    return false;
	}
	
	
	
//	public List<StudentRegisterDTO> getAllUsers() {
//	    List<UserEntity> userEntities = userRepository.findAll(); 
//
//	    return userEntities.stream()
//	        .filter(user -> user.getRoles().stream()
//	            .anyMatch(role -> role.equalsIgnoreCase("STUDENT")))
//	        .map(userEntity -> modelMapper.map(userEntity, StudentRegisterDTO.class))
//	        .collect(Collectors.toList()); 
//	}

	public List<StudentRegisterDTO> getAllUsers(List<String> classList) {
	    List<UserEntity> userEntities = userRepository.findAll();

	    return userEntities.stream()
	        .filter(user -> user.getRoles().stream()
	            .anyMatch(role -> role.equalsIgnoreCase("STUDENT")))
	        .filter(user -> classList == null || 
	            classList.stream()
	                .anyMatch(c -> c.equalsIgnoreCase(user.getStudentClass())))
	        .map(userEntity -> modelMapper.map(userEntity, StudentRegisterDTO.class))
	        .collect(Collectors.toList());
	}

	 @Transactional
	 public void deleteUserById(Long id) {
		    Optional<UserEntity> userOptional = userRepository.findById(id);

		    if (userOptional.isPresent()) {
		        UserEntity user = userOptional.get();

		        boolean isStudent = user.getRoles().stream()
		                .anyMatch(role -> role.equalsIgnoreCase("STUDENT"));

		        if (!isStudent) {
		            throw new UserNotFoundException(id);
		        }
		        vaccinationCommunicationService.deleteVaccinationsForAStudent(id);
		        userRepository.deleteById(id);
		    } else {
		        throw new UserNotFoundException(id);
		    }
		}

	

	 public StudentRegisterDTO updateStudent(Long id, StudentRegisterDTO studentDto) {
		    Optional<UserEntity> existingStudentOpt = userRepository.findById(id);

		    if (!existingStudentOpt.isPresent()) {
		        throw new UserNotFoundException(id);
		    }

		    UserEntity existingStudent = existingStudentOpt.get();

		    boolean isStudent = existingStudent.getRoles().stream()
		            .anyMatch(role ->  role.equalsIgnoreCase("STUDENT"));

		    if (!isStudent) {
		        throw new UserNotFoundException(id);
		    }

		    studentDto.setId(existingStudent.getId());
		    modelMapper.map(studentDto, existingStudent);

		    UserEntity updatedStudent = userRepository.save(existingStudent);

		    return modelMapper.map(updatedStudent, StudentRegisterDTO.class);
		}


	 
	 public void validateUserName(String username) {
		 UserEntity userEntity = userRepository.findByUserName(username);
		 if(userEntity != null) {
			 throw new InvalidUserNameException("Invalid username: "+ username);
		 }
	}


}
