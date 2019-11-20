package de.pharmamall.ms.mapservice.domain.testsuite;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

public class RunTestSuiteRequest {
    
    private TemplateIdentifier templateIdentifier;
    private RunTestParams testParams;
    
    public TemplateIdentifier getTemplateIdentifier() {
        return templateIdentifier;
    }
    
    public void setTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }
    
    public RunTestParams getTestParams() {
        return testParams;
    }
    
    public void setTestParams(RunTestParams testParams) {
        this.testParams = testParams;
    }
    
    @Override
    public String toString() {
        return "RunTestSuiteRequest [templateIdentifier=" + templateIdentifier + ", testParams=" + testParams + "]";
    }
}