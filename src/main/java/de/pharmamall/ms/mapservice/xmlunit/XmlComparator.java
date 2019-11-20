package de.pharmamall.ms.mapservice.xmlunit;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

public class XmlComparator {
    
    private boolean isIgnoreWhiteSpace;
    
    public Diff xmlCompare(byte[] expected, byte[] actual) {
        return xmlCompare(new String(expected), new String(actual));
    }
    
    private Diff xmlCompare(String expected, String actual) {
        DiffBuilder diffBuilder = DiffBuilder.compare(expected).withTest(actual);
        // diffBuilder = diffBuilder.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName));
        diffBuilder = diffBuilder.checkForSimilar();
        if (isIgnoreWhiteSpace) {
            diffBuilder = diffBuilder.ignoreWhitespace();
        }
        return diffBuilder.build();
    }
    
    public String makeDiffMsg(Difference diff) {
        return diff.getComparison().getControlDetails().getXPath() + ": " + diff.getComparison().getControlDetails().getValue() + " <> "
                + diff.getComparison().getTestDetails().getValue();
    }
    
    public boolean isIgnoreWhiteSpace() {
        return isIgnoreWhiteSpace;
    }
    
    public void setIgnoreWhiteSpace(boolean isIgnoreWhiteSpace) {
        this.isIgnoreWhiteSpace = isIgnoreWhiteSpace;
    }
}