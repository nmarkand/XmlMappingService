package de.pharmamall.ms.mapservice.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplateVersion;


public interface TemplateVersionRepository extends JpaRepository<XmlMappingTemplateVersion, Long> {
    
    public List<XmlMappingTemplateVersion> findXmlMappingTemplateVersionListByVendorIdAndSourceTypeAndTargetType(final Long vendorId,
            final String sourceType, final String targetType);
    
    public XmlMappingTemplateVersion findXmlMappingTemplateVersionByVendorIdAndSourceTypeAndTargetTypeAndVersionCounter(final Long vendorId,
            final String sourceType,
            final String targetType, final Long versionCounter);
    
}