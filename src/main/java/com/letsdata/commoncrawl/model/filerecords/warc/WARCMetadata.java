package com.letsdata.commoncrawl.model.filerecords.warc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.resonance.letsdata.data.util.StringFunctions;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class WARCMetadata extends AbstractWARCRecord {

    private static final Logger logger = LoggerFactory.getLogger(WARCMetadata.class);

    private final String refersTo;
    private final String concurrentTo;
    private final String infoId;
    private final DocumentRecordTypes documentRecordType;
    /*
    "WARC-Header-Metadata": {
        "Content-Length": "203",
        "Content-Type": "application/warc-fields",
        "WARC-Concurrent-To": "<urn:uuid:34b4755b-3ca3-464a-8ad2-988e2e872efa>",
        "WARC-Date": "2020-07-02T05:29:36Z",
        "WARC-Record-ID": "<urn:uuid:9f1abc20-3b8c-4780-801a-daf99b6ba613>",
        "WARC-Target-URI": "http://000.266sbc.com/zhongkaozw/zhongkaosc/",
        "WARC-Type": "metadata",
        "WARC-Warcinfo-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>"
    }
    * */
    @JsonCreator
    public WARCMetadata(@JsonProperty("WARC-Date") String date, @JsonProperty("Content-Type") String contentType, @JsonProperty("Content-Length") String contentLength, @JsonProperty("WARC-Target-URI") String targetUri, @JsonProperty("WARC-Record-ID") String recordId, @JsonProperty("WARC-Concurrent-To") String concurrentTo, @JsonProperty("WARC-Type") String warcType, @JsonProperty("WARC-Warcinfo-ID") String infoId) throws Exception {
        super(CommonCrawlFileType.WAT, "WARC", 1.0, WARCRecordTypes.fromTextValue(warcType), getDateFromString(date), recordId, targetUri, getContentTypeFromString(contentType), Integer.parseInt(contentLength), null);
        this.refersTo = null;
        this.concurrentTo = concurrentTo;
        this.documentRecordType = null;
        this.infoId = infoId;
        ValidationUtils.validateAssertCondition(this.recordType == WARCRecordTypes.METADATA, "recordType should be METADATA", recordType);
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

    private static ContentType getContentTypeFromString(String contentType) {
        if (contentType.contains(";")) {
            String[] contentTypeComponents = contentType.split(";");
            ValidationUtils.validateAssertCondition(contentTypeComponents.length == 2, "contentTypeComponents length should equal 2", contentType);
            StringFunctions.validateStringsAreEqual(contentTypeComponents[1].trim(), "msgtype=request", "contentTypeComponent string should be msgtype=metadata");
            return ContentType.fromString(contentTypeComponents[0]);
        } else {
            return ContentType.fromString(contentType);
        }
    }

    public WARCMetadata(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, Date date, ContentType contentType, int contentLength, WarcDoc warcMetadataDoc, String targetUri, String recordId, String refersTo, String concurrentTo, DocumentRecordTypes documentRecordType, String infoId) {
        super(commonCrawlFileType, protocol, version, recordType, date, recordId, targetUri, contentType, contentLength, warcMetadataDoc);
        ValidationUtils.validateAssertCondition(
                (documentRecordType == DocumentRecordTypes.WARC_METADATA_PAYLOAD && warcDoc instanceof WarcMetadataDoc) ||
                        ((Arrays.asList(DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD, DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD, DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD, DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD).contains(documentRecordType)) && warcDoc instanceof WatMetadataDoc), "warcDocumentType should be as expected", warcMetadataDoc);
        this.refersTo = refersTo;
        this.concurrentTo = concurrentTo;
        this.documentRecordType = documentRecordType;
        this.infoId = infoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WARCMetadata that = (WARCMetadata) o;
        return Objects.equals(recordId, that.recordId) &&
                Objects.equals(refersTo, that.refersTo) &&
                Objects.equals(infoId, that.infoId) &&
                documentRecordType == that.documentRecordType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), recordId, refersTo, documentRecordType, infoId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCMetadata{");
        sb.append(", recordId='").append(recordId).append('\'');
        sb.append(", refersTo='").append(refersTo).append('\'');
        sb.append(", documentRecordType=").append(documentRecordType);
        sb.append(", commonCrawlFileType=").append(commonCrawlFileType);
        sb.append(", protocol='").append(protocol).append('\'');
        sb.append(", version=").append(version);
        sb.append(", recordType=").append(recordType);
        sb.append(", date=").append(date);
        sb.append(", contentType=").append(contentType);
        sb.append(", contentLength=").append(contentLength);
        sb.append(", warcDoc=").append(warcDoc);
        sb.append(", infoId=").append(infoId);
        sb.append(", targetUri=").append(targetUri);
        sb.append('}');
        return sb.toString();
    }

    public String getRecordId() {
        return recordId;
    }

    public String getRefersTo() {
        return refersTo;
    }

    public DocumentRecordTypes getDocumentRecordType() {
        return documentRecordType;
    }

    public String getInfoId() {
        return infoId;
    }

    public String getConcurrentTo() {
        return concurrentTo;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
