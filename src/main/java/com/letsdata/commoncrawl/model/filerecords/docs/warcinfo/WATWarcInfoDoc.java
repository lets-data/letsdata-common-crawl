package com.letsdata.commoncrawl.model.filerecords.docs.warcinfo;

import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WATWarcInfoDoc extends WarcInfoDoc {
    private static final Logger logger = LoggerFactory.getLogger(WATWarcInfoDoc.class);

    private final String softwareInfo;
    private final Date extractedDate;
    private final InetAddress ip;
    private final String hostname;
    private final String format;
    private final String conformsTo;

    public WATWarcInfoDoc(String softwareInfo, Date extractedDate, InetAddress ip, String hostname, String format, String conformsTo) {
        super(DocumentRecordTypes.WAT_WARCINFO_PAYLOAD);
        this.softwareInfo = softwareInfo;
        this.extractedDate = extractedDate;
        this.ip = ip;
        this.hostname = hostname;
        this.format = format;
        this.conformsTo = conformsTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WATWarcInfoDoc that = (WATWarcInfoDoc) o;
        return Objects.equals(softwareInfo, that.softwareInfo) &&
                Objects.equals(extractedDate, that.extractedDate) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(hostname, that.hostname) &&
                Objects.equals(format, that.format) &&
                Objects.equals(conformsTo, that.conformsTo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(softwareInfo, extractedDate, ip, hostname, format, conformsTo);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WatMetadataWarcInfoDoc{");
        sb.append("softwareInfo='").append(softwareInfo).append('\'');
        sb.append(", extractedDate=").append(extractedDate);
        sb.append(", ip=").append(ip);
        sb.append(", hostname='").append(hostname).append('\'');
        sb.append(", format='").append(format).append('\'');
        sb.append(", conformsTo='").append(conformsTo).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getSoftwareInfo() {
        return softwareInfo;
    }

    public Date getExtractedDate() {
        return extractedDate;
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getHostname() {
        return hostname;
    }

    public String getFormat() {
        return format;
    }

    public String getConformsTo() {
        return conformsTo;
    }

    public static WATWarcInfoDoc getWATWarcInfoDoc(byte[] byteArr, int startIndex, int endIndex) throws Exception {
        int currIndex = startIndex;
        String softwareInfo = null;
        Date extractedDate = null;
        InetAddress ip = null;
        String hostname = null;
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

                case "ip": {
                    try {
                        ip = InetAddress.getByName(value);
                    } catch (UnknownHostException uhe) {
                        logger.error("unable to parse ip from warcinfo record - ip: {}", value);
                        throw new RuntimeException("unable to parse ip from warcinfo record");
                    }
                    break;
                }
                case "hostname": {
                    hostname = value;
                    break;
                }
                case "format": {
                    format = value;
                    break;
                }
                case "conformsTo": {
                    conformsTo = value;
                    break;
                }
                default:{
                    logger.error("unknown warcinfo document key in WAT doc - key: {}, value: {}", key, value);
                    throw new RuntimeException("unknown warcinfo document key");
                }
            }
        } while (currIndex < endIndex);
        WATWarcInfoDoc warcInfoDoc = new WATWarcInfoDoc(softwareInfo, extractedDate, ip, hostname, format, conformsTo);
        return warcInfoDoc;
    }
}