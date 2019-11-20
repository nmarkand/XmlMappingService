package de.pharmamall.ms.mapservice.domain.testsuite;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;

public class TestContentResult {
    
    private String source;
    private String target;
    
    public TestContentResult(XmlMappingTestsuiteEntry test) {
        setSource(new String(test.getSourceContent()));
        setTarget(new String(test.getTargetContent()));
    }
    
    public String getSource() {
        return source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
}