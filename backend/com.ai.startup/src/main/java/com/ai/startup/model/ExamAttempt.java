package com.ai.startup.model;

import jakarta.persistence.*;

@Entity
public class ExamAttempt {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int id;

private boolean completed=false;
private boolean autoSubmit=false;

public int getId(){return id;}
public void setId(int id){this.id=id;}

public boolean isCompleted(){return completed;}
public void setCompleted(boolean completed){this.completed=completed;}

public boolean isAutoSubmit(){return autoSubmit;}
public void setAutoSubmit(boolean autoSubmit){this.autoSubmit=autoSubmit;}
}
