package de.pharmamall.ms.mapservice.freemarker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.core.io.ClassPathResource;

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplate;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.services.TemplateStorageService;
import freemarker.cache.TemplateLoader;

public class DbTemplateLoader implements TemplateLoader {
    
    private TemplateStorageService templateStorageService;
    private final Map<String, BlobTemplateSource> templates = new HashMap<>();
    
    public DbTemplateLoader(TemplateStorageService dbService) {
        templateStorageService = dbService;
    }
    
    @Override
    public void closeTemplateSource(Object paramObject) throws IOException {
    }
    
    @Override
    public Object findTemplateSource(String name) throws IOException {
        if (templates.containsKey(name)) {
            return templates.get(name);
        }
        
        byte[] bytes = null;
        if (name.startsWith("classpath:")) {
            bytes = resourceToBytes(name);
        } else {
            bytes = loadTemplateBytes(name);
        }
        
        BlobTemplateSource ts = new BlobTemplateSource(name, bytes, System.currentTimeMillis());
        templates.put(name, ts);
        return ts;
    }
    
    private byte[] loadTemplateBytes(String ftname) {
        TemplateIdentifier templateIdentifier = TemplateIdentifier.fromFreemarkerTemplateName(ftname);
        XmlMappingTemplate xmlMappingTemplate = templateStorageService.getXmlMappingTemplateByTemplateIdentifier(templateIdentifier);
        return xmlMappingTemplate.getTemplateContent();
    }
    
    public void uncacheTemplate(TemplateIdentifier templateIdentifier) {
        templates.remove(templateIdentifier.toFreemarkerTemplateName());
    }
    
    @Override
    public long getLastModified(Object templateSource) {
        return ((BlobTemplateSource) templateSource).lastModified;
    }
    
    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return new InputStreamReader(new ByteArrayInputStream(((BlobTemplateSource) templateSource).templateContent), encoding);
    }
    
    private byte[] resourceToBytes(String name) {
        try {
            InputStream rStream = new ClassPathResource(name.replace("classpath:", "")).getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StreamUtils.copy(rStream, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new MappingServiceException("Could not read resource " + name, e);
        }
    }
    
    protected static class BlobTemplateSource {
        protected final String name;
        
        protected final byte[] templateContent;
        protected final long lastModified;
        
        BlobTemplateSource(String name, byte[] templateContent, long lastModified) {
            if (name == null) {
                throw new IllegalArgumentException("name == null");
            }
            if (templateContent == null) {
                throw new IllegalArgumentException("templateContent == null");
            }
            if (lastModified < -1L) {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            this.name = name;
            this.templateContent = templateContent;
            this.lastModified = lastModified;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (name == null ? 0 : name.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            BlobTemplateSource other = (BlobTemplateSource) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }
    }
}