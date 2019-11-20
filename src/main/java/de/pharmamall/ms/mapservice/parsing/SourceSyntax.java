package de.pharmamall.ms.mapservice.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.pharmamall.ms.mapservice.MappingServiceException;

public class SourceSyntax {
    
    public enum SOURCE_SYNTAX {
        XML,
        CSV, // no headers, seperator = ,
        JSON, // json-type = Map
        // EDI
        ;

        public static SourceSyntax.SOURCE_SYNTAX fromString(String name) {
            if(name == null || name.isEmpty()) {
                return SOURCE_SYNTAX.XML;
            }        
            else {
                try {
                    return SOURCE_SYNTAX.valueOf(name.toUpperCase());
                } catch(IllegalArgumentException e) {                   
                    throw new MappingServiceException("Unknown source syntax: " + name);
                }
            }
        }
    }
    
    public static final List<String> SYNTAX_OPTIONS = new ArrayList<>();
    static {
        SYNTAX_OPTIONS.addAll(CsvParser.SYNTAX_OPTIONS);
        SYNTAX_OPTIONS.addAll(JsonParser.SYNTAX_OPTIONS);
    }
    
    private SourceSyntax.SOURCE_SYNTAX syntax = SOURCE_SYNTAX.XML;
    private Map<String,String> options = new HashMap<>();
    
    public SourceSyntax() {}
    
    public SourceSyntax(SourceSyntax.SOURCE_SYNTAX syntax) {
        this.setSyntax(syntax);
    }
    
    public SourceSyntax(SourceSyntax.SOURCE_SYNTAX syntax, Map<String, String> options) {
        this.setSyntax(syntax);
        this.setOptions(options);
    }
    
    public SourceSyntax(String syntaxName, Map<String, String> options) {
        this(SourceSyntax.SOURCE_SYNTAX.fromString(syntaxName),options);
    }
    
    public void addOption(String key, String value) {
        getOptions().put(key,value);
    }

    public SourceSyntax.SOURCE_SYNTAX getSyntax() {
        return syntax;
    }

    public void setSyntax(SourceSyntax.SOURCE_SYNTAX syntax) {
        this.syntax = syntax;
    }

    public Map<String,String> getOptions() {
        return options;
    }

    public void setOptions(Map<String,String> options) {
        this.options = options;
    }
}