package com.ai.startup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ai.startup.model.ExamAttempt;

public interface ExamRepository extends JpaRepository<ExamAttempt,Integer>{
}
