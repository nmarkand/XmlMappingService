package de.pharmamall.ms.mapservice.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.testsuite.RunTestParams;
import de.pharmamall.ms.mapservice.domain.testsuite.TestsuiteResult;
import de.pharmamall.ms.mapservice.domain.testsuite.TestsuiteResult.TestEntryResult;
import de.pharmamall.ms.mapservice.domain.testsuite.TestsuiteResult.TestEntryResult.OUTCOME;
import freemarker.template.Template;

@Service
public class TemplateTestsuiteRunner {
    
    private TemplateTestsuiteRunnerFacade facade;
    
    private static final Logger log = LoggerFactory.getLogger(TemplateTestsuiteRunner.class);
    
    @Autowired
    public TemplateTestsuiteRunner(TemplateTestsuiteRunnerFacade facade) {
        this.facade = facade;
    }
    
    public TestsuiteResult runTestsFor(TemplateIdentifier templateIdentifier, RunTestParams testParams) {
        log.info("runTestsFor " + templateIdentifier);
        Template template = facade.loadTemplate(templateIdentifier);
        List<XmlMappingTestsuiteEntry> tests = facade.loadTestsForTemplate(templateIdentifier);
        
        TestsuiteResult result = new TestsuiteResult();
        for (XmlMappingTestsuiteEntry test : tests) {
            result.addResult(runTestEntry(template, test, testParams));
        }
        return result;
    }
    
    public TestEntryResult runTest(long testId, RunTestParams testParams) {
        log.info("runTest " + testId);
        XmlMappingTestsuiteEntry entry = facade.loadTestById(testId);
        Template template = facade.loadTemplate(entry.getTemplateIdentifier());
        return runTestEntry(template, entry, testParams);
    }
    
    // @VisibleForTesting
    protected TestEntryResult runTestEntry(Template template, XmlMappingTestsuiteEntry entry, RunTestParams testParams) {
        TestEntryResult result = new TestEntryResult(entry);
        
        byte[] transformed;
        try {
            transformed = facade.transform(entry.getSourceContent(), template);
        } catch (Exception e) {
            setResultForError(result, e);
            return result;
        }
        result.setTransformed(new String(transformed));
        setResultFromCompareDiffs(result, facade.xmlCompare(entry.getTargetContent(), transformed, testParams.getIgnoreWhitespace()));
        
        return result;
    }
    
    private void setResultFromCompareDiffs(TestEntryResult result, List<String> diffs) {
        result.setOutcome(diffs.isEmpty() ? OUTCOME.PASSED : OUTCOME.FAILED);
        result.setDiffs(diffs);
        log.info(String.format("Test %s run: %s", result.getTestId(), result.getOutcome()));
    }
    
    private void setResultForError(TestEntryResult result, Throwable e) {
        log.error("error running test " + result.getTestId(), e);
        result.setOutcome(OUTCOME.ERROR);
        String msg = "";
        while (e != null) {
            msg += e.getMessage() + " ; ";
            e = e.getCause();
        }
        result.setErrorMessage(msg);
    }
}