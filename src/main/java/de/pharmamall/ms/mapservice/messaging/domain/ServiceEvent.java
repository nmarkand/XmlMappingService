package de.pharmamall.ms.mapservice.messaging.domain;

import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

public class ServiceEvent {
    
    private String event;
    private TemplateIdentifier templateIdentifier;
    
    public ServiceEvent() {
    }
    
    public ServiceEvent(String event, TemplateIdentifier templateIdentifier) {
        this.event = event;
        this.templateIdentifier = templateIdentifier;
    }
    
    public String getEvent() {
        return event;
    }
    
    public void setEvent(String event) {
        this.event = event;
    }
    
    public TemplateIdentifier getTemplateIdentifier() {
        return templateIdentifier;
    }
    
    public void setTemplateIdentifier(TemplateIdentifier templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }
    
    @Override
    public String toString() {
        return "ServiceEvent [event=" + event + ", templateIdentifier=" + templateIdentifier + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + ((templateIdentifier == null) ? 0 : templateIdentifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceEvent other = (ServiceEvent) obj;
        if (event == null) {
            if (other.event != null)
                return false;
        } else if (!event.equals(other.event))
            return false;
        if (templateIdentifier == null) {
            if (other.templateIdentifier != null)
                return false;
        } else if (!templateIdentifier.equals(other.templateIdentifier))
            return false;
        return true;
    }
}