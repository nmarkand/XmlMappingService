package de.pharmamall.ms.mapservice.domain.testsuite;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

public class StoreTestRequest {
    
    private TemplateIdentifier templateIdentifier;
    private String source;
    private String target;
    
    public TemplateIdentifier getTemplateIdentifier() {
        return templateIdentifier;
    }
    
    public String getSource() {
        return source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public byte[] getSourceBytes() {
        return getSource().getBytes();
    }
    
    public byte[] getTargetBytes() {
        return getTarget().getBytes();
    }
}