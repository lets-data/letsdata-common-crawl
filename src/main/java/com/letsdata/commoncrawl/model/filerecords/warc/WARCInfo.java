package com.letsdata.commoncrawl.model.filerecords.warc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.warcinfo.WarcInfoDoc;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class WARCInfo extends AbstractWARCRecord {
    private static final Logger logger = LoggerFactory.getLogger(WARCInfo.class);

    private final String filename;

    /*"WARC-Header-Metadata": {
                "Content-Length": "499",
                "Content-Type": "application/warc-fields",
                "WARC-Date": "2020-07-02T04:57:58Z",
                "WARC-Filename": "CC-MAIN-20200702045758-20200702075758-00000.warc.gz",
                "WARC-Record-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>",
                "WARC-Type": "warcinfo"
    }*/
    @JsonCreator
    public WARCInfo(@JsonProperty("WARC-Type") String warcType, @JsonProperty("Content-Type") String contentType, @JsonProperty("Content-Length") String contentLength, @JsonProperty("WARC-Date") String warcDate, @JsonProperty("WARC-Filename") String filename, @JsonProperty("WARC-Record-ID") String recordId) throws Exception {
        super(CommonCrawlFileType.WAT, "WARC", 1.0, WARCRecordTypes.fromTextValue(warcType), getDateFromString(warcDate), recordId, null, ContentType.fromString(contentType), Integer.parseInt(contentLength), null);
        this.filename = filename;
    }

    private static Date getDateFromString(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WARCFileReaderConstants.WARC_DATE_FORMAT, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (Exception ex) {
            logger.warn("Error parsing date from WARC Request - dateStr: {}", dateStr);
        }
        return null;
    }

    public WARCInfo(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, Date date, ContentType contentType, int contentLength, WarcInfoDoc warcInfoDoc, String filename, String recordId) {
        super(commonCrawlFileType, protocol, version, recordType, date, recordId, null, contentType, contentLength, warcInfoDoc);
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WARCInfo warcInfo = (WARCInfo) o;
        return Objects.equals(filename, warcInfo.filename) &&
                Objects.equals(recordId, warcInfo.recordId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), filename, recordId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCInfo{");
        sb.append("filename='").append(filename).append('\'');
        sb.append(", recordId='").append(recordId).append('\'');
        sb.append(", commonCrawlFileType=").append(commonCrawlFileType);
        sb.append(", protocol='").append(protocol).append('\'');
        sb.append(", version=").append(version);
        sb.append(", recordType=").append(recordType);
        sb.append(", date=").append(date);
        sb.append(", contentType=").append(contentType);
        sb.append(", contentLength=").append(contentLength);
        sb.append(", warcDoc=").append(warcDoc);
        sb.append('}');
        return sb.toString();
    }

    public String getFilename() {
        return filename;
    }

    public String getRecordId() {
        return recordId;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
