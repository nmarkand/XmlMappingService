package de.pharmamall.ms.mapservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.pharmamall.ms.mapservice.MappingServiceException;
import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;
import de.pharmamall.ms.mapservice.db.repos.TestsuiteEntryRepository;
import de.pharmamall.ms.mapservice.domain.TemplateIdentifier;

@Service
public class TemplateTestsuiteService {
    
    private TestsuiteEntryRepository repo;
    
    public TemplateTestsuiteService(TestsuiteEntryRepository repo) {
        this.repo = repo;
    }
    
    public List<XmlMappingTestsuiteEntry> loadTestsForTemplate(TemplateIdentifier templateIdentifier) {
        return repo.findXmlMappingTestsuiteEntryByVendorIdAndSourceTypeAndTargetTypeOrderByIdDesc(templateIdentifier.getVendorId(),
                templateIdentifier.getSourceType(), templateIdentifier.getTargetType());
    }
    
    public XmlMappingTestsuiteEntry storeTest(TemplateIdentifier templateIdentifier, byte[] sourceBytes, byte[] targetBytes) {
        XmlMappingTestsuiteEntry entry = new XmlMappingTestsuiteEntry(templateIdentifier);
        entry.setSourceContent(sourceBytes);
        entry.setTargetContent(targetBytes);
        return repo.save(entry);
    }
    
    public XmlMappingTestsuiteEntry loadTestById(long id) {
        Optional<XmlMappingTestsuiteEntry> optEntry = repo.findById(id);
        if (!optEntry.isPresent()) {
            throw new MappingServiceException("Could not load test with id " + id);
        }
        return optEntry.get();
    }
    
    public boolean deleteTest(long testId) {
        repo.deleteById(testId);
        return true;
    }
}