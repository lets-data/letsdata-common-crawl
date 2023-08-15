package com.letsdata.commoncrawl.interfaces.implementations.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcErrorDoc;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.AbstractWARCRecord;
import com.resonance.letsdata.data.documents.interfaces.ErrorDocInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WARCError implements ErrorDocInterface {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(WARCError.class);

    protected final CommonCrawlFileType commonCrawlFileType;
    protected final String protocol;
    protected final double version;
    protected final WARCRecordTypes recordType;
    protected final Date date;
    protected final String recordId;
    protected final String targetUri;
    protected final ContentType contentType;
    protected final int contentLength;
    protected final WarcErrorDoc warcDoc;

    public WARCError(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, Date date, String recordId, String targetUri, ContentType contentType, int contentLength, WarcErrorDoc warcDoc) {
        this.commonCrawlFileType = commonCrawlFileType;
        this.protocol = protocol;
        this.version = version;
        this.recordType = recordType;
        this.date = date;
        this.recordId = recordId;
        this.targetUri = targetUri;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.warcDoc = warcDoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WARCError that = (WARCError) o;
        return Double.compare(that.version, version) == 0 &&
                contentLength == that.contentLength &&
                commonCrawlFileType == that.commonCrawlFileType &&
                Objects.equals(protocol, that.protocol) &&
                recordType == that.recordType &&
                Objects.equals(date, that.date) &&
                Objects.equals(recordId, that.recordId) &&
                Objects.equals(targetUri, that.targetUri) &&
                contentType == that.contentType &&
                Objects.equals(warcDoc, that.warcDoc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commonCrawlFileType, protocol, version, recordType, date, recordId, targetUri, contentType, contentLength, warcDoc);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCError{");
        sb.append("commonCrawlFileType=").append(commonCrawlFileType);
        sb.append(", protocol='").append(protocol).append('\'');
        sb.append(", version=").append(version);
        sb.append(", recordType=").append(recordType);
        sb.append(", date=").append(date);
        sb.append(", recordId=").append(recordId);
        sb.append(", targetUri=").append(targetUri);
        sb.append(", contentType=").append(contentType);
        sb.append(", contentLength=").append(contentLength);
        sb.append(", warcDoc=").append(warcDoc);
        sb.append('}');
        return sb.toString();
    }

    public Date getDate() {
        return date;
    }

    public CommonCrawlFileType getCommonCrawlFileType() {
        return commonCrawlFileType;
    }

    public String getProtocol() {
        return protocol;
    }

    public double getVersion() {
        return version;
    }

    public WARCRecordTypes getWARCRecordTypes() {
        return recordType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public WarcErrorDoc getWarcDoc() {
        return warcDoc;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getTargetUri() {
        return targetUri;
    }

    @Override
    public String getRecordType() {
        return this.getWARCRecordTypes().toString() + (warcDoc == null ? "" : ","+warcDoc.getDocumentRecordTypes().toString());
    }

    @Override
    public String getDocumentId() {
        return this.getRecordId()+this.getTargetUri();
    }

    @Override
    public Map<String, Object> getDocumentMetadata() {
        return new HashMap<>();
    }

    @JsonIgnore
    @Override
    public String serialize() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("getDocumentContents serialize exception", e);
        }
    }

    @Override
    public String getPartitionKey() {
        String partitionKey = null;
        partitionKey = getTargetUri().length() > 256 ? AbstractWARCRecord.encodeBase64(AbstractWARCRecord.computeMD5Hash(getTargetUri())) : getTargetUri();
        if (partitionKey.length() > 256) {
            logger.error("partitionKey is greater than max allowed length - url: {}, partitionKey: {}", getTargetUri(), partitionKey);
            throw new RuntimeException("partitionKey is greater than max allowed length");
        }
        return partitionKey;
    }

    @Override
    public Map<String, String> getErrorStartOffsetMap() {
        return getWarcDoc().getRecordStartOffset();
    }

    @Override
    public Map<String, String> getErrorEndOffsetMap() {
        return getWarcDoc().getRecordEndOffset();
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public boolean isSingleDoc() {
        return true;
    }
}
