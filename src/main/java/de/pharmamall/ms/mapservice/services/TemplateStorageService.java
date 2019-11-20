package de.pharmamall.ms.mapservice.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplate;
import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplateVersion;
import de.pharmamall.ms.mapservice.db.repos.TemplateRepository;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.TemplateListFilter;

@Service
public class TemplateStorageService {
    
    private TemplateRepository templateRepository;
    private TemplateVersionService templateVersionService;
    
    @Autowired
    public TemplateStorageService(TemplateRepository templateRepository, TemplateVersionService templateVersionService) {
        this.templateRepository = templateRepository;
        this.templateVersionService = templateVersionService;
    }
    
    @Transactional
    public XmlMappingTemplate saveXmlMappingTemplate(final byte[] templateContent, final TemplateIdentifier templateIdentifier) {
        XmlMappingTemplate xmlMappingTemplate = getXmlMappingTemplateByTemplateIdentifier(templateIdentifier);
        if (xmlMappingTemplate == null) {
            xmlMappingTemplate = new XmlMappingTemplate(templateIdentifier);
        }
        
        if (xmlMappingTemplate.getVersionCounter() > 0L) {
            saveVersion(xmlMappingTemplate);
            xmlMappingTemplate.setUpdatedAt(new Date());
        }
        
        xmlMappingTemplate.setVersionCounter(xmlMappingTemplate.getVersionCounter() + 1L);
        xmlMappingTemplate.setTemplateContent(templateContent);
        
        templateRepository.save(xmlMappingTemplate);
        return xmlMappingTemplate;
    }
    
    public void saveVersion(final XmlMappingTemplate xmlMappingTemplate) {
        XmlMappingTemplateVersion version = XmlMappingTemplateVersion.forXmlMappingTemplate(xmlMappingTemplate);
        templateVersionService.saveXmlMappingTemplateVersion(version);
    }
    
    public List<TemplateIdentifier> listTemplates() {
        List<XmlMappingTemplate> templates = templateRepository.findAll();
        return getTemplateIdentifierListByXmlMappingTemplateList(templates);
    }
    
    public XmlMappingTemplate getXmlMappingTemplateByTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        return templateRepository.findXmlMappingTemplateByVendorIdAndSourceTypeAndTargetType(templateIdentifier.getVendorId(),
                templateIdentifier.getSourceType(), templateIdentifier.getTargetType());
    }
    
    public boolean deleteTemplate(TemplateIdentifier templateIdentifier) {
        XmlMappingTemplate xmlMappingTemplate = getXmlMappingTemplateByTemplateIdentifier(templateIdentifier);
        saveVersion(xmlMappingTemplate);
        templateRepository.delete(xmlMappingTemplate);
        return true;
    }
    
    public List<TemplateIdentifier> listTemplatesByTemplateListFilter(final TemplateListFilter templateListFilter) {
        return getTemplateIdentifierListByXmlMappingTemplateList(listXmlMappingTemplatesByTemplateListFilter(templateListFilter));
    }
    
    // @VisibleForTesting
    protected List<TemplateIdentifier> getTemplateIdentifierListByXmlMappingTemplateList(List<XmlMappingTemplate> templates) {
        return templates.stream().map(XmlMappingTemplate::getTemplateIdentifier).sorted((t1, t2) -> t1.getVendorId().compareTo(t2.getVendorId()))
                .collect(Collectors.toList());
    }
    
    public List<XmlMappingTemplate> listXmlMappingTemplatesByTemplateListFilter(final TemplateListFilter templateListFilter) {
        Example<XmlMappingTemplate> xmlMappingTemplateExample = xmlMappingTemplateExampleByTemplateListFilter(templateListFilter);
        return templateRepository.findAll(xmlMappingTemplateExample);
    }
    
    // @VisibleForTesting
    protected Example<XmlMappingTemplate> xmlMappingTemplateExampleByTemplateListFilter(final TemplateListFilter templateListFilter) {
        TemplateFilterBuilder templateFilterBuilder = new TemplateFilterBuilder();
        return templateFilterBuilder.getExampleXmlMappingTemplateByTemplateListFilter(templateListFilter);
    }
}