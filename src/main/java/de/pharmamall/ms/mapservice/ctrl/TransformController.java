package de.pharmamall.ms.mapservice.ctrl;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.TransformRequest;
import de.pharmamall.ms.mapservice.domain.TransformResult;
import de.pharmamall.ms.mapservice.freemarker.FreemarkerConfigurationProducer;

@Controller
@RestController
@RequestMapping("/")
public class TransformController {
    
    private TransformControllerDelegate delegate;
    private ControllerUtils controllerUtils;
    
    private static final Logger log = LoggerFactory.getLogger(TransformController.class);
    
    public TransformController(TransformControllerDelegate delegate) {
        this.delegate = delegate;
        controllerUtils = new ControllerUtils();
    }
    
    
    @RequestMapping(path = "transformFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> transformFile(@RequestParam("identifier") String identifier, @RequestParam("file") MultipartFile file) {
        TemplateIdentifier templateIdentifier = controllerUtils.parseTemplateIdentifier(identifier);
        log.info("transformFile with template " + templateIdentifier);
        
        byte[] sourceContent = controllerUtils.getFileContent(file);
        byte[] fileData = delegate.map(sourceContent, templateIdentifier);
        
        dumpDetectedEncoding("sourceContent", sourceContent);
        dumpDetectedEncoding("result", fileData);
        
        log.info("transformed, returning response file");
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + "mapped.xml; charset=" + FreemarkerConfigurationProducer.DEFAULT_ENCODING)
                .body(new ByteArrayResource(fileData));
    }
    
    @RequestMapping(path = "transform", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransformResult> transform(@RequestBody TransformRequest transformRequest) {
        TemplateIdentifier templateIdentifier = transformRequest.getTemplateIdentifier();
        log.info("transformFile with template " + templateIdentifier);
        
        byte[] sourceContent = transformRequest.getSourceBytes();
        byte[] transformed = delegate.map(sourceContent, templateIdentifier);
        
        return respondTransformResult(transformed);
    }
    
    @RequestMapping(path = "transformFromTemplateString", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransformResult> transformFromTemplateString(@RequestBody TransformRequest transformRequest) {
        log.info("transformFromTemplateString");
        
        byte[] templateContent = transformRequest.getTemplateBytes();
        byte[] sourceContent = transformRequest.getSourceBytes();
        byte[] transformed = delegate.map(templateContent, sourceContent);
        
        return respondTransformResult(transformed);
    }
    
    private ResponseEntity<TransformResult> respondTransformResult(byte[] transformed) {
        log.info("transformed, returning TransformResult");
        return ResponseEntity.ok().body(new TransformResult(transformed));
    }
    
    private void dumpDetectedEncoding(String identifier, byte[] content) {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(content);
        CharsetMatch possibleEncoding = detector.detect();
        log.info(identifier + ": " + possibleEncoding.getName() + " - confidence: " + possibleEncoding.getConfidence());
        
    }
}