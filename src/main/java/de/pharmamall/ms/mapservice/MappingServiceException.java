package de.pharmamall.ms.mapservice;

public class MappingServiceException extends RuntimeException {
    private static final long serialVersionUID = -3155056304831037735L;
    
    public MappingServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public MappingServiceException(String message) {
        super(message);
    }
}