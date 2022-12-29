package com.letsdata.commoncrawl.model.filerecords.warc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcHttpResponseDoc;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.resonance.letsdata.data.util.StringFunctions;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class WARCResponse extends AbstractWARCRecord {
    private static final Logger logger = LoggerFactory.getLogger(WARCResponse.class);
    /*
    WARC/1.0
    WARC-Type: response
    WARC-Date: 2020-07-02T05:29:36Z
    WARC-Record-ID: <urn:uuid:34b4755b-3ca3-464a-8ad2-988e2e872efa>
    Content-Length: 53641
    Content-Type: application/http; msgtype=response
    WARC-Warcinfo-ID: <urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>
    WARC-Concurrent-To: <urn:uuid:776fac7c-b1ee-40ba-8444-2f0da1c33400>
    WARC-IP-Address: 107.160.155.30
    WARC-Target-URI: http://000.266sbc.com/zhongkaozw/zhongkaosc/
    WARC-Payload-Digest: sha1:VCIGKODWQTTYO3O54Q335ADRYU5WSGRP
    WARC-Block-Digest: sha1:C2AJMEC6D5GLWJF4MDGIV5D3XVSN2ROU
    WARC-Identified-Payload-Type: application/xhtml+xml

    HTTP/1.1 200 OK
    Date: Thu, 02 Jul 2020 04:29:36 GMT
    Content-Type: text/html; charset=GB2312
    Content-Length: 53469
    Content-Type: text/html
    Server: Microsoft-IIS/6.0
    * */
    private final String warcInfoId;
    private final String warcConcurrentTo;
    private final InetAddress warcIPAddress;
    private final String warcPayloadDigest;
    private final String warcBlockDigest;
    private final String warcIdentifiedPayloadType;
    private final String warcTruncated;

    /*
        "WARC-Header-Metadata": {
            "Content-Length": "5149",
            "Content-Type": "application/http; msgtype=response",
            "WARC-Block-Digest": "sha1:VSQOTNLEMFURZUXHVVRYNYHGDTPZG6PW",
            "WARC-Concurrent-To": "<urn:uuid:0d4689dd-62bc-4a12-95b8-7c79859f389e>",
            "WARC-Date": "2020-07-02T05:22:10Z",
            "WARC-IP-Address": "210.140.45.105",
            "WARC-Identified-Payload-Type": "text/html",
            "WARC-Payload-Digest": "sha1:ZA4S2ROKJQBBZUB7HNCIF4B343CJJLNW",
            "WARC-Record-ID": "<urn:uuid:355cc15b-45ff-4350-a325-4b7e857abccd>",
            "WARC-Target-URI": "http://01.rknt.jp/rank.php?page=0&id=m5130&genre=607835",
            "WARC-Type": "response",
            "WARC-Warcinfo-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>"
        }

    * */
    @JsonCreator
    public WARCResponse(@JsonProperty("WARC-Type") String recordType, @JsonProperty("Content-Type") String contentType, @JsonProperty("WARC-Date") String date, @JsonProperty("Content-Length") String contentLength, @JsonProperty("WARC-Record-ID") String warcRecordId, @JsonProperty("WARC-Warcinfo-ID") String warcInfoId, @JsonProperty("WARC-Concurrent-To") String warcConcurrentTo, @JsonProperty("WARC-IP-Address") String warcIPAddress, @JsonProperty("WARC-Target-URI") String warcTargetUri, @JsonProperty("WARC-Payload-Digest") String warcPayloadDigest, @JsonProperty("WARC-Block-Digest") String warcBlockDigest, @JsonProperty("WARC-Identified-Payload-Type") String warcIdentifiedPayloadType, @JsonProperty("WARC-Truncated") String warcTruncated) throws Exception {
        super(CommonCrawlFileType.WAT, "WARC", 1.0, WARCRecordTypes.fromTextValue(recordType), getDateFromString(date), warcRecordId, warcTargetUri, getContentTypeFromString(contentType), Integer.parseInt(contentLength), null);
        this.warcInfoId = warcInfoId;
        this.warcConcurrentTo = warcConcurrentTo;
        this.warcIPAddress = InetAddress.getByName(warcIPAddress);
        this.warcPayloadDigest = warcPayloadDigest;
        this.warcBlockDigest = warcBlockDigest;
        this.warcIdentifiedPayloadType = warcIdentifiedPayloadType;
        this.warcTruncated = warcTruncated;
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
            StringFunctions.validateStringsAreEqual(contentTypeComponents[1].trim(), "msgtype=response", "contentTypeComponent string should be msgtype=response");
            return ContentType.fromString(contentTypeComponents[0]);
        } else {
            return ContentType.fromString(contentType);
        }
    }


    public WARCResponse(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, ContentType contentType, Date date, int contentLength, WarcHttpResponseDoc warcDoc, String warcRecordId, String warcInfoId, String warcConcurrentTo, InetAddress warcIPAddress, String warcTargetUri, String warcPayloadDigest, String warcBlockDigest, String warcIdentifiedPayloadType, String warcTruncated) {
        super(commonCrawlFileType, protocol, version, recordType, date, warcRecordId, warcTargetUri, contentType, contentLength, warcDoc);
        this.warcInfoId = warcInfoId;
        this.warcConcurrentTo = warcConcurrentTo;
        this.warcIPAddress = warcIPAddress;
        this.warcPayloadDigest = warcPayloadDigest;
        this.warcBlockDigest = warcBlockDigest;
        this.warcIdentifiedPayloadType = warcIdentifiedPayloadType;
        this.warcTruncated = warcTruncated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WARCResponse that = (WARCResponse) o;
        return Objects.equals(recordId, that.recordId) &&
                Objects.equals(warcInfoId, that.warcInfoId) &&
                Objects.equals(warcConcurrentTo, that.warcConcurrentTo) &&
                Objects.equals(warcIPAddress, that.warcIPAddress) &&
                Objects.equals(warcPayloadDigest, that.warcPayloadDigest) &&
                Objects.equals(warcBlockDigest, that.warcBlockDigest) &&
                Objects.equals(warcIdentifiedPayloadType, that.warcIdentifiedPayloadType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), recordId, warcInfoId, warcConcurrentTo, warcIPAddress, warcPayloadDigest, warcBlockDigest, warcIdentifiedPayloadType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCResponse{");
        sb.append("warcRecordId='").append(recordId).append('\'');
        sb.append(", warcInfoId='").append(warcInfoId).append('\'');
        sb.append(", warcConcurrentTo='").append(warcConcurrentTo).append('\'');
        sb.append(", warcIPAddress=").append(warcIPAddress);
        sb.append(", warcTargetUri='").append(targetUri).append('\'');
        sb.append(", warcPayloadDigest='").append(warcPayloadDigest).append('\'');
        sb.append(", warcBlockDigest='").append(warcBlockDigest).append('\'');
        sb.append(", warcIdentifiedPayloadType='").append(warcIdentifiedPayloadType).append('\'');
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

    public String getRecordId() {
        return recordId;
    }

    public String getWarcInfoId() {
        return warcInfoId;
    }

    public String getWarcConcurrentTo() {
        return warcConcurrentTo;
    }

    public InetAddress getWarcIPAddress() {
        return warcIPAddress;
    }

    public String getWarcPayloadDigest() {
        return warcPayloadDigest;
    }

    public String getWarcBlockDigest() {
        return warcBlockDigest;
    }

    public String getWarcIdentifiedPayloadType() {
        return warcIdentifiedPayloadType;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
