package de.pharmamall.ms.mapservice.db.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.pharmamall.ms.mapservice.db.entities.XmlMappingTestsuiteEntry;

public interface TestsuiteEntryRepository extends JpaRepository<XmlMappingTestsuiteEntry, Long> {
    
    List<XmlMappingTestsuiteEntry> findXmlMappingTestsuiteEntryByVendorIdAndSourceTypeAndTargetTypeOrderByIdDesc(Long vendorId, String sourceType,
            String targetType);
}