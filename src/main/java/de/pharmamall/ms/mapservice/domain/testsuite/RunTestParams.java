package de.pharmamall.ms.mapservice.domain.testsuite;

public class RunTestParams {
    
    private boolean isIgnoreWhitespace;
    
    public boolean getIgnoreWhitespace() {
        return isIgnoreWhitespace;
    }
    
    public void setIgnoreWhitespace(boolean isIgnoreWhitespace) {
        this.isIgnoreWhitespace = isIgnoreWhitespace;
    }
    
    @Override
    public String toString() {
        return "RunTestParams [isIgnoreWhitespace=" + isIgnoreWhitespace + "]";
    }
}