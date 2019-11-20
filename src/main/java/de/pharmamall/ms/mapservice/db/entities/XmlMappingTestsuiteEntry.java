package de.pharmamall.ms.mapservice.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

@Entity
@Table(name = "TBL_MS_MAP_TEST_ENTRY")
public class XmlMappingTestsuiteEntry {
    
    @Id
    @GeneratedValue(generator = "XmlMappingTestuiteEntrySeq")
    @SequenceGenerator(name = "XmlMappingTestuiteEntrySeq", sequenceName = "SEQ_TBL_MS_MAP_TEST_ENTRY", allocationSize = 1)
    @Column(name = "ID", nullable = false, updatable = false)
    private long id;
    
    @Column(name = "vendor_id")
    private Long vendorId;
    
    @Column(name = "source_type")
    private String sourceType;
    
    @Column(name = "target_type")
    private String targetType;
    
    @Lob
    @Column(name = "source", columnDefinition = "BLOB")
    private byte[] sourceContent;
    
    @Lob
    @Column(name = "target", columnDefinition = "BLOB")
    private byte[] targetContent;
    
    public XmlMappingTestsuiteEntry() {
        
    }
    
    public XmlMappingTestsuiteEntry(TemplateIdentifier templateIdentifier) {
        vendorId = templateIdentifier.getVendorId();
        sourceType = templateIdentifier.getSourceType();
        targetType = templateIdentifier.getTargetType();
    }
    
    public long getId() {
        return id;
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
    
    public byte[] getSourceContent() {
        return sourceContent;
    }
    
    public byte[] getTargetContent() {
        return targetContent;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    public void setSourceContent(byte[] sourceContent) {
        this.sourceContent = sourceContent;
    }
    
    public void setTargetContent(byte[] targetContent) {
        this.targetContent = targetContent;
    }
    
    public TemplateIdentifier getTemplateIdentifier() {
        return new TemplateIdentifier(getVendorId(), getSourceType(), getTargetType());
    }
    
    @Override
    public String toString() {
        return "XmlMappingTestsuiteEntry [id=" + id + ", vendorId=" + vendorId + ", sourceType=" + sourceType + ", targetType=" + targetType + "]";
    }
}