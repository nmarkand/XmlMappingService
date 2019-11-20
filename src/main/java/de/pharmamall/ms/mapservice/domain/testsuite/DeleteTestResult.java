package de.pharmamall.ms.mapservice.domain.testsuite;

public class DeleteTestResult {
    
    private Long testId;
    private boolean deleted;
    
    public DeleteTestResult() {
    }
    
    public DeleteTestResult(long testId) {
        this.testId = testId;
    }
    
    public Long getTestId() {
        return testId;
    }
    
    public boolean getDeleted() {
        return deleted;
    }
    
    public void setTestId(Long testId) {
        this.testId = testId;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}