package de.pharmamall.ms.mapservice.domain;

public class TransformRequest {
    
    private TemplateIdentifier templateIdentifier;
    private String sourceContent;
    private String templateContent;
    
    public TemplateIdentifier getTemplateIdentifier() {
        return templateIdentifier;
    }
    
    public String getSourceContent() {
        return sourceContent;
    }
    
    public void setTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }
    
    public void setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent;
    }
    
    public String getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    
    public byte[] getSourceBytes() {
        return getSourceContent().getBytes();
    }
    
    public byte[] getTemplateBytes() {
        return getTemplateContent().getBytes();
    }
    
}