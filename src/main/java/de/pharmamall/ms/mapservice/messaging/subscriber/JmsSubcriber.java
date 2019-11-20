package de.pharmamall.ms.mapservice.messaging.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.pharmamall.ms.mapservice.messaging.domain.ServiceEvent;
import de.pharmamall.ms.mapservice.services.TemplateLoaderService;

@Component
public class JmsSubcriber {
    
    private static final Logger log = LoggerFactory.getLogger(JmsSubcriber.class);
    
    private TemplateLoaderService templateLoaderService;
    
    @Autowired
    public JmsSubcriber(TemplateLoaderService templateLoaderService) {
        this.templateLoaderService = templateLoaderService;
    }
    
    @JmsListener(id = "templateUncacheEventListener", destination = "${spring.activemq.topic-name}", containerFactory = "jmsListenerContainerFactory")
    public void receive(final ServiceEvent serviceEvent) {
        log.info("Event based uncahing" + " Event " + serviceEvent.getEvent() + " Tempalate " + serviceEvent.getTemplateIdentifier());
        templateLoaderService.uncacheTemplate(serviceEvent.getTemplateIdentifier());
    }
}