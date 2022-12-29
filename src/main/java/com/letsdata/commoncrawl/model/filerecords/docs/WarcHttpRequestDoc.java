package com.letsdata.commoncrawl.model.filerecords.docs;

import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.http.HttpHeaderNames;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.StringFunctions;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WarcHttpRequestDoc extends WarcDoc {
    private static final Logger logger = LoggerFactory.getLogger(WarcHttpRequestDoc.class);
    String httpMethod;
    String httpResource;
    String protocol;
    double version;
    String userAgent;
    String acceptHeader;
    String acceptLanguage;
    String acceptEncoding;
    String ifModifiedSince;
    String host;
    String connection;

    public WarcHttpRequestDoc(String httpMethod, String httpResource, String protocol, double version, String userAgent, String acceptHeader, String acceptLanguage, String acceptEncoding, String host, String connection, String ifModifiedSince) {
        super(DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD);
        this.httpMethod = httpMethod;
        this.httpResource = httpResource;
        this.protocol = protocol;
        this.version = version;
        this.userAgent = userAgent;
        this.acceptHeader = acceptHeader;
        this.acceptLanguage = acceptLanguage;
        this.acceptEncoding = acceptEncoding;
        this.ifModifiedSince = ifModifiedSince;
        this.host = host;
        this.connection = connection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcHttpRequestDoc that = (WarcHttpRequestDoc) o;
        return Objects.equals(httpMethod, that.httpMethod) &&
                Objects.equals(httpResource, that.httpResource) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(version, that.version) &&
                Objects.equals(userAgent, that.userAgent) &&
                Objects.equals(acceptHeader, that.acceptHeader) &&
                Objects.equals(acceptLanguage, that.acceptLanguage) &&
                Objects.equals(acceptEncoding, that.acceptEncoding) &&
                Objects.equals(ifModifiedSince, that.ifModifiedSince) &&
                Objects.equals(host, that.host) &&
                Objects.equals(connection, that.connection);
    }

    @Override
    public int hashCode() {

        return Objects.hash(httpMethod, httpResource, protocol, version, userAgent, acceptHeader, acceptLanguage, acceptEncoding, ifModifiedSince, host, connection);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcHttpRequestDoc{");
        sb.append("httpMethod=").append(httpMethod);
        sb.append(", httpResource='").append(httpResource).append('\'');
        sb.append(", protocol='").append(protocol).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append(", acceptHeader='").append(acceptHeader).append('\'');
        sb.append(", acceptLanguage='").append(acceptLanguage).append('\'');
        sb.append(", acceptEncoding='").append(acceptEncoding).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", connection='").append(connection).append('\'');
        sb.append(", ifModifiedSince='").append(ifModifiedSince).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpResource() {
        return httpResource;
    }

    public void setHttpResource(String httpResource) {
        this.httpResource = httpResource;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public static WarcHttpRequestDoc getWarcHttpRequestDocFromString(byte[] byteArr, int startIndex, int endIndex) throws Exception {
        int currIndex = startIndex;

        // GET /media_news/detail.php?id=7373476 HTTP/1.1
        String httpMethod = null;
        String path = null;
        String protocol = null;
        double version = -1;

        Map<HttpHeaderNames, String> headerNameValueMap = new HashMap<>();
        do {
            Matcher.KeyValueResult keyValueResult = Matcher.getKeyValueFromLine(byteArr, currIndex, endIndex, WARCFileReaderConstants.WARC_HEADER_LINE_END_PATTERN, WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN);
            String key = keyValueResult.getKey();
            String value = keyValueResult.getValue();

            if (key == null && value == null
                    || key.startsWith("GET ") // when the GET line has the separator - GET /resource/query?id=11:40:43:46:2: HTTP/1.1
                    ) {
                if (keyValueResult.getNextIndex() < endIndex) {
                    String httpRequestLine = new String(byteArr, currIndex, keyValueResult.getNextIndex()-currIndex, "utf-8").trim();
                    String[] httpRequestComponents = httpRequestLine.split(" ");
                    ValidationUtils.validateAssertCondition(httpRequestComponents.length == 3, "httpRequestLine components should be equal to 3", Arrays.toString(httpRequestComponents));
                    httpMethod = httpRequestComponents[0];
                    path = httpRequestComponents[1];

                    int separatorIndex = httpRequestComponents[2].indexOf("/");
                    if (separatorIndex < 0) {
                        throw new RuntimeException("separator not found in the httpRequestLine");
                    }
                    protocol = httpRequestComponents[2].substring(0, separatorIndex);
                    version = Double.parseDouble(httpRequestComponents[2].substring(separatorIndex + 1, httpRequestComponents[2].length()).trim());
                } else {
                    break;
                }
            } else {
                HttpHeaderNames headerKey = HttpHeaderNames.getHttpHeaderNameFromString(key);
                if (headerKey == null) {
                    logger.error("header key not found in header names in getWarcHttpRequestDocFromString - key: {}, string: {}", key, value);
                    throw new RuntimeException("header key not found in header names in getWarcHttpRequestDocFromString - key: "+key);
                }
                switch (headerKey) {
                    case ACCEPT:
                    case ACCEPT_ENCODING:
                    case ACCEPT_LANGUAGE:
                    case HOST:
                    case CONNECTION:
                    case IF_MODIFIED_SINCE:
                    case USER_AGENT: {
                        if (headerNameValueMap.containsKey(headerKey)) {
                            logger.error("duplicate Http header in the HttpRequest in getWarcHttpRequestDocFromString - key: {}, value: {}", headerKey, value);
                            throw new RuntimeException("duplicate Http header in the HttpRequest");
                        }
                        headerNameValueMap.put(headerKey, value);
                        break;
                    }
                    default: {
                        logger.error("unknown request header key in getWarcHttpRequestDocFromString - key: {}, string: {}", headerKey, value);
                        throw new RuntimeException("unknown request header key in getWarcHttpRequestDocFromString");
                    }
                }
            }
            currIndex = keyValueResult.getNextIndex();
        } while (currIndex < endIndex);

        // assert mandatory fields
        StringFunctions.validateStringIsNotBlank(httpMethod, "httpMethod string should not be blank");
        StringFunctions.validateStringIsNotBlank(path, "path string should not be blank");
        ValidationUtils.validateAssertCondition(protocol != null && StringUtils.equalsIgnoreCase(protocol, "HTTP"), "protocol should be HTTP");
        ValidationUtils.validateAssertCondition(version > 0, "version should be greater than zero");
        String acceptHeader = headerNameValueMap.remove(HttpHeaderNames.ACCEPT);
        StringFunctions.validateStringIsNotBlank(acceptHeader, "acceptHeader string should not be blank");
        String acceptEncodingHeader = headerNameValueMap.remove(HttpHeaderNames.ACCEPT_ENCODING);
        StringFunctions.validateStringIsNotBlank(acceptEncodingHeader, "acceptEncodingHeader string should not be blank");
        String acceptLanguageHeader = headerNameValueMap.remove(HttpHeaderNames.ACCEPT_LANGUAGE);
        StringFunctions.validateStringIsNotBlank(acceptLanguageHeader, "acceptLanguageHeader string should not be blank");
        String hostHeader = headerNameValueMap.remove(HttpHeaderNames.HOST);
        StringFunctions.validateStringIsNotBlank(hostHeader, "hostHeader string should not be blank");
        String connectionHeader = headerNameValueMap.remove(HttpHeaderNames.CONNECTION);
        StringFunctions.validateStringIsNotBlank(connectionHeader, "connectionHeader string should not be blank");
        String userAgentHeader = headerNameValueMap.remove(HttpHeaderNames.USER_AGENT);
        StringFunctions.validateStringIsNotBlank(userAgentHeader, "userAgentHeader string should not be blank");

        String ifModifiedSince = headerNameValueMap.remove(HttpHeaderNames.IF_MODIFIED_SINCE);
        ValidationUtils.validateAssertCondition(headerNameValueMap.size() == 0, "headerNameValueMap should be empty", headerNameValueMap);
        return new WarcHttpRequestDoc(httpMethod, path, protocol, version, userAgentHeader, acceptHeader, acceptLanguageHeader, acceptEncodingHeader, ifModifiedSince, hostHeader, connectionHeader);
    }
}
