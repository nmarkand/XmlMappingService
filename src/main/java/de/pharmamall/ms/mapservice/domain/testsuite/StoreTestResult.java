package de.pharmamall.ms.mapservice.domain.testsuite;

public class StoreTestResult {
    
    private Long testId;
    
    public StoreTestResult(Long testId) {
        this.testId = testId;
    }
    
    public Long getTestId() {
        return testId;
    }
    
    public void setTestId(Long testId) {
        this.testId = testId;
    }
}