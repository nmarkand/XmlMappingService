package de.pharmamall.ms.mapservice.freemarker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import de.pharmamall.ms.mapservice.services.TemplateStorageService;
import freemarker.template.Configuration;

@Component
public class FreemarkerConfigurationProducer {
    
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    @Autowired
    private TemplateStorageService templateStorageService;
    
    @Bean
    public Configuration getFreemarkerConfiguration() {
        Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_28);
        freemarkerConfig.setTemplateLoader(new DbTemplateLoader(templateStorageService));
        freemarkerConfig.setLocalizedLookup(false);
        freemarkerConfig.setDefaultEncoding(DEFAULT_ENCODING);
        freemarkerConfig.setOutputEncoding(DEFAULT_ENCODING);
        return freemarkerConfig;
    }
}