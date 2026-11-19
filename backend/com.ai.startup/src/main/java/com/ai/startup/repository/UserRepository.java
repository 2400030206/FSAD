package com.ai.startup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.startup.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

User findByEmail(String email);

}
