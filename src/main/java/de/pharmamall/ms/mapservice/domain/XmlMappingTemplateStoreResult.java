package de.pharmamall.ms.mapservice.domain;

public class XmlMappingTemplateStoreResult {
    
    private Long dbId;
    
    public XmlMappingTemplateStoreResult(Long dbId) {
        this.dbId = dbId;
    }
    
    public Long getDbId() {
        return dbId;
    }
    
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }
    
    @Override
    public String toString() {
        return "XmlMappingTemplateStoreResult [dbId=" + dbId + "]";
    }
}