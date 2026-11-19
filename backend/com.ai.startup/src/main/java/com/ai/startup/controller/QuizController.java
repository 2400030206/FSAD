package com.ai.startup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.startup.model.Question;
import com.ai.startup.service.QuizService;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

@Autowired QuizService quiz;

@GetMapping("/generate")
public List<Question> generate(){
return quiz.generateQuiz();
}

@GetMapping("/last")
public List<Question> last(){
return quiz.getLastQuestions();
}
}
