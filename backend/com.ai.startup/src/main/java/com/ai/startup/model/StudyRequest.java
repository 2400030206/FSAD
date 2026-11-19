package com.ai.startup.model;

import java.util.List;

public class StudyRequest {

private String text;
private String language;
private String level;
private List<String> outputs;

public String getText() {
    return text;
}

public void setText(String text) {
    this.text = text;
}

public String getLanguage() {
    return language;
}

public void setLanguage(String language) {
    this.language = language;
}

public String getLevel() {
    return level;
}

public void setLevel(String level) {
    this.level = level;
}

public List<String> getOutputs() {
    return outputs;
}

public void setOutputs(List<String> outputs) {
    this.outputs = outputs;
}
}
