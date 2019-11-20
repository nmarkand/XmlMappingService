package de.pharmamall.ms.mapservice.domain;

public class DeleteTemplateResult {
    
    private boolean deleted = false;
    
    public DeleteTemplateResult() {
    }
    
    public DeleteTemplateResult(boolean deleted) {
        this.deleted = deleted;
    }
    
    public boolean getDeleted() {
        return deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}