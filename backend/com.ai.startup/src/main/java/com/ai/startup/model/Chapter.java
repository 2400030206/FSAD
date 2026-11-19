package com.ai.startup.model;

import jakarta.persistence.*;

@Entity
public class Chapter {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int id;

private String title;

@Column(length=5000)
private String content;

public int getId(){return id;}
public void setId(int id){this.id=id;}

public String getTitle(){return title;}
public void setTitle(String title){this.title=title;}

public String getContent(){return content;}
public void setContent(String content){this.content=content;}
}
