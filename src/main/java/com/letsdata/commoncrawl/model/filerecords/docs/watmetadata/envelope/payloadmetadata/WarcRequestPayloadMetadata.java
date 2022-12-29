package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcHttpRequestDoc;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class WarcRequestPayloadMetadata extends PayloadMetadata {
    /*"Payload-Metadata": {
        "Actual-Content-Length": "272",
        "Actual-Content-Type": "application/http; msgtype=request",
        "Block-Digest": "sha1:JQ6YBRMZSSKE7EMW4C4W2FSWWE3TL2QT",
        "Trailing-Slop-Length": "4"
        "HTTP-Request-Metadata": {
            "Entity-Digest": "sha1:3I42H3S6NNFQ2MSVX7XZKYAYSCX5QBYJ",
            "Entity-Length": "0",
            "Entity-Trailing-Slop-Length": "0",
            "Headers": {
                "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*\/*;q=0.8",
                "Accept-Encoding": "br,gzip",
                "Accept-Language": "en-US,en;q=0.5",
                "Connection": "Keep-Alive",
                "Host": "000.266sbc.com",
                "User-Agent": "CCBot/2.0 (https://commoncrawl.org/faq/)"
            },
            "Headers-Length": "270",
            "Request-Message": {
                "Method": "GET",
                "Path": "/zhongkaozw/zhongkaosc/",
                "Version": "HTTP/1.1"
            }
        },
    }*/
    private final String entityDigest;
    private final Long entityLength;
    private final Long entityTrailingSlopLength;
    private final Long headersLength;
    private final WarcHttpRequestDoc httpRequestDoc;

    @JsonCreator
    public WarcRequestPayloadMetadata(@JsonProperty("Actual-Content-Length") String actualContentLength, @JsonProperty("Actual-Content-Type") String actualContentType, @JsonProperty("Block-Digest") String blockDigest, @JsonProperty("Trailing-Slop-Length") String trailingSlopLength, @JsonProperty("HTTP-Request-Metadata") PayloadMetadataHttpRequestMetadata requestMetadata) {
        super(StringUtils.isBlank(actualContentLength) ? null : Long.parseLong(actualContentLength), ContentType.fromString(actualContentType), blockDigest, StringUtils.isBlank(trailingSlopLength) ? null : Long.parseLong(trailingSlopLength));
        this.entityDigest = requestMetadata.entityDigest;
        this.entityLength = requestMetadata.entityLength;
        this.entityTrailingSlopLength = requestMetadata.entityTrailingSlopLength;
        this.headersLength = requestMetadata.headersLength;
        this.httpRequestDoc = new WarcHttpRequestDoc(requestMetadata.requestMessage.method, requestMetadata.requestMessage.path, requestMetadata.requestMessage.protocol, requestMetadata.requestMessage.version, requestMetadata.requestHeaders.userAgent, requestMetadata.requestHeaders.accept, requestMetadata.requestHeaders.acceptLanguage, requestMetadata.requestHeaders.acceptEncoding, requestMetadata.requestHeaders.ifModifiedSince, requestMetadata.requestHeaders.host, requestMetadata.requestHeaders.connection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WarcRequestPayloadMetadata that = (WarcRequestPayloadMetadata) o;
        return entityLength == that.entityLength &&
                entityTrailingSlopLength == that.entityTrailingSlopLength &&
                headersLength == that.headersLength &&
                Objects.equals(entityDigest, that.entityDigest) &&
                Objects.equals(httpRequestDoc, that.httpRequestDoc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), entityDigest, entityLength, entityTrailingSlopLength, headersLength, httpRequestDoc);
    }

    public String getEntityDigest() {
        return entityDigest;
    }

    public Long getEntityLength() {
        return entityLength;
    }

    public Long getEntityTrailingSlopLength() {
        return entityTrailingSlopLength;
    }

    public Long getHeadersLength() {
        return headersLength;
    }

    public WarcHttpRequestDoc getHttpRequestDoc() {
        return httpRequestDoc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcRequestPayloadMetadata{");
        sb.append("actualContentLength=").append(actualContentLength);
        sb.append(", actualContentType=").append(actualContentType);
        sb.append(", blockDigest='").append(blockDigest).append('\'');
        sb.append(", trailingSlopLength=").append(trailingSlopLength);
        sb.append(", entityDigest='").append(entityDigest).append('\'');
        sb.append(", entityLength=").append(entityLength);
        sb.append(", entityTrailingSlopLength=").append(entityTrailingSlopLength);
        sb.append(", headersLength=").append(headersLength);
        sb.append(", httpRequestDoc=").append(httpRequestDoc);
        sb.append('}');
        return sb.toString();
    }

    public static class PayloadMetadataHttpRequestMetadata {
        private final String entityDigest;
        private final Long entityLength;
        private final Long entityTrailingSlopLength;
        private final PayloadMetadataHttpRequestHeaders requestHeaders;
        private final Long headersLength;
        private final PayloadMetadataHttpRequestMessage requestMessage;

        public PayloadMetadataHttpRequestMetadata(@JsonProperty("Entity-Digest") String entityDigest,@JsonProperty("Entity-Length") String entityLength, @JsonProperty("Entity-Trailing-Slop-Length") String entityTrailingSlopLength, @JsonProperty("Headers") PayloadMetadataHttpRequestHeaders requestHeaders, @JsonProperty("Headers-Length") String headersLength, @JsonProperty("Request-Message") PayloadMetadataHttpRequestMessage requestMessage) {
            this.entityDigest = entityDigest;
            this.entityLength = StringUtils.isBlank(entityLength) ? null : Long.parseLong(entityLength);
            this.entityTrailingSlopLength = StringUtils.isBlank(entityTrailingSlopLength) ? null : Long.parseLong(entityTrailingSlopLength);
            this.requestHeaders = requestHeaders;
            this.headersLength = StringUtils.isBlank(headersLength) ? null : Long.parseLong(headersLength);
            this.requestMessage = requestMessage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PayloadMetadataHttpRequestMetadata that = (PayloadMetadataHttpRequestMetadata) o;
            return Objects.equals(entityLength, that.entityLength) &&
                    Objects.equals(entityTrailingSlopLength, that.entityTrailingSlopLength) &&
                    Objects.equals(headersLength, that.headersLength) &&
                    Objects.equals(entityDigest, that.entityDigest) &&
                    Objects.equals(requestHeaders, that.requestHeaders) &&
                    Objects.equals(requestMessage, that.requestMessage);
        }

        @Override
        public int hashCode() {

            return Objects.hash(entityDigest, entityLength, entityTrailingSlopLength, requestHeaders, headersLength, requestMessage);
        }

        public String getEntityDigest() {
            return entityDigest;
        }

        public Long getEntityLength() {
            return entityLength;
        }

        public Long getEntityTrailingSlopLength() {
            return entityTrailingSlopLength;
        }

        public PayloadMetadataHttpRequestHeaders getRequestHeaders() {
            return requestHeaders;
        }

        public Long getHeadersLength() {
            return headersLength;
        }

        public PayloadMetadataHttpRequestMessage getRequestMessage() {
            return requestMessage;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("PayloadMetadataHttpRequestMetadata{");
            sb.append("entityDigest='").append(entityDigest).append('\'');
            sb.append(", entityLength=").append(entityLength);
            sb.append(", entityTrailingSlopLength=").append(entityTrailingSlopLength);
            sb.append(", requestHeaders=").append(requestHeaders);
            sb.append(", headersLength=").append(headersLength);
            sb.append(", requestMessage=").append(requestMessage);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class PayloadMetadataHttpRequestHeaders {
        private final String accept;
        private final String acceptEncoding;
        private final String acceptLanguage;
        private final String connection;
        private final String host;
        private final String userAgent;
        private final String ifModifiedSince;

        @JsonCreator
        public PayloadMetadataHttpRequestHeaders(@JsonProperty("Accept") String accept, @JsonProperty("Accept-Encoding") String acceptEncoding, @JsonProperty("Accept-Language") String acceptLanguage, @JsonProperty("Connection") String connection, @JsonProperty("Host") String host, @JsonProperty("User-Agent") String userAgent, @JsonProperty("If-Modified-Since") String ifModifiedSince) {
            this.accept = accept;
            this.acceptEncoding = acceptEncoding;
            this.acceptLanguage = acceptLanguage;
            this.connection = connection;
            this.host = host;
            this.userAgent = userAgent;
            this.ifModifiedSince = ifModifiedSince;
        }

        public String getAccept() {
            return accept;
        }

        public String getAcceptEncoding() {
            return acceptEncoding;
        }

        public String getAcceptLanguage() {
            return acceptLanguage;
        }

        public String getConnection() {
            return connection;
        }

        public String getHost() {
            return host;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public String getIfModifiedSince() {
            return ifModifiedSince;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PayloadMetadataHttpRequestHeaders that = (PayloadMetadataHttpRequestHeaders) o;
            return Objects.equals(accept, that.accept) &&
                    Objects.equals(acceptEncoding, that.acceptEncoding) &&
                    Objects.equals(acceptLanguage, that.acceptLanguage) &&
                    Objects.equals(connection, that.connection) &&
                    Objects.equals(host, that.host) &&
                    Objects.equals(ifModifiedSince, that.ifModifiedSince) &&
                    Objects.equals(userAgent, that.userAgent);
        }

        @Override
        public int hashCode() {

            return Objects.hash(accept, acceptEncoding, acceptLanguage, connection, host, userAgent, ifModifiedSince);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("PayloadMetadataHttpRequestHeaders{");
            sb.append("accept='").append(accept).append('\'');
            sb.append(", acceptEncoding='").append(acceptEncoding).append('\'');
            sb.append(", acceptLanguage='").append(acceptLanguage).append('\'');
            sb.append(", connection='").append(connection).append('\'');
            sb.append(", host='").append(host).append('\'');
            sb.append(", userAgent='").append(userAgent).append('\'');
            sb.append(", ifModifiedSince='").append(ifModifiedSince).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class PayloadMetadataHttpRequestMessage {
        private String method;
        private String path;
        private String protocol;
        private double version;

        @JsonCreator
        public PayloadMetadataHttpRequestMessage(@JsonProperty("Method") String method, @JsonProperty("Path") String path, @JsonProperty("Version") String version) {
            this.method = method;
            this.path = path;
            String[] components = version.split("/");
            ValidationUtils.validateAssertCondition(components.length == 2, "components length should be 2", version);
            this.protocol = components[0];
            this.version = Double.parseDouble(components[1]);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PayloadMetadataHttpRequestMessage that = (PayloadMetadataHttpRequestMessage) o;
            return Double.compare(that.version, version) == 0 &&
                    Objects.equals(method, that.method) &&
                    Objects.equals(path, that.path) &&
                    Objects.equals(protocol, that.protocol);
        }

        @Override
        public int hashCode() {

            return Objects.hash(method, path, protocol, version);
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("PayloadMetadataHttpRequestMessage{");
            sb.append("method=").append(method);
            sb.append(", path='").append(path).append('\'');
            sb.append(", protocol='").append(protocol).append('\'');
            sb.append(", version=").append(version);
            sb.append('}');
            return sb.toString();
        }
    }
}