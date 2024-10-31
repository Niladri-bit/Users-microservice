package com.assignment.user.service.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assignment.user.service.UserService.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

	UserEntity findByUserName(String userName);
}
