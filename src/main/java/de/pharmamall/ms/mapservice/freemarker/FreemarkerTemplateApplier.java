package de.pharmamall.ms.mapservice.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.parsing.SourceSyntax;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerTemplateApplier {
    
    private static final String CUSTOM_ATTRIBUTE_SOURCE_SYNTAX = "source-syntax";
    
    private FreemarkerModelFactory modelFactory = new FreemarkerModelFactory();
    
    public byte[] transform(final byte[] sourceContent, Template template) {
        SourceSyntax syntax = getTemplateSourceSyntax(template);
        Map<String, Object> model = modelFactory.parseSourceToModel(sourceContent,syntax);
        return applyTemplateAndGetResultBytes(template, model);
    }
    
    private byte[] applyTemplateAndGetResultBytes(Template template, Map<String, Object> root) {
        String out = applyTemplate(template, root);
        return out.getBytes();
    }
    
    private String applyTemplate(Template template, Map<String, Object> root) {
        Writer out = new StringWriter();
        
        try {
            template.setOutputEncoding(FreemarkerConfigurationProducer.DEFAULT_ENCODING);
            template.process(root, out);
        } catch (TemplateException | IOException e) {
            throw new MappingServiceException("Error in freemarker template processing", e);
        }
        return out.toString();
    }
    
    private SourceSyntax getTemplateSourceSyntax(Template template) {
        String syntaxName = (String) template.getCustomAttribute(CUSTOM_ATTRIBUTE_SOURCE_SYNTAX);

        Map<String, String> options = 
                Arrays.stream(template.getCustomAttributeNames())
                .filter(a -> SourceSyntax.SYNTAX_OPTIONS.contains(a))
                .collect(Collectors.toMap(a -> a, a -> (String) template.getCustomAttribute(a)));        
        
        return new SourceSyntax(syntaxName,options);
    }
}