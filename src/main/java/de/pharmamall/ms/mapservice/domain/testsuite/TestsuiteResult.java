package de.pharmamall.ms.mapservice.domain.testsuite;

import java.util.ArrayList;
import java.util.List;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;

public class TestsuiteResult {
    
    public static class TestEntryResult {
        
        public TestEntryResult(XmlMappingTestsuiteEntry entry) {
            setTestId(entry.getId());
        }
        
        public enum OUTCOME {
            PASSED,
            FAILED,
            ERROR
        }
        
        private Long testId;
        private OUTCOME outcome = OUTCOME.FAILED;
        private String transformed;
        private List<String> diffs = new ArrayList<String>();
        private String errorMessage;
        
        public Long getTestId() {
            return testId;
        }
        
        public void setTestId(Long testId) {
            this.testId = testId;
        }
        
        public OUTCOME getOutcome() {
            return outcome;
        }
        
        public void setOutcome(OUTCOME outcome) {
            this.outcome = outcome;
        }
        
        public String getTransformed() {
            return transformed;
        }
        
        public void setTransformed(String transformed) {
            this.transformed = transformed;
        }
        
        public List<String> getDiffs() {
            return diffs;
        }
        
        public void setDiffs(List<String> diffs) {
            this.diffs = diffs;
        }
        
        public void addDiff(String diff) {
            diffs.add(diff);
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        @Override
        public String toString() {
            return "TestEntryResult [testId=" + testId + ", outcome=" + outcome + "]";
        }
    }
    
    private List<TestEntryResult> results = new ArrayList<TestEntryResult>();
    
    public void addResult(TestEntryResult result) {
        results.add(result);
    }
    
    public List<TestEntryResult> getResults() {
        return results;
    }
    
    @Override
    public String toString() {
        return "TestsuiteResult [results=" + results + "]";
    }
}