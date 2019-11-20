package de.pharmamall.ms.mapservice.db.repos;
import org.springframework.data.jpa.repository.JpaRepository;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplate;

public interface TemplateRepository extends JpaRepository<XmlMappingTemplate, Long> {
    
    public XmlMappingTemplate findXmlMappingTemplateByVendorIdAndSourceTypeAndTargetType(final Long vendorId, final String sourceType,
            final String targetType);
}