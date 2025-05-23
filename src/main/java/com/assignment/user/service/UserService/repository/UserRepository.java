package com.assignment.user.service.UserService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.user.service.UserService.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{

	UserEntity findByUserName(String userName);

	UserEntity save(Optional<UserEntity> existingStudent);
}
