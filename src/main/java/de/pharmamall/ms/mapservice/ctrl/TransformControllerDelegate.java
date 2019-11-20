package de.pharmamall.ms.mapservice.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.freemarker.FreemarkerTemplateApplier;
import de.pharmamall.ms.mapservice.services.TemplateLoaderService;
import freemarker.template.Template;

@Service
public class TransformControllerDelegate {
    
    private TemplateLoaderService templateLoaderService;
    private FreemarkerTemplateApplier freemarkerMappingService;
    
    private static final Logger log = LoggerFactory.getLogger(TransformControllerDelegate.class);
    
    @Autowired
    public TransformControllerDelegate(TemplateLoaderService templateLoaderService) {
        this.templateLoaderService = templateLoaderService;
        freemarkerMappingService = new FreemarkerTemplateApplier();
    }
    
    public byte[] map(final byte[] sourceContent, final TemplateIdentifier templateIdentifier) {
        log.info(String.format("map %s bytes with template %s", sourceContent.length, templateIdentifier));
        long start = System.currentTimeMillis();
        byte[] transformed = transform(sourceContent, templateLoaderService.loadTemplate(templateIdentifier));
        log.info(String.format("mapped to %s bytes in %sms", transformed.length, System.currentTimeMillis() - start));
        return transformed;
    }
    
    public byte[] map(final byte[] templateContent, final byte[] sourceContent) {
        return transform(sourceContent, templateLoaderService.getTemplateFromString(templateContent));
    }
    
    private byte[] transform(final byte[] sourceContent, Template template) {
        return freemarkerMappingService.transform(sourceContent, template);
    }
    
}