package de.pharmamall.ms.mapservice.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import javax.jms.Topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplate;
import de.pharmamall.ms.mapservice.domain.DeleteTemplateResult;
import de.pharmamall.ms.mapservice.domain.NamedTemplate;
import de.pharmamall.ms.mapservice.domain.TemplateContent;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.TemplateList;
import de.pharmamall.ms.mapservice.domain.TemplateListFilter;
import de.pharmamall.ms.mapservice.messaging.domain.ServiceEvent;
import de.pharmamall.ms.mapservice.services.TemplateLoaderService;
import de.pharmamall.ms.mapservice.services.TemplateStorageService;

@Service
public class TemplateStorageControllerDelegate {
    
    private static final Logger log = LoggerFactory.getLogger(TemplateStorageControllerDelegate.class);
    
    private TemplateLoaderService templateLoaderService;
    private TemplateStorageService templateProviderService;
    private Topic topic;
    private JmsMessagingTemplate jmsMessagingTemplate;
    
    @Autowired
    public TemplateStorageControllerDelegate(TemplateLoaderService templateLoaderService, TemplateStorageService templateProviderService, Topic topic,
            JmsMessagingTemplate jmsMessagingTemplate) {
        this.templateLoaderService = templateLoaderService;
        this.templateProviderService = templateProviderService;
        this.topic = topic;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }
    
    public void storeTemplates(TemplateList templates) {
        templates.getTemplates().forEach(p -> storeTemplate(p));
    }
    
    public XmlMappingTemplate storeTemplate(NamedTemplate template) {
        return storeXmlMappingTemplate(template.getTemplateContent().getBytes(),template.getTemplateIdentifier());
    }
    
    public XmlMappingTemplate storeXmlMappingTemplate(final byte[] templateContent, final TemplateIdentifier templateIdentifier) {
        XmlMappingTemplate template = templateProviderService.saveXmlMappingTemplate(templateContent, templateIdentifier);
        jmsMessagingTemplate.convertAndSend(topic, new ServiceEvent("UNCACHE_ON_SAVE_TEMPLATE", templateIdentifier));
        return template;
    }
    
    public List<TemplateIdentifier> listTemplates() {
        return templateProviderService.listTemplates();
    }
    
    public TemplateContent getTemplateContent(TemplateIdentifier templateIdentifier) {
        XmlMappingTemplate template = templateProviderService.getXmlMappingTemplateByTemplateIdentifier(templateIdentifier);
        return new TemplateContent(template.getTemplateContent());
    }
    
    public DeleteTemplateResult deleteTemplate(TemplateIdentifier templateIdentifier) {
        boolean deleted = templateProviderService.deleteTemplate(templateIdentifier);
        templateLoaderService.uncacheTemplate(templateIdentifier);
        return new DeleteTemplateResult(deleted);
    }
    
    public boolean templateExists(TemplateIdentifier templateIdentifier) {
        XmlMappingTemplate template = templateProviderService.getXmlMappingTemplateByTemplateIdentifier(templateIdentifier);
        return template != null;
    }
    
    public List<TemplateIdentifier> listTemplatesByTemplateListFilter(final TemplateListFilter templateListFilter) {
        return templateProviderService.listTemplatesByTemplateListFilter(templateListFilter);
    }
    
    public List<NamedTemplate> listNamedTemplatesByTemplateListFilter(TemplateListFilter templateListFilter) {
        List<XmlMappingTemplate> templateList = templateProviderService.listXmlMappingTemplatesByTemplateListFilter(templateListFilter);
        return templateList.stream().map(t -> new NamedTemplate(t.getTemplateIdentifier(),t.getTemplateContent())).collect(Collectors.toList());
    }
}