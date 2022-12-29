package com.letsdata.commoncrawl.model.filerecords.warc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcDoc;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.resonance.letsdata.data.documents.interfaces.SingleDocInterface;
import org.slf4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public abstract class AbstractWARCRecord implements SingleDocInterface {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected final CommonCrawlFileType commonCrawlFileType;
    protected final String protocol;
    protected final double version;
    protected final WARCRecordTypes recordType;
    protected final Date date;
    protected final String recordId;
    protected final String targetUri;
    protected final ContentType contentType;
    protected final int contentLength;
    protected final WarcDoc warcDoc;

    public AbstractWARCRecord(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, Date date, String recordId, String targetUri, ContentType contentType, int contentLength, WarcDoc warcDoc) {
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
        AbstractWARCRecord that = (AbstractWARCRecord) o;
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
        final StringBuilder sb = new StringBuilder("AbstractWARCRecord{");
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

    public WarcDoc getWarcDoc() {
        return warcDoc;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getTargetUri() {
        return targetUri;
    }

    public abstract Logger getLogger();

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
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getPartitionKey() {
        String partitionKey = null;
        partitionKey = getTargetUri().length() > 256 ? encodeBase64(computeMD5Hash(getTargetUri())) : getTargetUri();
        if (partitionKey.length() > 256) {
            getLogger().error("partitionKey is greater than max allowed length - url: {}, partitionKey: {}", getTargetUri(), partitionKey);
            throw new RuntimeException("partitionKey is greater than max allowed length");
        }
        return partitionKey;
    }

    @Override
    public boolean isSingleDoc() {
        return true;
    }

    public static String computeMD5Hash(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeBase64(String input){
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
}
