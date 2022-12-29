package com.letsdata.commoncrawl.model.filerecords.docs;

import com.letsdata.commoncrawl.model.CrawlDataRecordErrorException;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.http.HttpHeaderNames;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.util.DateUtils;
import com.resonance.letsdata.data.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;

public class WarcHttpResponseDoc extends WarcDoc {

    private static final Logger logger = LoggerFactory.getLogger(WarcHttpResponseDoc.class);

    private final String protocol;
    private final double version;
    private final double statusCode;
    private final String status;
    private final String contentType;
    private final Long contentLength;
    private final Date date;
    private final String server;
    private final String connection;
    private final String setCookie;
    private final String eTag;
    private final String responseDocument;

    private String keepAlive;
    private String vary;
    private Date lastModified;
    private String acceptRanges;
    private String cacheControl;
    private String pragma;
    private String expires;
    private String xPoweredBy;
    private String xCrawlerContentEncoding;
    private long xCrawlerContentLength;
    private String xFrameOptions;
    private String xXSSProtection;
    private String cfCacheStatus;
    private String cfRequestId;
    private String cfRay;
    private String accessControlAllowOrigin;
    private String accessControlAllowMethods;
    private String accessControlAllowHeaders;
    private Map<HttpHeaderNames, String> responseHeaderMap;

    public WarcHttpResponseDoc(String protocol, double version, double statusCode, String status, String contentType, Long contentLength, Date date, String server, String connection, String setCookie, String eTag, String responseDocument) {
        super(DocumentRecordTypes.WARC_HTTP_RESPONSE_PAYLOAD);
        this.protocol = protocol;
        this.version = version;
        this.statusCode = statusCode;
        this.status = status;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.date = date;
        this.server = server;
        this.connection = connection;
        this.setCookie = setCookie;
        this.eTag = eTag;
        this.responseDocument = responseDocument;
    }

    public String getProtocol() {
        return protocol;
    }

    public double getVersion() {
        return version;
    }

    public double getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public Date getDate() {
        return date;
    }

    public String getServer() {
        return server;
    }

    public String getConnection() {
        return connection;
    }

    public String getSetCookie() {
        return setCookie;
    }

    public String geteTag() {
        return eTag;
    }

    public Map<HttpHeaderNames, String> getResponseHeaderMap() {
        return responseHeaderMap;
    }

    public String getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(String keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getVary() {
        return vary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcHttpResponseDoc that = (WarcHttpResponseDoc) o;
        return Double.compare(that.version, version) == 0 &&
                Double.compare(that.statusCode, statusCode) == 0 &&
                xCrawlerContentLength == that.xCrawlerContentLength &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(status, that.status) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(contentLength, that.contentLength) &&
                Objects.equals(date, that.date) &&
                Objects.equals(server, that.server) &&
                Objects.equals(connection, that.connection) &&
                Objects.equals(setCookie, that.setCookie) &&
                Objects.equals(eTag, that.eTag) &&
                Objects.equals(responseDocument, that.responseDocument) &&
                Objects.equals(responseHeaderMap, that.responseHeaderMap) &&
                Objects.equals(keepAlive, that.keepAlive) &&
                Objects.equals(vary, that.vary) &&
                Objects.equals(lastModified, that.lastModified) &&
                Objects.equals(acceptRanges, that.acceptRanges) &&
                Objects.equals(cacheControl, that.cacheControl) &&
                Objects.equals(pragma, that.pragma) &&
                Objects.equals(expires, that.expires) &&
                Objects.equals(xPoweredBy, that.xPoweredBy) &&
                Objects.equals(xCrawlerContentEncoding, that.xCrawlerContentEncoding) &&
                Objects.equals(xFrameOptions, that.xFrameOptions) &&
                Objects.equals(xXSSProtection, that.xXSSProtection) &&
                Objects.equals(cfCacheStatus, that.cfCacheStatus) &&
                Objects.equals(cfRequestId, that.cfRequestId) &&
                Objects.equals(cfRay, that.cfRay) &&
                Objects.equals(accessControlAllowOrigin, that.accessControlAllowOrigin) &&
                Objects.equals(accessControlAllowMethods, that.accessControlAllowMethods) &&
                Objects.equals(accessControlAllowHeaders, that.accessControlAllowHeaders);
    }

    @Override
    public int hashCode() {

        return Objects.hash(protocol, version, statusCode, status, contentType, contentLength, date, server, connection, setCookie, eTag, responseHeaderMap, keepAlive, vary, lastModified, acceptRanges, cacheControl, pragma, expires, xPoweredBy, xCrawlerContentEncoding, xCrawlerContentLength, xFrameOptions, xXSSProtection, cfCacheStatus, cfRequestId, cfRay, accessControlAllowOrigin, accessControlAllowMethods, accessControlAllowHeaders, responseDocument);
    }

    public void setVary(String vary) {
        this.vary = vary;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(String acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getPragma() {
        return pragma;
    }

    public void setPragma(String pragma) {
        this.pragma = pragma;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getxPoweredBy() {
        return xPoweredBy;
    }

    public void setxPoweredBy(String xPoweredBy) {
        this.xPoweredBy = xPoweredBy;
    }

    public String getxCrawlerContentEncoding() {
        return xCrawlerContentEncoding;
    }

    public void setxCrawlerContentEncoding(String xCrawlerContentEncoding) {
        this.xCrawlerContentEncoding = xCrawlerContentEncoding;
    }

    public long getxCrawlerContentLength() {
        return xCrawlerContentLength;
    }

    public void setxCrawlerContentLength(long xCrawlerContentLength) {
        this.xCrawlerContentLength = xCrawlerContentLength;
    }

    public String getxFrameOptions() {
        return xFrameOptions;
    }

    public void setxFrameOptions(String xFrameOptions) {
        this.xFrameOptions = xFrameOptions;
    }

    public String getxXSSProtection() {
        return xXSSProtection;
    }

    public void setxXSSProtection(String xXSSProtection) {
        this.xXSSProtection = xXSSProtection;
    }

    public String getCfCacheStatus() {
        return cfCacheStatus;
    }

    public void setCfCacheStatus(String cfCacheStatus) {
        this.cfCacheStatus = cfCacheStatus;
    }

    public String getCfRequestId() {
        return cfRequestId;
    }

    public void setCfRequestId(String cfRequestId) {
        this.cfRequestId = cfRequestId;
    }

    public String getCfRay() {
        return cfRay;
    }

    public void setCfRay(String cfRay) {
        this.cfRay = cfRay;
    }

    public String getAccessControlAllowOrigin() {
        return accessControlAllowOrigin;
    }

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public String getAccessControlAllowMethods() {
        return accessControlAllowMethods;
    }

    public void setAccessControlAllowMethods(String accessControlAllowMethods) {
        this.accessControlAllowMethods = accessControlAllowMethods;
    }

    public String getAccessControlAllowHeaders() {
        return accessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(String accessControlAllowHeaders) {
        this.accessControlAllowHeaders = accessControlAllowHeaders;
    }

    public String getResponseDocument() {
        return responseDocument;
    }

    public void setResponseHeaderMap(Map<HttpHeaderNames, String> responseHeaderMap) {
        this.responseHeaderMap = responseHeaderMap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcHttpResponseDoc{");
        sb.append("protocol='").append(protocol).append('\'');
        sb.append(", version=").append(version);
        sb.append(", statusCode=").append(statusCode);
        sb.append(", status='").append(status).append('\'');
        sb.append(", contentType='").append(contentType).append('\'');
        sb.append(", contentLength=").append(contentLength);
        sb.append(", date=").append(date);
        sb.append(", server='").append(server).append('\'');
        sb.append(", connection='").append(connection).append('\'');
        sb.append(", setCookie='").append(setCookie).append('\'');
        sb.append(", eTag='").append(eTag).append('\'');
        sb.append(", responseHeaderMap=").append(responseHeaderMap);
        sb.append(", keepAlive='").append(keepAlive).append('\'');
        sb.append(", vary='").append(vary).append('\'');
        sb.append(", lastModified=").append(lastModified);
        sb.append(", acceptRanges='").append(acceptRanges).append('\'');
        sb.append(", cacheControl='").append(cacheControl).append('\'');
        sb.append(", pragma='").append(pragma).append('\'');
        sb.append(", expires='").append(expires).append('\'');
        sb.append(", xPoweredBy='").append(xPoweredBy).append('\'');
        sb.append(", xCrawlerContentEncoding='").append(xCrawlerContentEncoding).append('\'');
        sb.append(", xCrawlerContentLength=").append(xCrawlerContentLength);
        sb.append(", xFrameOptions='").append(xFrameOptions).append('\'');
        sb.append(", xXSSProtection='").append(xXSSProtection).append('\'');
        sb.append(", cfCacheStatus='").append(cfCacheStatus).append('\'');
        sb.append(", cfRequestId='").append(cfRequestId).append('\'');
        sb.append(", cfRay='").append(cfRay).append('\'');
        sb.append(", accessControlAllowOrigin='").append(accessControlAllowOrigin).append('\'');
        sb.append(", accessControlAllowMethods='").append(accessControlAllowMethods).append('\'');
        sb.append(", accessControlAllowHeaders='").append(accessControlAllowHeaders).append('\'');
        sb.append(", responseDocument='").append(responseDocument).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static WarcHttpResponseDoc getWarcHttpResponseDocFromString(byte[] byteArr, int startIndex, int endIndex) throws Exception{
        int currIndex = startIndex;
        // HTTP/1.1 200 OK
        String status = null;
        double statusCode = -1;
        String protocol = null;
        double version = -1;

        int headerEndIndex = Matcher.match(byteArr, startIndex, endIndex, WARCFileReaderConstants.WARC_RECORD_HEADER_END_PATTERN);
        if (headerEndIndex == -1) {
            logger.error("header end index not found in Http Response - startIndex: "+startIndex+", endIndex: "+endIndex);
            throw new RuntimeException("header end index not found in Http Response - startIndex: "+startIndex+", endIndex: "+endIndex);
        }

        Map<HttpHeaderNames, String> headerNameValueMap = new HashMap<>();
        do {
            Matcher.KeyValueResult keyValueResult = Matcher.getKeyValueFromLine(byteArr, currIndex, headerEndIndex, WARCFileReaderConstants.WARC_HEADER_LINE_END_PATTERN, WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN);
            String key = keyValueResult.getKey();
            String value = keyValueResult.getValue();
            if (key == null && value == null) {
                if (protocol == null && keyValueResult.getNextIndex() < headerEndIndex) {
                    String httpResponseLine = new String(byteArr, currIndex, keyValueResult.getNextIndex()-currIndex, "utf-8").trim();
                    String[] httpResponseComponents = httpResponseLine.split(" ");
                    ValidationUtils.validateAssertCondition(httpResponseComponents.length >= 2, "httpResponseLine components should be less than or equal to 2", Arrays.toString(httpResponseComponents));
                    statusCode = Double.parseDouble(httpResponseComponents[1].trim());
                    if (httpResponseComponents.length == 2) {
                        status = Double.toString(statusCode);
                    } else {
                        status = httpResponseComponents[2].trim();
                        if (StringUtils.isBlank(status)) {
                            status = Double.toString(statusCode);
                        }
                    }

                    int separatorIndex = httpResponseComponents[0].indexOf("/");
                    if (separatorIndex < 0) {
                        throw new RuntimeException("separator not found in the httpResponseLine");
                    }
                    protocol = httpResponseComponents[0].substring(0, separatorIndex).trim();
                    version = Double.parseDouble(httpResponseComponents[0].substring(separatorIndex+1, httpResponseComponents[0].length()).trim());
                } else {
                    break;
                }
            }  else {
                HttpHeaderNames headerKey = HttpHeaderNames.getHttpHeaderNameFromString(key);
                if (headerKey == null) {
                    logger.debug("header key not found in header key names in getWarcHttpResponseDocFromString - skipping - key: {}, value: {}", key, value);
                } else {
                    switch (headerKey) {
                        default: {
                            if (headerNameValueMap.containsKey(headerKey)) {
                                logger.debug("duplicate Http header in the HttpResponse in getWarcHttpResponseDocFromString - key: {}, value: {}", headerKey, value);
                                break;
                                // throw new RuntimeException("duplicate Http header in the HttpResponse");
                            }
                            headerNameValueMap.put(headerKey, value);
                            break;
                        }
                    }
                }
            }
            currIndex = keyValueResult.getNextIndex();
        } while (currIndex < headerEndIndex);

        // assert mandatory fields
        try {
            try {
                StringFunctions.validateStringIsNotBlank(status, "status string should not be blank - status: " + status);
            } catch (Exception ex) {
                Matcher.KeyValueResult keyValueResult = Matcher.getKeyValueFromLine(byteArr, currIndex, headerEndIndex, WARCFileReaderConstants.WARC_HEADER_LINE_END_PATTERN, WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN);
                logger.error("status is blank - line: "+new String(byteArr, startIndex, keyValueResult.getNextIndex() - startIndex, "utf-8").trim(), ex);
                throw new CrawlDataRecordErrorException("status is blank", ex);
            }
            ValidationUtils.validateAssertCondition(statusCode > 0, "statusCode should not be greater than zero ", statusCode);
            ValidationUtils.validateAssertCondition(protocol != null && StringUtils.equalsIgnoreCase(protocol, "HTTP"), "protocol should be HTTP");
            ValidationUtils.validateAssertCondition(version > 0, "version should be greater than zero");
            String contentType = headerNameValueMap.remove(HttpHeaderNames.CONTENT_TYPE);
            String contentLengthStr = headerNameValueMap.remove(HttpHeaderNames.CONTENT_LENGTH);
            Long contentLength = contentLengthStr == null ? null : Long.parseLong(contentLengthStr);
            String dateStr = headerNameValueMap.remove(HttpHeaderNames.DATE);
            Date date = StringUtils.isBlank(dateStr) ? null : DateUtils.getDateFromDateString(dateStr);
            String server = headerNameValueMap.remove(HttpHeaderNames.SERVER);
            String connection = headerNameValueMap.remove(HttpHeaderNames.CONNECTION);
            String setCookie = headerNameValueMap.remove(HttpHeaderNames.SET_COOKIE);
            String eTag = headerNameValueMap.remove(HttpHeaderNames.ETAG);

            int documentStartIndex = Matcher.consumeWhitespace(byteArr, headerEndIndex, endIndex);

            Charset charset = Charset.forName("utf-8");
            if (!StringUtils.isBlank(contentType)) {
                int charsetIndex = contentType.toLowerCase().indexOf("charset=");
                if (charsetIndex != -1) {
                    int charsetEndIndex = contentType.indexOf(" |;|\n|\t");
                    String charsetStr;
                    if (charsetEndIndex != -1) {
                        charsetStr = contentType.substring(charsetIndex + "charset=".length(), charsetEndIndex).trim();
                    } else {
                        charsetStr = contentType.substring(charsetIndex + "charset=".length(), contentType.length()).trim();
                    }
                    try {
                        charset = Charset.forName(charsetStr);
                    } catch (Exception ex) {
                        logger.info("charset not found for name {}", charsetStr);
                    }
                }
            }

            String document = new String(byteArr, documentStartIndex, endIndex - documentStartIndex, charset);
            WarcHttpResponseDoc httpResponseDoc = new WarcHttpResponseDoc(protocol, version, statusCode, status, contentType, contentLength, date, server, connection, setCookie, eTag, document);
            setHttpHeadersFromMap(headerNameValueMap, httpResponseDoc);
            return httpResponseDoc;
        } catch (Exception ex) {
            logger.error("Error in getting WarcHttpResponseDoc from string - str: "+new String(byteArr, startIndex, endIndex-startIndex, "utf-8"), ex);
            throw ex;
        }
    }

    public static void setHttpHeadersFromMap(Map<HttpHeaderNames, String> headerNameValueMap, WarcHttpResponseDoc httpResponseDoc) throws Exception {
        if (headerNameValueMap.containsKey(HttpHeaderNames.ACCEPT_RANGES)) {
            httpResponseDoc.setAcceptRanges(headerNameValueMap.remove(HttpHeaderNames.ACCEPT_RANGES));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS)) {
            httpResponseDoc.setAccessControlAllowHeaders(headerNameValueMap.remove(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS)) {
            httpResponseDoc.setAccessControlAllowMethods(headerNameValueMap.remove(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN)) {
            httpResponseDoc.setAccessControlAllowOrigin(headerNameValueMap.remove(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.CACHE_CONTROL)) {
            httpResponseDoc.setCacheControl(headerNameValueMap.remove(HttpHeaderNames.CACHE_CONTROL));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.CF_CACHE_STATUS)) {
            httpResponseDoc.setCfCacheStatus(headerNameValueMap.remove(HttpHeaderNames.CF_CACHE_STATUS));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.CF_RAY)) {
            httpResponseDoc.setCfRay(headerNameValueMap.remove(HttpHeaderNames.CF_RAY));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.CF_REQUEST_ID)) {
            httpResponseDoc.setCfRequestId(headerNameValueMap.remove(HttpHeaderNames.CF_REQUEST_ID));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.EXPIRES)) {
            httpResponseDoc.setExpires(headerNameValueMap.remove(HttpHeaderNames.EXPIRES));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.KEEP_ALIVE)) {
            httpResponseDoc.setKeepAlive(headerNameValueMap.remove(HttpHeaderNames.KEEP_ALIVE));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.LAST_MODIFIED)) {
            String lastModifiedDate = headerNameValueMap.remove(HttpHeaderNames.LAST_MODIFIED);
            if (!StringUtils.isBlank(lastModifiedDate)) {
                Date date = DateUtils.getDateFromDateString(lastModifiedDate);
                if (date != null) {
                    httpResponseDoc.setLastModified(date);
                } else {
                    // logger.warn("Could not parse date for HTTP Header HttpHeaderNames - LAST_MODIFIED: "+lastModifiedDate);
                    logger.info("Could not parse date for HTTP Header HttpHeaderNames - LAST_MODIFIED: "+lastModifiedDate);
                }
            } else {
                logger.info("LAST_MODIFIED header found in the record but the value is blank - "+lastModifiedDate);
            }
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.PRAGMA)) {
            httpResponseDoc.setPragma(headerNameValueMap.remove(HttpHeaderNames.PRAGMA));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.VARY)) {
            httpResponseDoc.setVary(headerNameValueMap.remove(HttpHeaderNames.VARY));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.X_CRAWLER_CONTENT_ENCODING)) {
            httpResponseDoc.setxCrawlerContentEncoding(headerNameValueMap.remove(HttpHeaderNames.X_CRAWLER_CONTENT_ENCODING));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.X_CRAWLER_CONTENT_LENGTH)) {
            String contentLengthStr = headerNameValueMap.remove(HttpHeaderNames.X_CRAWLER_CONTENT_LENGTH);
            if (StringUtils.isBlank(contentLengthStr)) {
                // logger.warn("Could not parse X_CRAWLER_CONTENT_LENGTH for HTTP Header HttpHeaderNames - string is blank");
                logger.info("Could not parse X_CRAWLER_CONTENT_LENGTH for HTTP Header HttpHeaderNames - string is blank");
            } else {
                try {
                    contentLengthStr = contentLengthStr.trim();
                    if (contentLengthStr.endsWith(";")) {
                        contentLengthStr = contentLengthStr.substring(0, contentLengthStr.length()-1);
                    }
                    long contentLength = Long.parseLong(contentLengthStr);
                    httpResponseDoc.setxCrawlerContentLength(contentLength);
                } catch (Exception ex) {
                    // logger.warn("Could not parse X_CRAWLER_CONTENT_LENGTH for HTTP Header HttpHeaderNames - exception in parseLong", ex);
                    logger.info("Could not parse X_CRAWLER_CONTENT_LENGTH for HTTP Header HttpHeaderNames - exception in parseLong", ex);
                }
            }
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.X_FRAME_OPTIONS)) {
            httpResponseDoc.setxFrameOptions(headerNameValueMap.remove(HttpHeaderNames.X_FRAME_OPTIONS));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.X_POWERED_BY)) {
            httpResponseDoc.setxPoweredBy(headerNameValueMap.remove(HttpHeaderNames.X_POWERED_BY));
        }

        if (headerNameValueMap.containsKey(HttpHeaderNames.X_XSS_PROTECTION)) {
            httpResponseDoc.setxXSSProtection(headerNameValueMap.remove(HttpHeaderNames.X_XSS_PROTECTION));
        }

        httpResponseDoc.setResponseHeaderMap(headerNameValueMap);
    }
}