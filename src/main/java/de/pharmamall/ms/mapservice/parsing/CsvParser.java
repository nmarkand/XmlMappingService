package de.pharmamall.ms.mapservice.parsing;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import de.pharmamall.ms.mapservice.MappingServiceException;

public class CsvParser {
    
    private static final String OPTION_DELIMITER = "csv-delimiter";
    private static final String OPTION_FIRST_ROW_AS_HEADER= "csv-first-row-as-header";
    public static final List<String> SYNTAX_OPTIONS = Arrays.asList(new String[] {OPTION_DELIMITER,OPTION_FIRST_ROW_AS_HEADER}); 

    public List<CSVRecord> parseCsvStringWithDefaultFormat(String csv) {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        // .withHeader(HEADERS)
        // .withFirstRecordAsHeader()
        return parseCsvString(csvFormat, csv);
    }
    
    public List<CSVRecord> parseCsvString(CSVFormat csvFormat, String csv) {
        return parseCsv(csvFormat, new StringReader(csv));
    }

    private List<CSVRecord> parseCsv(CSVFormat csvFormat, Reader in) {
        try {
            return csvFormat.parse(in).getRecords();
        } catch (IOException e) {
            throw new RuntimeException("Could not parse csv");
        }
    }

    public List<CSVRecord> parseCsvStringWithOptions(Map<String, String> options, String csv) {
        return parseCsvString(buildCsvFormat(options),csv);
    }

    private CSVFormat buildCsvFormat(Map<String, String> options) {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        for(Entry<String, String> option : options.entrySet()) {
            if(OPTION_DELIMITER.equals(option.getKey())) {
                csvFormat = csvFormat.withDelimiter(option.getValue().charAt(0));
            }
            else if(OPTION_FIRST_ROW_AS_HEADER.equals(option.getKey())) {
                if(Boolean.valueOf(option.getValue())) {
                    csvFormat = csvFormat.withFirstRecordAsHeader();
                }
            }
            else {
                throw new MappingServiceException("unknown csv option " + option.getKey());
            }
        }
        return csvFormat;
    }
}