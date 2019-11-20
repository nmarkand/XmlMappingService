package de.pharmamall.ms.mapservice.domain.version;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemplateVersionListResult {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    
    private Long versionNumber;
    private String validTo;
    
    public TemplateVersionListResult() {
    }
    
    public TemplateVersionListResult(Long versionNumber, String validTo) {
        this.versionNumber = versionNumber;
        this.validTo = validTo;
    }
    
    public TemplateVersionListResult(Long versionNumber, Date validTo) {
        this.versionNumber = versionNumber;
        this.validTo = dateFormat.format(validTo);
    }
    
    public Long getVersionNumber() {
        return versionNumber;
    }
    
    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    public String getValidTo() {
        return validTo;
    }
    
    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
    
    @Override
    public String toString() {
        return "TemplateVersionListResult [versionNumber=" + versionNumber + ", validTo=" + validTo + "]";
    }
}