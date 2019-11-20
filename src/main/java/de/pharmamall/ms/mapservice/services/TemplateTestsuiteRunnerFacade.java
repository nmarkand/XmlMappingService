package de.pharmamall.ms.mapservice.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.freemarker.FreemarkerTemplateApplier;
import de.pharmamall.ms.mapservice.xmlunit.XmlComparator;
import freemarker.template.Template;

@Service
public class TemplateTestsuiteRunnerFacade {
    
    private TemplateTestsuiteService testsuiteService;
    private TemplateLoaderService templateLoaderService;
    private FreemarkerTemplateApplier freemarkerService;
    
    @Autowired
    public TemplateTestsuiteRunnerFacade(TemplateTestsuiteService testsuiteService, TemplateLoaderService templateLoaderService) {
        this.testsuiteService = testsuiteService;
        this.templateLoaderService = templateLoaderService;
        
        freemarkerService = new FreemarkerTemplateApplier();
    }
    
    // @VisibleForTesting
    protected TemplateTestsuiteRunnerFacade(TemplateTestsuiteService testsuiteService, TemplateLoaderService templateLoaderService,
            FreemarkerTemplateApplier freemarkerService, XmlComparator xmlComparator) {
        this.testsuiteService = testsuiteService;
        this.templateLoaderService = templateLoaderService;
        
        this.freemarkerService = freemarkerService;
    }
    
    public Template loadTemplate(TemplateIdentifier templateIdentifier) {
        return templateLoaderService.loadTemplate(templateIdentifier);
    }
    
    public List<XmlMappingTestsuiteEntry> loadTestsForTemplate(TemplateIdentifier templateIdentifier) {
        return testsuiteService.loadTestsForTemplate(templateIdentifier);
    }
    
    public XmlMappingTestsuiteEntry loadTestById(long testId) {
        return testsuiteService.loadTestById(testId);
    }
    
    public byte[] transform(byte[] sourceContent, Template template) {
        return freemarkerService.transform(sourceContent, template);
    }
    
    public List<String> xmlCompare(byte[] expected, byte[] actual, boolean ignoreWhitespace) {
        XmlComparator xmlComparator = new XmlComparator();
        xmlComparator.setIgnoreWhiteSpace(ignoreWhitespace);
        
        Diff xmlUnitDiff = xmlComparator.xmlCompare(expected, actual);
        
        Stream<Difference> diffStream = StreamSupport.stream(xmlUnitDiff.getDifferences().spliterator(), false);
        return diffStream.map(xmlComparator::makeDiffMsg).collect(Collectors.toList());
    }
}