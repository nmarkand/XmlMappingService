package de.pharmamall.ms.mapservice.services;

import java.io.IOException;
import java.io.StringReader;

import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.freemarker.DbTemplateLoader;
import de.pharmamall.ms.mapservice.freemarker.FreemarkerConfigurationProducer;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class TemplateLoaderService {
    
    private Configuration freemarkerConfig;
    
    @Autowired
    public TemplateLoaderService(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }
    
    public Template loadTemplate(TemplateIdentifier templateIdentifier) {
        try {
            return freemarkerConfig.getTemplate(templateIdentifier.toFreemarkerTemplateName(), FreemarkerConfigurationProducer.DEFAULT_ENCODING);
        } catch (IOException e) {
            throw new MappingServiceException("Could not load template " + templateIdentifier, e);
        }
    }
    
    public void uncacheTemplate(TemplateIdentifier templateIdentifier) {
        try {
            freemarkerConfig.removeTemplateFromCache(templateIdentifier.toFreemarkerTemplateName());
        } catch (IOException e) {
            throw new MappingException("Could not remove template from freemarker cache " + templateIdentifier, e);
        }
        getFreemarkerTemplateLoader().uncacheTemplate(templateIdentifier);
    }
    
    public Template getTemplateFromString(byte[] templateContent) {
        StringReader reader = new StringReader(new String(templateContent));
        try {
            return new Template(null, reader, freemarkerConfig);
        } catch (IOException e) {
            throw new MappingServiceException("Could not create template from input string " + new String(templateContent), e);
        }
    }
    
    private DbTemplateLoader getFreemarkerTemplateLoader() {
        return (DbTemplateLoader) freemarkerConfig.getTemplateLoader();
    }
}