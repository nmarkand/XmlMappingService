package de.pharmamall.ms.mapservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NamedTemplate {
    
    private TemplateIdentifier templateIdentifier;
    private String templateContent;
    
    public NamedTemplate() {
    }
    
    public NamedTemplate(TemplateIdentifier templateIdentifier, String templateContent) {
        this.templateIdentifier = templateIdentifier;
        this.templateContent = templateContent;
    }
    
    public NamedTemplate(TemplateIdentifier templateIdentifier, byte[] bytes) {
        this.templateIdentifier = templateIdentifier;
        setTemplateContent(bytes);
    }
    
    @JsonIgnore
    public byte[] getBytes() {
        return getTemplateContent().getBytes();
    }
    
    public TemplateIdentifier getTemplateIdentifier() {
        return templateIdentifier;
    }
    
    public String getTemplateContent() {
        return templateContent;
    }
    
    public void setTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }
    
    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }
    
    public void setTemplateContent(byte[] bytes) {
        setTemplateContent(new String(bytes));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((templateContent == null) ? 0 : templateContent.hashCode());
        result = prime * result + ((templateIdentifier == null) ? 0 : templateIdentifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NamedTemplate other = (NamedTemplate) obj;
        if (templateContent == null) {
            if (other.templateContent != null)
                return false;
        } else if (!templateContent.equals(other.templateContent))
            return false;
        if (templateIdentifier == null) {
            if (other.templateIdentifier != null)
                return false;
        } else if (!templateIdentifier.equals(other.templateIdentifier))
            return false;
        return true;
    }
    
}