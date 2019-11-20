package de.pharmamall.ms.mapservice.parsing;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.pharmamall.ms.mapservice.MappingServiceException;

public class JsonParser {
    
    private static final String OPTION_JSON_TYPE = "json-type";
    public static final List<String> SYNTAX_OPTIONS = Arrays.asList(new String[] {OPTION_JSON_TYPE}); 
    
    @SuppressWarnings("unchecked")
    public Object parseAsJsonType(byte[] source, Map<String, String> options) {
        return readObjectMapperValue(source, getJsonClassType(options,new String(source)));
    }

    @SuppressWarnings("rawtypes")
    private Class getJsonClassType(Map<String, String> options, String source) {
        String type = options.get(OPTION_JSON_TYPE);
        if(type == null) {
            type = "auto";
        }
        Class jsonClass = null;
        switch(type) {
            case "auto" : jsonClass = getJsonAutoClass(source); break;
            case "map" : jsonClass = Map.class; break;
            case "list" : jsonClass = List.class; break;
            default : throw new MappingServiceException("unknown json type " + type);
        }
        return jsonClass;
    }
    
    @SuppressWarnings("rawtypes")
    // @VisibleForTesting
    protected Class getJsonAutoClass(String source) {
        OptionalInt opener = source.chars().filter(c -> c == '{' || c =='[').findFirst();
        if(! opener.isPresent()) {
            throw new MappingServiceException("No json opener char found in " + source);
        }
        if(opener.getAsInt() == '[') {
            return List.class;                
        }
        else {
            return Map.class;
        }
    }
    
    private <T> T readObjectMapperValue(byte[] source, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(source, clazz);
        } catch (IOException e) {
            throw new MappingServiceException("Error in parsing json from " + new String(source));
        }
    }    
}