package com.ai.startup.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.ai.startup.model.Question;

@Service
public class QuizService {

private static final Pattern SENTENCE_SPLIT=Pattern.compile("[.!?]+\\s*");

private List<Question> lastQuestions=new ArrayList<>();

public List<Question> generateQuiz(){

List<Question> list=new ArrayList<>();

Question q1=new Question();
q1.setType("MCQ");

Question q2=new Question();
q2.setType("TRUE_FALSE");

Question q3=new Question();
q3.setType("ONE_LINE");

Question q4=new Question();
q4.setType("IMPORTANT");

list.add(q1);
list.add(q2);
list.add(q3);
list.add(q4);

return list;
}

public List<Question> generateFromText(String text){

List<Question> list=new ArrayList<>();

if(text == null) text="";

String cleaned=text.replaceAll("\r", " ").replaceAll("\n", " ").trim();
if(cleaned.length() > 800){
cleaned=cleaned.substring(0,800);
}

String[] sentences=cleaned.split("[.!?]");

int count=0;
for(String s:sentences){
if(count>=5) break;
String trimmed=s.trim();
if(trimmed.length()<20) continue;

Question q=new Question();
q.setType("ONE_LINE");
q.setQuestion("Explain: "+trimmed);
q.setCorrectAnswer("Refer to the uploaded PDF explanation.");
list.add(q);
count++;
}

if(list.isEmpty()){
Question q=new Question();
q.setType("ONE_LINE");
q.setQuestion("Summarize the main idea of the uploaded PDF.");
q.setCorrectAnswer("Refer to the uploaded PDF explanation.");
list.add(q);
}

lastQuestions=list;
return list;
}

public List<Question> generateStudyQuiz(String text,String level){

List<String> sentences=extractSentences(text);
List<Question> list=new ArrayList<>();

Question mcq=new Question();
mcq.setType("MCQ");
String mcqQuestion=buildMcqQuestion(sentences);
mcq.setQuestion(mcqQuestion);
mcq.setCorrectAnswer(extractMcqAnswer(mcqQuestion));
list.add(mcq);

Question tf=new Question();
tf.setType("TRUE_FALSE");
tf.setQuestion(buildTrueFalseQuestion(sentences));
tf.setCorrectAnswer("True");
list.add(tf);

Question oneLine=new Question();
oneLine.setType("ONE_LINE");
oneLine.setQuestion(buildOneLineQuestion(sentences));
oneLine.setCorrectAnswer("Answer should be based on the PDF text.");
list.add(oneLine);

Question important=new Question();
important.setType("IMPORTANT");
important.setQuestion(buildImportantQuestion(sentences));
important.setCorrectAnswer("Answer should include key points from the PDF.");
list.add(important);

lastQuestions=list;
return list;
}

public List<Question> getLastQuestions(){
return lastQuestions;
}

private static List<String> extractSentences(String text){
if(text == null || text.trim().isEmpty()) return Collections.emptyList();
String[] parts=SENTENCE_SPLIT.split(text.replace("\r"," ").replace("\n"," ").trim());
List<String> sentences=new ArrayList<>();
for(String part:parts){
String trimmed=part.trim();
if(trimmed.length() < 10) continue;
sentences.add(trimmed);
}
return sentences;
}

private static String buildMcqQuestion(List<String> sentences){
String correct=pickSentence(sentences,0,"The PDF explains a key idea in this section");
String optionB=pickSentence(sentences,1,"Not stated in the PDF text");
String optionC=pickSentence(sentences,2,"Not stated in the PDF text");
String optionD=pickSentence(sentences,3,"Not stated in the PDF text");

StringBuilder sb=new StringBuilder();
sb.append("Which statement is directly supported by the PDF?\n");
sb.append("A) ").append(correct).append("\n");
sb.append("B) ").append(optionB).append("\n");
sb.append("C) ").append(optionC).append("\n");
sb.append("D) ").append(optionD);
return sb.toString();
}

private static String extractMcqAnswer(String mcqQuestion){
if(mcqQuestion == null) return "A";
return "A";
}

private static String buildTrueFalseQuestion(List<String> sentences){
String statement=pickSentence(sentences,0,"The PDF contains key concepts for study.");
return "True or False: The PDF states that \""+statement+"\".";
}

private static String buildOneLineQuestion(List<String> sentences){
String statement=pickSentence(sentences,1,"the main idea of the PDF");
return "Answer in one line: "+statement;
}

private static String buildImportantQuestion(List<String> sentences){
String statement=pickSentence(sentences,2,"the main idea of the PDF");
return "Important exam question: Explain the significance of \""+statement+"\".";
}

private static String pickSentence(List<String> sentences,int index,String fallback){
if(sentences == null || sentences.isEmpty()) return fallback;
if(index < sentences.size()) return sentences.get(index);
return sentences.get(sentences.size()-1);
}
}
