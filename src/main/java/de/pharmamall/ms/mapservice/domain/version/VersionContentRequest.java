package de.pharmamall.ms.mapservice.domain.version;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

public class VersionContentRequest {
    
    private TemplateIdentifier templateIdentifier;
    private Long versionNumber;
    
    public VersionContentRequest() {
    }
    
    public VersionContentRequest(TemplateIdentifier templateIdentifier, Long versionNumber) {
        super();
        this.templateIdentifier = templateIdentifier;
        this.versionNumber = versionNumber;
    }
    
    public TemplateIdentifier getTemplateIdentifier() {
        return templateIdentifier;
    }
    
    public void setTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }
    
    public Long getVersionNumber() {
        return versionNumber;
    }
    
    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    @Override
    public String toString() {
        return "VersionContentRequest [templateIdentifier=" + templateIdentifier + ", versionNumber=" + versionNumber + "]";
    }
}