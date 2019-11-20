package de.pharmamall.ms.mapservice.domain;

public class TransformResult {
    
    private String transformed;
    
    public TransformResult(byte[] transformed) {
        this.transformed = new String(transformed);
    }
    
    public String getTransformed() {
        return transformed;
    }
    
    public void setTransformed(String transformed) {
        this.transformed = transformed;
    }
    
}