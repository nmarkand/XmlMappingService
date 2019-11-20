package de.pharmamall.ms.mapservice.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleMappingException(RuntimeException e, WebRequest request) {
        log.error(e.getMessage(), e);
        Throwable es = e;
        String msg = "";
        while(es != null) {
            msg += es.getMessage() + " ; ";
            es = es.getCause();
        }
        return handleExceptionInternal(e, msg, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}