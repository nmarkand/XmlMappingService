package de.pharmamall.ms.mapservice.domain.version;

public class XmlMappingTemplateVersionStoreResult {
    
    private Long dbId;
    
    public Long getDbId() {
        return dbId;
    }
    
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }
    
    @Override
    public String toString() {
        return "XmlMappingTemplateVersionStoreResult [dbId=" + dbId + "]";
    }
}