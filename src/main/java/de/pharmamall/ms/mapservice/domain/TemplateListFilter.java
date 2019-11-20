package de.pharmamall.ms.mapservice.domain;

public class TemplateListFilter {
    
    private Long vendorId;
    private String sourceType;
    private String targetType;
    
    public TemplateListFilter() {
    }
    
    public TemplateListFilter(TemplateIdentifier templateIdentifier) {
        vendorId = templateIdentifier.getVendorId();
        sourceType = templateIdentifier.getSourceType();
        targetType = templateIdentifier.getTargetType();
    }
    
    public Long getVendorId() {
        return vendorId;
    }
    
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    @Override
    public String toString() {
        return "TemplateListFilter [vendorId=" + vendorId + ", sourceType=" + sourceType + ", targetType=" + targetType + "]";
    }
}