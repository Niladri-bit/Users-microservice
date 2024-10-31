package com.assignment.user.service.UserService.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.user.service.UserService.DTO.UserDTO;
import com.assignment.user.service.UserService.Exceptions.UserNotFoundException;
import com.assignment.user.service.UserService.entities.UserEntity;
import com.assignment.user.service.UserService.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserDTO getUserById(Long userId) {
		UserEntity userEntity = userRepository.findById(userId).orElse(null);
		if(userEntity == null) {
			throw new UserNotFoundException(userId);
		}
		return modelMapper.map(userEntity, UserDTO.class);
	}
	
	public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setDateOfJoining(LocalDateTime.now()); 
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
	
	public List<UserDTO> getAllUsers() {
	    List<UserEntity> userEntities = userRepository.findAll(); 
	    return userEntities.stream() 
	        .map(userEntity -> modelMapper.map(userEntity, UserDTO.class))
	        .collect(Collectors.toList()); 
	}
	
	 public void deleteUserById(Long id) {
		 Optional<UserEntity> user = userRepository.findById(id);
	        if (user.isPresent()) {
	            userRepository.deleteById(id);
	        } else {
	            throw new UserNotFoundException(id); 
	        }
	    }


}
