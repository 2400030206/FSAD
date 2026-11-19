package com.ai.startup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LectureService {

public List<String> splitScenes(String script){

List<String> scenes=new ArrayList<>();

String[] parts=script.split("Scene");

for(String p:parts){

if(!p.trim().isEmpty())
scenes.add("Scene "+p);

}

return scenes;
}
}
