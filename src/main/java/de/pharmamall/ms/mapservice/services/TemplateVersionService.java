package de.pharmamall.ms.mapservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplateVersion;
import de.pharmamall.ms.mapservice.db.repos.TemplateVersionRepository;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

@Service
public class TemplateVersionService {
    TemplateVersionRepository templateRepositoryVersion;
    
    @Autowired
    public TemplateVersionService(TemplateVersionRepository templateRepositoryVersion) {
        this.templateRepositoryVersion = templateRepositoryVersion;
    }
    
    public XmlMappingTemplateVersion saveXmlMappingTemplateVersion(final XmlMappingTemplateVersion xmlMappingTemplateVersion) {
        return templateRepositoryVersion.save(xmlMappingTemplateVersion);
    }
    
    public List<XmlMappingTemplateVersion> getXmlMappingTemplateVersions(final TemplateIdentifier templateIdentifier) {
        return templateRepositoryVersion.findXmlMappingTemplateVersionListByVendorIdAndSourceTypeAndTargetType(templateIdentifier.getVendorId(),
                templateIdentifier.getSourceType(), templateIdentifier.getTargetType());
    }
    
    public XmlMappingTemplateVersion getXmlMappingTemplateVersion(final TemplateIdentifier templateIdentifier, final Long versionCounter) {
        return templateRepositoryVersion.findXmlMappingTemplateVersionByVendorIdAndSourceTypeAndTargetTypeAndVersionCounter(
                templateIdentifier.getVendorId(), templateIdentifier.getSourceType(), templateIdentifier.getTargetType(), versionCounter);
    }
}