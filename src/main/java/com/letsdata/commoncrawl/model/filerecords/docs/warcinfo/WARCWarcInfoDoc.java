package com.letsdata.commoncrawl.model.filerecords.docs.warcinfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WARCWarcInfoDoc extends WarcInfoDoc {
    private static final Logger logger = LoggerFactory.getLogger(WARCWarcInfoDoc.class);
    private final String isPartOf;
    private final String publisher;
    private final String description;
    private final String operator;
    private final String hostname;
    private final String software;
    private final String robots;
    private final String format;
    private final String conformsTo;

    @JsonCreator
    public WARCWarcInfoDoc(@JsonProperty("isPartOf") String isPartOf, @JsonProperty("publisher") String publisher, @JsonProperty("description") String description, @JsonProperty("operator") String operator, @JsonProperty("hostname") String hostname, @JsonProperty("software") String software, @JsonProperty("robots") String robots, @JsonProperty("format") String format, @JsonProperty("conformsTo") String conformsTo) {
        super(DocumentRecordTypes.WARC_WARCINFO_PAYLOAD);
        this.isPartOf = isPartOf;
        this.publisher = publisher;
        this.description = description;
        this.operator = operator;
        this.hostname = hostname;
        this.software = software;
        this.robots = robots;
        this.format = format;
        this.conformsTo = conformsTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WARCWarcInfoDoc that = (WARCWarcInfoDoc) o;
        return Objects.equals(isPartOf, that.isPartOf) &&
                Objects.equals(publisher, that.publisher) &&
                Objects.equals(description, that.description) &&
                Objects.equals(operator, that.operator) &&
                Objects.equals(hostname, that.hostname) &&
                Objects.equals(software, that.software) &&
                Objects.equals(robots, that.robots) &&
                Objects.equals(format, that.format) &&
                Objects.equals(conformsTo, that.conformsTo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(isPartOf, publisher, description, operator, hostname, software, robots, format, conformsTo);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCWarcInfoDoc{");
        sb.append("isPartOf='").append(isPartOf).append('\'');
        sb.append(", publisher='").append(publisher).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", operator='").append(operator).append('\'');
        sb.append(", hostname='").append(hostname).append('\'');
        sb.append(", software='").append(software).append('\'');
        sb.append(", robots='").append(robots).append('\'');
        sb.append(", format='").append(format).append('\'');
        sb.append(", conformsTo='").append(conformsTo).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getIsPartOf() {
        return isPartOf;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDescription() {
        return description;
    }

    public String getOperator() {
        return operator;
    }

    public String getHostname() {
        return hostname;
    }

    public String getSoftware() {
        return software;
    }

    public String getRobots() {
        return robots;
    }

    public String getFormat() {
        return format;
    }

    public String getConformsTo() {
        return conformsTo;
    }

    public static WARCWarcInfoDoc getWARCWarcInfoDoc(byte[] byteArr, int startIndex, int endIndex) throws Exception {
        int currIndex = startIndex;
        String isPartOf = null;
        String publisher = null;
        String description  = null;
        String operator = null;
        String hostname = null;
        String software = null;
        String robots = null;
        String format = null;
        String conformsTo = null;
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
                case "software": {
                    software = value;
                    break;
                }
                case "isPartOf": {
                    isPartOf = value;
                    break;
                }

                case "publisher": {
                    publisher = value;
                    break;
                }
                case "description": {
                    description = value;
                    break;
                }
                case "operator": {
                    operator = value;
                    break;
                }
                case "conformsTo": {
                    conformsTo = value;
                    break;
                }
                case "hostname": {
                    hostname = value;
                    break;
                }
                case "robots": {
                    robots = value;
                    break;
                }
                case "format": {
                    format = value;
                    break;
                }
                default:{
                    logger.error("unknown key in WARCINFO payload - key: {} value: {}", key, value);
                    throw new RuntimeException("unknown key in WARCINFO payload");
                }
            }
        } while (currIndex < endIndex);
        return new WARCWarcInfoDoc(isPartOf, publisher, description, operator, hostname, software, robots, format, conformsTo);
    }
}
