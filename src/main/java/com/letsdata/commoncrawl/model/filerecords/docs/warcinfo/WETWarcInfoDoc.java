package com.letsdata.commoncrawl.model.filerecords.docs.warcinfo;

import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WETWarcInfoDoc extends WarcInfoDoc {
    private static final Logger logger = LoggerFactory.getLogger(WETWarcInfoDoc.class);

    private final String softwareInfo;
    private final Date extractedDate;
    private final String robots;
    private final String isPartOf;
    private final String operator;
    private final String description;
    private final String publisher;

    public WETWarcInfoDoc(String softwareInfo, Date extractedDate, String robots, String isPartOf, String operator, String description, String publisher) {
        super(DocumentRecordTypes.WET_WARCINFO_PAYLOAD);
        this.softwareInfo = softwareInfo;
        this.extractedDate = extractedDate;
        this.robots = robots;
        this.isPartOf = isPartOf;
        this.operator = operator;
        this.description = description;
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WETWarcInfoDoc that = (WETWarcInfoDoc) o;
        return Objects.equals(softwareInfo, that.softwareInfo) &&
                Objects.equals(extractedDate, that.extractedDate) &&
                Objects.equals(robots, that.robots) &&
                Objects.equals(isPartOf, that.isPartOf) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(description, that.description) &&
                Objects.equals(publisher, that.publisher);
    }

    @Override
    public int hashCode() {

        return Objects.hash(softwareInfo, extractedDate, robots, isPartOf, operator, description, publisher);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WETWarcInfoDoc{");
        sb.append("softwareInfo='").append(softwareInfo).append('\'');
        sb.append(", extractedDate=").append(extractedDate);
        sb.append(", robots='").append(robots).append('\'');
        sb.append(", isPartOf='").append(isPartOf).append('\'');
        sb.append(", operator='").append(operator).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getSoftwareInfo() {
        return softwareInfo;
    }

    public Date getExtractedDate() {
        return extractedDate;
    }

    public String getRobots() {
        return robots;
    }

    public String getIsPartOf() {
        return isPartOf;
    }

    public String getOperator() {
        return operator;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public static WETWarcInfoDoc getWETWarcInfoDoc(byte[] byteArr, int startIndex, int endIndex) throws Exception {
        int currIndex = startIndex;
        String softwareInfo = null;
        Date extractedDate = null;
        String robots = null;
        String isPartOf = null;
        String operator = null;
        String description = null;
        String publisher = null;
        do {
            Matcher.KeyValueResult keyValueResult = Matcher.getKeyValueFromLine(byteArr, currIndex, endIndex, WARCFileReaderConstants.WARC_HEADER_LINE_END_PATTERN, WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN);
            String key = keyValueResult.getKey();
            String value = keyValueResult.getValue();
            currIndex = keyValueResult.getNextIndex();

            if (key == null || value == null || currIndex > endIndex) {
                ValidationUtils.validateAssertCondition(key == null && value == null && currIndex > endIndex, "key value should both be null and nextIndex should be greater than endIndex", key, value);
                break;
            }

            switch (key) {
                case "Software-Info": {
                    softwareInfo = value;
                    break;
                }
                case "Extracted-Date": {
                    try {
                        extractedDate = new SimpleDateFormat(WARCFileReaderConstants.WARC_INFO_EXTRACTED_DATE_FORMAT).parse(value);
                    } catch(ParseException pe) {
                        logger.error("unable to parse extracted date from warcinfo record - date: {}, format: {}", value, WARCFileReaderConstants.WARC_INFO_EXTRACTED_DATE_FORMAT);
                        throw new RuntimeException("unable to parse extracted date from warcinfo record");
                    }
                    break;
                }
                case "robots": {
                    robots = value;
                    break;
                }
                case "isPartOf": {
                    isPartOf = value;
                    break;
                }
                case "operator": {
                    operator = value;
                    break;
                }
                case "description": {
                    description = value;
                    break;
                }
                case "publisher": {
                    publisher = value;
                    break;
                }
                default:{
                    logger.error("unknown warcinfo document key in WET doc - key: {}, value: {}", key, value);
                    throw new RuntimeException("unknown warcinfo document key");
                }
            }
        } while (currIndex < endIndex);
        return new WETWarcInfoDoc(softwareInfo, extractedDate, robots, isPartOf, operator, description, publisher);
    }
}