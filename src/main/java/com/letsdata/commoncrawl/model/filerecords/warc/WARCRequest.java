package com.letsdata.commoncrawl.model.filerecords.warc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcHttpRequestDoc;
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

public class WARCRequest extends AbstractWARCRecord {
    private static final Logger logger = LoggerFactory.getLogger(WARCRequest.class);
    /*
        WARC/1.0
        WARC-Type: request
        WARC-Date: 2020-07-02T05:29:36Z
        WARC-Record-ID: <urn:uuid:776fac7c-b1ee-40ba-8444-2f0da1c33400>
        Content-Length: 272
        Content-Type: application/http; msgtype=request
        WARC-Warcinfo-ID: <urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>
        WARC-IP-Address: 107.160.155.30
        WARC-Target-URI: http://000.266sbc.com/zhongkaozw/zhongkaosc/

        GET /zhongkaozw/zhongkaosc/ HTTP/1.1
        User-Agent: CCBot/2.0 (https://commoncrawl.org/faq/)
        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*\/*;q=0.8
        Accept-Language: en-US,en;q=0.5
        Accept-Encoding: br,gzip
        Host: 000.266sbc.com
        Connection: Keep-Alive
    */
    private final String warcInfoId;
    private final InetAddress warcIP;

    /*
    "WARC-Header-Metadata": {
        "Content-Length": "272",
        "Content-Type": "application/http; msgtype=request",
        "WARC-Date": "2020-07-02T05:29:36Z",
        "WARC-IP-Address": "107.160.155.30",
        "WARC-Record-ID": "<urn:uuid:776fac7c-b1ee-40ba-8444-2f0da1c33400>",
        "WARC-Target-URI": "http://000.266sbc.com/zhongkaozw/zhongkaosc/",
        "WARC-Type": "request",
        "WARC-Warcinfo-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>"
    }
    * */
    @JsonCreator
    public WARCRequest(@JsonProperty("WARC-Type") String recordType, @JsonProperty("WARC-Date") String date, @JsonProperty("Content-Type") String contentType, @JsonProperty("Content-Length") String contentLength, @JsonProperty("WARC-Record-ID") String recordId, @JsonProperty("WARC-Warcinfo-ID") String warcInfoId, @JsonProperty("WARC-IP-Address") String warcIP, @JsonProperty("WARC-Target-URI") String warcTargetUri) throws Exception {
        super(CommonCrawlFileType.WAT, "WARC", 1.0, WARCRecordTypes.fromTextValue(recordType), getDateFromString(date), recordId, warcTargetUri, getContentTypeFromString(contentType), Integer.parseInt(contentLength), null);
        this.warcInfoId = warcInfoId;
        this.warcIP = InetAddress.getByName(warcIP);
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
            StringFunctions.validateStringsAreEqual(contentTypeComponents[1].trim(), "msgtype=request", "contentTypeComponent string should be msgtype=request");
            return ContentType.fromString(contentTypeComponents[0]);
        } else {
            return ContentType.fromString(contentType);
        }
    }

    public WARCRequest(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, Date date, ContentType contentType, int contentLength, WarcHttpRequestDoc httpRequestDoc, String recordId, String warcInfoId, InetAddress warcIP, String warcTargetUri) {
        super(commonCrawlFileType, protocol, version, recordType, date, recordId, warcTargetUri, contentType, contentLength, httpRequestDoc);
        this.warcInfoId = warcInfoId;
        this.warcIP = warcIP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WARCRequest that = (WARCRequest) o;
        return Objects.equals(recordId, that.recordId) &&
                Objects.equals(warcInfoId, that.warcInfoId) &&
                Objects.equals(warcIP, that.warcIP);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), recordId, warcInfoId, warcIP);
    }

    public String getRecordId() {
        return recordId;
    }

    public String getWarcInfoId() {
        return warcInfoId;
    }

    public InetAddress getWarcIP() {
        return warcIP;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCRequest{");
        sb.append("recordId='").append(recordId).append('\'');
        sb.append(", warcInfoId='").append(warcInfoId).append('\'');
        sb.append(", warcIP=").append(warcIP);
        sb.append(", targetUri=").append(targetUri);
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

    @Override
    public Logger getLogger() {
        return logger;
    }
}
