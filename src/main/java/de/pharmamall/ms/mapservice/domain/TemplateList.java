package de.pharmamall.ms.mapservice.domain;

import java.util.List;

public class TemplateList {
    
    List<NamedTemplate> templates;
    
    public TemplateList() {
    }
    
    public TemplateList(List<NamedTemplate> templates) {
        this.templates = templates;
    }
    
    public List<NamedTemplate> getTemplates() {
        return templates;
    }
    
    public void setTemplates(List<NamedTemplate> templates) {
        this.templates = templates;
    }
}