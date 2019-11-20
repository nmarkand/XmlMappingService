package de.pharmamall.ms.mapservice.domain.version;

public class VersionContentResult {
    private String templateContent;
    
    public VersionContentResult(String templateContent) {
        setTemplateContent(templateContent);
    }
    
    public VersionContentResult(byte[] templateContent) {
        this(new String(templateContent));
    }
    
    public String getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
}