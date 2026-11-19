package com.ai.startup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ai.startup.service.AIService;
import com.ai.startup.service.PdfService;
import com.ai.startup.service.QuizService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

@Autowired PdfService pdf;
@Autowired AIService ai;
@Autowired QuizService quiz;

@PostMapping("/upload")
public ResponseEntity<String> upload(@RequestParam MultipartFile file,
                     @RequestParam String level,
                     @RequestParam String language,
                     @RequestParam(required=false) String outputs){

if(file == null || file.isEmpty()){
return ResponseEntity.badRequest().body("Please upload a PDF file");
}

if(!pdf.isPdf(file)){
return ResponseEntity.badRequest().body("Invalid PDF file. Please upload a valid PDF.");
}

try{
String text=pdf.extractText(file);
List<String> selectedOutputs=parseOutputs(outputs);
List<com.ai.startup.model.Question> questions=null;
if(needsQuiz(selectedOutputs)){
questions=quiz.generateStudyQuiz(text,level);
}
String result=ai.generateStudyResponse(text,level,language,selectedOutputs,questions);

Path outputDir=Path.of("output");
Files.createDirectories(outputDir);
Path summary=outputDir.resolve("summary.txt");
Files.writeString(summary,result,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);

StringBuilder response=new StringBuilder();
response.append(result);

return ResponseEntity.ok(response.toString().trim());
}catch(Exception ex){
return ResponseEntity.internalServerError().body("Failed to process PDF: "+ex.getMessage());
}
}

private List<String> parseOutputs(String outputs){
if(outputs == null || outputs.trim().isEmpty()) return List.of();
String[] parts=outputs.split(",");
List<String> list=new java.util.ArrayList<>();
for(String part:parts){
String trimmed=part.trim();
if(!trimmed.isEmpty()) list.add(trimmed);
}
return list;
}

private boolean needsQuiz(List<String> outputs){
if(outputs == null || outputs.isEmpty()) return false;
for(String output:outputs){
if(output == null) continue;
String value=output.trim().toLowerCase();
if(value.equals("quiz") || value.equals("answer_explanation")) return true;
}
return false;
}
}

