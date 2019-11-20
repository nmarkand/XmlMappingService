package de.pharmamall.ms.mapservice.messaging.config;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class ConnectionFactoryConfig {
    @Value("${spring.activemq.broker-url}")
    String brokerUrl;
    
    @Value("${spring.activemq.user}")
    String userName;
    
    @Value("${spring.activemq.password}")
    String password;
    
    @Value("${spring.activemq.topic-name}")
    private String topicName;
    
    @Bean
    public Topic topic() {
        return new ActiveMQTopic(topicName);
    }
    
    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(userName, password, brokerUrl);
    }
    
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setPubSubDomain(true);
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMessageConverter(jacksonJmsMessageConverter());
        return containerFactory;
    }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setMessageConverter(jacksonJmsMessageConverter());
        template.setPubSubDomain(true);
        return template;
    }
    
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}