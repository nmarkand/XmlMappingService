package de.pharmamall.ms.mapservice.ctrl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.domain.DeleteTemplateResult;
import de.pharmamall.ms.mapservice.domain.NamedTemplate;
import de.pharmamall.ms.mapservice.domain.TemplateContent;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.TemplateListFilter;
import de.pharmamall.ms.mapservice.domain.TemplateList;
import de.pharmamall.ms.mapservice.domain.TemplateListStoreResponse;
import de.pharmamall.ms.mapservice.domain.XmlMappingTemplateStoreResult;

@Controller
@RestController
@RequestMapping("/template")
public class TemplateStorageController {
    
    private TemplateStorageControllerDelegate delegate;
    private ControllerUtils controllerUtils;
    
    private static final Logger log = LoggerFactory.getLogger(TemplateStorageController.class);
    
    @Autowired
    public TemplateStorageController(TemplateStorageControllerDelegate delegate) {
        this.delegate = delegate;
        controllerUtils = new ControllerUtils();
    }
    
    @RequestMapping(path = "upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<XmlMappingTemplateStoreResult> uploadTemplate(@RequestParam("identifier") String identifier,
            @RequestParam("file") MultipartFile file) {
        TemplateIdentifier templateIdentifier = controllerUtils.parseTemplateIdentifier(identifier);
        log.info("uploadTemplate with " + templateIdentifier);
        
        byte[] templateContent = controllerUtils.getFileContent(file);
        return storeTemplateAndRespond(templateIdentifier, templateContent);
    }
    
    @RequestMapping(path = "store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<XmlMappingTemplateStoreResult> storeTemplate(@RequestBody NamedTemplate template) {
        TemplateIdentifier templateIdentifier = template.getTemplateIdentifier();
        log.info("storeTemplate with " + templateIdentifier);
        return storeTemplateAndRespond(template);
    }
    
    @RequestMapping(path = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<XmlMappingTemplateStoreResult> createTemplate(@RequestBody NamedTemplate template) {
        TemplateIdentifier templateIdentifier = template.getTemplateIdentifier();
        log.info("createTemplate with " + templateIdentifier);
        if (delegate.templateExists(templateIdentifier)) {
            throw new MappingServiceException("Template already exists " + templateIdentifier);
        }
        byte[] templateContent = template.getBytes();
        return storeTemplateAndRespond(templateIdentifier, templateContent);
    }
    
    private ResponseEntity<XmlMappingTemplateStoreResult> storeTemplateAndRespond(TemplateIdentifier templateIdentifier, byte[] templateContent) {
        return storeTemplateAndRespond(new NamedTemplate(templateIdentifier, new String(templateContent)));
    }
    
    private ResponseEntity<XmlMappingTemplateStoreResult> storeTemplateAndRespond(NamedTemplate template) {
        XmlMappingTemplateStoreResult result = new XmlMappingTemplateStoreResult(delegate.storeTemplate(template).getId());
        log.info("stored template with " + template.getTemplateIdentifier());
        return ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(path = "list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TemplateIdentifier>> listTemplates() {
        log.info("listTemplates");
        List<TemplateIdentifier> templates = delegate.listTemplates();
        log.info("got templates, returning list");
        return ResponseEntity.ok().body(templates);
    }
    
    @RequestMapping(path = "content", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateContent> getTemplateContent(@RequestBody TemplateIdentifier templateIdentifier) {
        log.info("getTemplateContent for " + templateIdentifier);
        TemplateContent content = delegate.getTemplateContent(templateIdentifier);
        log.info("got template content, returning data" + templateIdentifier);
        return ResponseEntity.ok().body(content);
    }
    
    @RequestMapping(path = "delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteTemplateResult> deleteTemplate(@RequestBody TemplateIdentifier templateIdentifier) {
        log.info("deleteTemplate with " + templateIdentifier);
        DeleteTemplateResult result = delegate.deleteTemplate(templateIdentifier);
        log.info("template deleted");
        return ResponseEntity.ok().body(result);
    }
    
    @RequestMapping(path = "search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TemplateIdentifier>> listTemplatesByTemplateListFilter(@RequestBody TemplateListFilter templateListFilter) {
        log.info("listTemplatesByTemplateListFilter");
        List<TemplateIdentifier> templates = delegate.listTemplatesByTemplateListFilter(templateListFilter);
        log.info("got templates, returning list");
        return ResponseEntity.ok().body(templates);
    }
    
    @RequestMapping(path = "export", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> exportTemplates(@RequestBody TemplateListFilter templateListFilter) {
        log.info("export templates by templateListFilter " + templateListFilter);
        List<NamedTemplate> responseList = delegate.listNamedTemplatesByTemplateListFilter(templateListFilter);
        log.info("Transform template list to response, returning list");
        String outfile = "templates_" + "" + new SimpleDateFormat("YYYYMMdd").format(new Date()) + ".json";
        return ResponseEntity.ok().header("Content-Disposition", "inline;filename=" + outfile)
                .body(controllerUtils.toByteArrayResource(new TemplateList(responseList)));
    }

    @RequestMapping(path = "import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TemplateListStoreResponse> importTemplates(@RequestParam("templateFile") MultipartFile file) throws IOException {
        log.info("import template");
        TemplateList templates = controllerUtils.parseTemplateImportFile(file);
        log.info("Storing XmlMappingTemplate list");
        delegate.storeTemplates(templates);
        log.info("Returning response");
        return ResponseEntity.ok().body(new TemplateListStoreResponse("Stored", templates.getTemplates().size()));
    }
}