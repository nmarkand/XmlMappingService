package de.pharmamall.ms.mapservice.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTemplateVersion;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.version.TemplateVersionListResult;
import de.pharmamall.ms.mapservice.domain.version.VersionContentRequest;
import de.pharmamall.ms.mapservice.domain.version.VersionContentResult;
import de.pharmamall.ms.mapservice.services.TemplateVersionService;

@Controller
@RestController
@RequestMapping("/template/version")
public class TemplateVersionController {
    
    private TemplateVersionService versionService;
    
    private static final Logger log = LoggerFactory.getLogger(TemplateVersionController.class);
    
    @Autowired
    public TemplateVersionController(TemplateVersionService versionService) {
        this.versionService = versionService;
    }
    
    @RequestMapping(path = "list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TemplateVersionListResult>> getTemplateVersionListByTemplateIdentifier(
            @RequestBody TemplateIdentifier templateIdentifier) {
        log.info("listTemplateVersionsByTemplateIdentifier with " + templateIdentifier);
        
        List<TemplateVersionListResult> templateVersionListResult = templateVersionsToShortlist(
                versionService.getXmlMappingTemplateVersions(templateIdentifier));
        
        log.info("got TemplateVersionListResult, returning list");
        return ResponseEntity.ok().body(templateVersionListResult);
    }
    
    @RequestMapping(path = "content", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionContentResult> getVersionContent(@RequestBody VersionContentRequest versionContentRequest) {
        log.info("getVersionContent for " + versionContentRequest);
        
        XmlMappingTemplateVersion version = versionService.getXmlMappingTemplateVersion(versionContentRequest.getTemplateIdentifier(),
                versionContentRequest.getVersionNumber());
        VersionContentResult result = new VersionContentResult(version.getTemplateContent());
        
        log.info("got version content, returning data");
        return ResponseEntity.ok().body(result);
    }
    
    public List<TemplateVersionListResult> templateVersionsToShortlist(final List<XmlMappingTemplateVersion> xmlMappingTemplateVersionList) {
        return xmlMappingTemplateVersionList.stream().map(v -> new TemplateVersionListResult(v.getVersionCounter(), v.getValidTo()))
                .sorted((v1, v2) -> v2.getVersionNumber().compareTo(v1.getVersionNumber())).collect(Collectors.toList());
    }
}