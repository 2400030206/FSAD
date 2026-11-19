package com.ai.startup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.startup.model.ExamAttempt;
import com.ai.startup.repository.ExamRepository;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

@Autowired ExamRepository repo;

@GetMapping("/start")
public int start(){

ExamAttempt exam=new ExamAttempt();
exam.setCompleted(false);
exam.setAutoSubmit(false);

ExamAttempt saved=repo.save(exam);
return saved.getId();
}

@GetMapping("/autoSubmit")
public String autoSubmit(@RequestParam int id){

return repo.findById(id)
.map(exam -> {
exam.setCompleted(true);
exam.setAutoSubmit(true);
repo.save(exam);
return "Exam auto submitted";
})
.orElse("Exam attempt not found");
}

@GetMapping("/submit")
public String submit(@RequestParam int id){

return repo.findById(id)
.map(exam -> {
exam.setCompleted(true);
exam.setAutoSubmit(false);
repo.save(exam);
return "Exam submitted";
})
.orElse("Exam attempt not found");
}

@GetMapping("/status")
public String status(@RequestParam int id){

return repo.findById(id)
.map(exam -> "Completed: "+exam.isCompleted()+", AutoSubmit: "+exam.isAutoSubmit())
.orElse("Exam attempt not found");
}
}
