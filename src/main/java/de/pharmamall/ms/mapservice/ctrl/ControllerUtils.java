package de.pharmamall.ms.mapservice.ctrl;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;
import de.pharmamall.ms.mapservice.domain.TemplateList;

public class ControllerUtils {
    
    private ObjectMapper objectMapper;
    
    public ControllerUtils() {
        objectMapper = new ObjectMapper();
    }
    
    public byte[] getFileContent(final MultipartFile file) {
        byte[] sourceContent;
        try {
            sourceContent = file.getBytes();
        } catch (IOException e) {
            throw new MappingServiceException("Error while getting byte array", e);
        }
        return sourceContent;
    }
    
    public TemplateIdentifier parseTemplateIdentifier(final String jsondata) {
        try {
            return objectMapper.readValue(jsondata, TemplateIdentifier.class);
        } catch (IOException e) {
            throw new MappingServiceException("Could not parse TemplateIdentifier from " + jsondata, e);
        }
    }
    
    public ByteArrayResource toByteArrayResource(final TemplateList responseWrapper) {
        try {
            return new ByteArrayResource(new ObjectMapper().writeValueAsBytes(responseWrapper));
        } catch (JsonProcessingException e) {
            throw new MappingServiceException("Could not be transformed to ByteArrayResource", e);
        }
    }
    
    public TemplateList parseTemplateImportFile(final MultipartFile file) {
        try {
            return new ObjectMapper().readValue(getFileContent(file), TemplateList.class);
        } catch (IOException e) {
            throw new MappingServiceException("MultipartFile could not be parsed to TemplateList", e);
        }
    }
}