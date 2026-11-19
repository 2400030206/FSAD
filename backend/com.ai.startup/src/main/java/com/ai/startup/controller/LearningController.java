package com.ai.startup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.startup.service.AIService;
import com.ai.startup.service.QuizService;
import com.ai.startup.model.StudyRequest;
import java.util.List;

@RestController
@RequestMapping("/api/learning")
public class LearningController {

@Autowired AIService ai;
@Autowired QuizService quiz;

@PostMapping("/revision")
public ResponseEntity<String> revision(@RequestBody String text){

if(text == null || text.trim().isEmpty()){
return ResponseEntity.badRequest().body("No text to summarize");
}

return ResponseEntity.ok(ai.quickRevision(text));
}

@PostMapping("/generate")
public ResponseEntity<String> generate(@RequestBody StudyRequest request){
if(request == null || request.getText() == null || request.getText().trim().isEmpty()){
return ResponseEntity.badRequest().body("No PDF text provided");
}

List<String> outputs=request.getOutputs();
List<com.ai.startup.model.Question> questions=null;
if(outputs != null && !outputs.isEmpty() && needsQuiz(outputs)){
questions=quiz.generateStudyQuiz(request.getText(),request.getLevel());
}
String result=ai.generateStudyResponse(request.getText(),request.getLevel(),request.getLanguage(),outputs,questions);
return ResponseEntity.ok(result);
}

private boolean needsQuiz(List<String> outputs){
for(String output:outputs){
if(output == null) continue;
String value=output.trim().toLowerCase();
if(value.equals("quiz") || value.equals("answer_explanation")) return true;
}
return false;
}

@GetMapping("/download")
public ResponseEntity<Resource> download(){

FileSystemResource file=new FileSystemResource("output/summary.txt");

if(!file.exists()){
return ResponseEntity.notFound().build();
}

return ResponseEntity.ok()
.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=summary.txt")
.body(file);
}
}
