package de.pharmamall.ms.mapservice.db.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

@Entity
@Table(name = "TBL_MS_MAP_TMPLT_VERSION")
public class XmlMappingTemplateVersion {
    
    public XmlMappingTemplateVersion() {
        
    }
    
    public XmlMappingTemplateVersion(TemplateIdentifier templateIdentifier) {
        setSourceType(templateIdentifier.getSourceType());
        setTargetType(templateIdentifier.getTargetType());
        setVendorId(templateIdentifier.getVendorId());
        setValidTo(new Date());
    }
    
    @Id
    @GeneratedValue(generator = "XmlMappingTemplateVersionSeq")
    @SequenceGenerator(name = "XmlMappingTemplateVersionSeq", sequenceName = "SEQ_TBL_MS_MAP_TMPLT_VERSION", allocationSize = 1)
    @Column(name = "ID", nullable = false, updatable = false)
    private long id;
    
    @Column(name = "vendor_id")
    private Long vendorId;
    
    @Column(name = "source_type")
    private String sourceType;
    
    @Column(name = "target_type")
    private String targetType;
    
    @Column(name = "valid_from")
    private Date validFrom;
    
    @Column(name = "valid_to")
    private Date validTo;
    
    @Column(name = "version_counter")
    private long versionCounter;
    
    @Lob
    @Column(name = "template_content", columnDefinition = "BLOB")
    private byte[] templateContent;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public Date getValidFrom() {
        return validFrom;
    }
    
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    
    public Date getValidTo() {
        return validTo;
    }
    
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    
    public long getVersionCounter() {
        return versionCounter;
    }
    
    public void setVersionCounter(long versionCounter) {
        this.versionCounter = versionCounter;
    }
    
    public byte[] getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateContent(byte[] templateContent) {
        this.templateContent = templateContent;
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
    
    public static XmlMappingTemplateVersion forXmlMappingTemplate(final XmlMappingTemplate xmlMappingTemplate) {
        XmlMappingTemplateVersion version = new XmlMappingTemplateVersion(xmlMappingTemplate.getTemplateIdentifier());
        
        Date validFrom = xmlMappingTemplate.getUpdatedAt() != null ? xmlMappingTemplate.getUpdatedAt() : xmlMappingTemplate.getCreatedAt();
        version.setValidFrom(validFrom);
        version.setTemplateContent(xmlMappingTemplate.getTemplateContent());
        version.setVersionCounter(xmlMappingTemplate.getVersionCounter());
        return version;
    }
    
    @Override
    public String toString() {
        return "XmlMappingTemplateVersion [id=" + id + ", vendorId=" + vendorId + ", sourceType=" + sourceType + ", targetType=" + targetType
                + ", validFrom=" + validFrom + ", validTo=" + validTo + ", versionCounter=" + versionCounter + "]";
    }
}