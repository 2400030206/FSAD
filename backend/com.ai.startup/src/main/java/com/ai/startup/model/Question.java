package com.ai.startup.model;

import jakarta.persistence.*;

@Entity
public class Question {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int id;

private String question;
private String type;
private String correctAnswer;


// GETTERS AND SETTERS

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getQuestion() {
    return question;
}

public void setQuestion(String question) {
    this.question = question;
}

public String getType() {
    return type;
}

public void setType(String type) {
    this.type = type;
}

public String getCorrectAnswer() {
    return correctAnswer;
}

public void setCorrectAnswer(String correctAnswer) {
    this.correctAnswer = correctAnswer;
}

}
