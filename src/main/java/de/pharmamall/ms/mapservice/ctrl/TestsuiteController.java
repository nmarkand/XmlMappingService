package de.pharmamall.ms.mapservice.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;
import de.pharmamall.ms.mapservice.domain.testsuite.DeleteTestResult;
import de.pharmamall.ms.mapservice.domain.testsuite.RunTestParams;
import de.pharmamall.ms.mapservice.domain.testsuite.RunTestSuiteRequest;
import de.pharmamall.ms.mapservice.domain.testsuite.StoreTestRequest;
import de.pharmamall.ms.mapservice.domain.testsuite.StoreTestResult;
import de.pharmamall.ms.mapservice.domain.testsuite.TestContentResult;
import de.pharmamall.ms.mapservice.domain.testsuite.TestsuiteResult;
import de.pharmamall.ms.mapservice.domain.testsuite.TestsuiteResult.TestEntryResult;
import de.pharmamall.ms.mapservice.services.TemplateTestsuiteRunner;
import de.pharmamall.ms.mapservice.services.TemplateTestsuiteService;

@Controller
@RestController
@RequestMapping("/testsuite")
public class TestsuiteController {
    
    private TemplateTestsuiteService templateTestsuiteService;
    private TemplateTestsuiteRunner runner;
    
    private static final Logger log = LoggerFactory.getLogger(TestsuiteController.class);
    
    @Autowired
    public TestsuiteController(TemplateTestsuiteService templateTestsuiteService, TemplateTestsuiteRunner runner) {
        this.templateTestsuiteService = templateTestsuiteService;
        this.runner = runner;
    }
    
    @RequestMapping(path = "runForTemplate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestsuiteResult> runForTemplate(@RequestBody RunTestSuiteRequest runSuiteRequest) {
        log.info("runForTemplate " + runSuiteRequest.getTemplateIdentifier());
        long start = System.currentTimeMillis();
        TestsuiteResult result = runner.runTestsFor(runSuiteRequest.getTemplateIdentifier(), runSuiteRequest.getTestParams());
        log.info(String.format("testsuite with %s tests run in %sms, returning result", result.getResults().size(),
                System.currentTimeMillis() - start));
        return ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(path = "test/content/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestContentResult> getTestContent(@PathVariable("id") long testId) {
        log.info("getTestContent for testId " + testId);
        XmlMappingTestsuiteEntry testEntry = templateTestsuiteService.loadTestById(testId);
        log.info("got content, returning data");
        return ResponseEntity.ok().body(new TestContentResult(testEntry));
    }
    
    @RequestMapping(path = "test/run/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestEntryResult> runTest(@PathVariable("id") long testId, @RequestBody RunTestParams testParams) {
        log.info(String.format("runTest with testId %s, params=%s", testId, testParams));
        TestEntryResult result = runner.runTest(testId, testParams);
        log.info("test Run, returning result");
        return ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(path = "test/delete/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteTestResult> deleteTest(@PathVariable("id") long testId) {
        log.info("deleteTest with testId " + testId);
        boolean deleted = templateTestsuiteService.deleteTest(testId);
        
        DeleteTestResult result = new DeleteTestResult(testId);
        result.setDeleted(deleted);
        log.info("test deleted");
        return ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(path = "storeTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StoreTestResult> storeTest(@RequestBody StoreTestRequest storeTestRequest) {
        log.info("storeTest for template " + storeTestRequest.getTemplateIdentifier());
        XmlMappingTestsuiteEntry stored = templateTestsuiteService.storeTest(storeTestRequest.getTemplateIdentifier(),
                storeTestRequest.getSourceBytes(),
                storeTestRequest.getTargetBytes());
        log.info("test stored");
        return ResponseEntity.ok().body(new StoreTestResult(stored.getId()));
    }
}