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
@Table(name = "TBL_MS_MAP_TMPLT")
public class XmlMappingTemplate {
    
    public XmlMappingTemplate() {
    }
    
    public XmlMappingTemplate(TemplateIdentifier templateIdentifier) {
        setSourceType(templateIdentifier.getSourceType());
        setTargetType(templateIdentifier.getTargetType());
        setVendorId(templateIdentifier.getVendorId());
        setVersionCounter(0L);
        setCreatedAt(new Date());
        setUpdatedAt(new Date());
    }
    
    @Id
    @GeneratedValue(generator = "XmlMappingTemplateSeq")
    @SequenceGenerator(name = "XmlMappingTemplateSeq", sequenceName = "SEQ_TBL_MS_MAP_TMPLT", allocationSize = 1)
    @Column(name = "ID", nullable = false, updatable = false)
    private long id;
    
    @Column(name = "vendor_id")
    private Long vendorId;
    
    @Column(name = "source_type")
    private String sourceType;
    
    @Column(name = "target_type")
    private String targetType;
    
    @Lob
    @Column(name = "template_content", columnDefinition = "BLOB")
    private byte[] templateContent;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(name = "version_counter")
    private long versionCounter;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
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
    
    public byte[] getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateContent(byte[] templateContent) {
        this.templateContent = templateContent;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public long getVersionCounter() {
        return versionCounter;
    }
    
    public void setVersionCounter(long versionCounter) {
        this.versionCounter = versionCounter;
    }
    
    @Override
    public String toString() {
        return "XmlMappingTemplate [id=" + id + ", vendorId=" + vendorId + ", sourceType=" + sourceType + ", targetType=" + targetType
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", versionCounter=" + versionCounter + "]";
    }
    
    public TemplateIdentifier getTemplateIdentifier() {
        return new TemplateIdentifier(getVendorId(), getSourceType(), getTargetType());
    }
}