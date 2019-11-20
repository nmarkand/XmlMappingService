package de.pharmamall.ms.mapservice.domain;

public class TemplateIdentifier {
    
    // version
    private Long vendorId;
    private String sourceType;
    private String targetType;
    
    public TemplateIdentifier() {
        
    }
    
    public TemplateIdentifier(Long vendorId, String sourceType, String targetType) {
        this.vendorId = vendorId;
        this.sourceType = sourceType;
        this.targetType = targetType;
    }
    
    public Long getVendorId() {
        return vendorId;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public String toFreemarkerTemplateName() {
        return String.format("%s:%s:%s", getVendorId(), getSourceType(), getTargetType());
    }
    
    public static TemplateIdentifier fromFreemarkerTemplateName(String ftName) {
        String[] parts = ftName.split(":");
        return new TemplateIdentifier(Long.parseLong(parts[0]), parts[1], parts[2]);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (sourceType == null ? 0 : sourceType.hashCode());
        result = prime * result + (targetType == null ? 0 : targetType.hashCode());
        result = prime * result + (vendorId == null ? 0 : vendorId.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TemplateIdentifier other = (TemplateIdentifier) obj;
        if (sourceType == null) {
            if (other.sourceType != null) {
                return false;
            }
        } else if (!sourceType.equals(other.sourceType)) {
            return false;
        }
        if (targetType == null) {
            if (other.targetType != null) {
                return false;
            }
        } else if (!targetType.equals(other.targetType)) {
            return false;
        }
        if (vendorId == null) {
            if (other.vendorId != null) {
                return false;
            }
        } else if (!vendorId.equals(other.vendorId)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "TemplateIdentifier [vendorId=" + vendorId + ", sourceType=" + sourceType + ", targetType=" + targetType + "]";
    }
    
}