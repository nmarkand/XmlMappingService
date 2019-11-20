package de.pharmamall.ms.mapservice.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplate;
import de.pharmamall.ms.mapservice.domain.TemplateListFilter;

public class TemplateFilterBuilder {
    private static final List<String> IGNORED_PROPERTY_LIST = Arrays.asList(new String[] { "id", "versionCounter" });
    
    public Example<XmlMappingTemplate> getExampleXmlMappingTemplateByTemplateListFilter(final TemplateListFilter templateListFilter) {
        final XmlMappingTemplate xmlMappingTemplate = forTemplateListFilter(templateListFilter);
        return Example.of(xmlMappingTemplate, getExampleMatcher(IGNORED_PROPERTY_LIST));
    }
    
    private ExampleMatcher getExampleMatcher(final List<String> ignoredProperties) {
        return ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths(ignoredProperties.toArray(new String[ignoredProperties.size()]));
    }
    
    private XmlMappingTemplate forTemplateListFilter(final TemplateListFilter templateListFilter) {
        XmlMappingTemplate xmlMappingTemplate = new XmlMappingTemplate();
        xmlMappingTemplate.setVendorId(templateListFilter.getVendorId());
        xmlMappingTemplate.setSourceType(templateListFilter.getSourceType());
        xmlMappingTemplate.setTargetType(templateListFilter.getTargetType());
        return xmlMappingTemplate;
    }
}