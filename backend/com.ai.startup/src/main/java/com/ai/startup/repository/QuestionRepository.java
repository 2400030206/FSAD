package com.ai.startup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.startup.model.Question;

public interface QuestionRepository extends JpaRepository<Question,Integer>{
}
