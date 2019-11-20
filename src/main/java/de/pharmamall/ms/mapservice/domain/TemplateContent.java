package de.pharmamall.ms.mapservice.domain;

public class TemplateContent {
    
    private String content;
    
    public TemplateContent() {
    }
    
    public TemplateContent(byte[] bytes) {
        content = new String(bytes);
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
}