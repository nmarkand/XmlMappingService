package de.pharmamall.ms.mapservice.freemarker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.parsing.CsvParser;
import de.pharmamall.ms.mapservice.parsing.JsonParser;
import de.pharmamall.ms.mapservice.parsing.SourceSyntax;
import freemarker.ext.dom.NodeModel;

public class FreemarkerModelFactory {
    
    private static final String ROOT_NAME = "doc";
    
    public Map<String, Object> parseSourceToModel(byte[] source, SourceSyntax syntax) {
        switch(syntax.getSyntax()) {
            case XML : return parseXmlSource(source);
            case CSV : return parseCsvSource(source,syntax.getOptions());
            case JSON : return parseJsonSource(source,syntax.getOptions());
            default: throw new MappingServiceException("Unknown source syntax: " + syntax);
        }
    }
    
    private Map<String, Object> parseXmlSource(byte[] sourceContent) {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(sourceContent));
        inputSource.setEncoding(FreemarkerConfigurationProducer.DEFAULT_ENCODING);
        try {
            return makeDocModel(NodeModel.parse(inputSource));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new MappingServiceException("Error in freemarker document parsing", e);
        }
    }
        
    private Map<String, Object> parseCsvSource(byte[] source, Map<String, String> options) {
        return makeDocModel(new CsvParser().parseCsvStringWithOptions(options, new String(source)));
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonSource(byte[] source, Map<String, String> options) {
        return makeDocModel(new JsonParser().parseAsJsonType(source, options));
    }

    private Map<String, Object> makeDocModel(Object model) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put(ROOT_NAME, model);
        return root;
    }
}