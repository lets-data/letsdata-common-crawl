package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcHttpResponseDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.http.HtmlMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.http.HttpHeaderNames;
import com.letsdata.commoncrawl.util.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WarcResponsePayloadMetadata extends PayloadMetadata {
    private final String entityDigest;
    private final Long entityLength;
    private final Long entityTrailingSlopLength;
    private final HtmlMetadataDoc htmlMetadataDoc;
    private final Long headersLength;
    private final WarcHttpResponseDoc responseDoc;


    /*
        "Payload-Metadata": {
            "Actual-Content-Length": "5149",
            "Actual-Content-Type": "application/http; msgtype=response",
            "Block-Digest": "sha1:VSQOTNLEMFURZUXHVVRYNYHGDTPZG6PW",
            "Trailing-Slop-Length": "4",
            "HTTP-Response-Metadata": {
                "Entity-Digest": "sha1:ZA4S2ROKJQBBZUB7HNCIF4B343CJJLNW",
                "Entity-Length": "4951",
                "Entity-Trailing-Slop-Length": "0",
                "HTML-Metadata": {
                    "Head": {
                        "Link": [
                            {
                                "path": "LINK@/href",
                                "rel": "shortcut icon",
                                "url": "/favicon.ico"
                            },
                            {
                                "path": "LINK@/href",
                                "rel": "stylesheet",
                                "type": "text/css",
                                "url": "http://fonts.googleapis.com/css?family=Sofia"
                            }
                        ],
                        "Metas": [
                            {
                                "content": "text/html; charset=shift_JIS",
                                "http-equiv": "content-type"
                            },
                            {
                                "content": "no-cache",
                                "http-equiv": "Cache-control"
                            },
                            {
                                "content": "application/xhtml+xml; charset=Shift_JIS",
                                "http-equiv": "Content-Type"
                            },
                            {
                                "content": "no-cache",
                                "http-equiv": "Pragma"
                            },
                            {
                                "content": "no-cache",
                                "http-equiv": "Cache-Control"
                            },
                            {
                                "content": "noindex, follow",
                                "name": "robots"
                            },
                            {
                                "content": "none,noindex,nofollow,noarchive",
                                "name": "robots"
                            },
                            {
                                "content": "noindex,nofollow,noarchive",
                                "name": "robots"
                            },
                            {
                                "content": "none",
                                "name": "robots"
                            },
                            {
                                "content": "NODIFF,NOINDEX",
                                "name": "HATENA"
                            },
                            {
                                "content": "NOINDEX,NOFOLLOW",
                                "name": "GOOGLEBOT"
                            },
                            {
                                "content": "NOARCHIVE",
                                "name": "GOOGLEBOT"
                            },
                            {
                                "content": "NOSNIPPET",
                                "name": "GOOGLEBOT"
                            },
                            {
                                "content": "no-cache",
                                "http-equiv": "Pragma"
                            },
                            {
                                "content": "no-cache",
                                "http-equiv": "Cache-Control"
                            }
                        ],
                        "Scripts": [
                            {
                                "path": "SCRIPT@/src",
                                "type": "text/javascript",
                                "url": "http://adm.shinobi.jp/st/na.js"
                            }
                        ],
                        "Title": "ranking(No.1 \u2192 No.10)"
                    },
                    "Links": [
                        {
                            "path": "A@/href",
                            "text": "\u6d77\u8cca\u5922RANK",
                            "title": "\u6d77\u8cca\u5922RANK",
                            "url": "http://01.rknt.jp/link.php?id=opdreamrank&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "\u8d64\u9aea\u6d77\u8cca\u56e3Rank!",
                            "title": "\u8d64\u9aea\u6d77\u8cca\u56e3Rank!",
                            "url": "http://01.rknt.jp/link.php?id=redredred&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "\u71b1\u5e2f\u9b5a",
                            "title": "\u71b1\u5e2f\u9b5a",
                            "url": "http://01.rknt.jp/link.php?id=48c100506&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "\u4e0d\u6b7b\u9ce5\u306e\u80cc\u4e2d",
                            "title": "\u4e0d\u6b7b\u9ce5\u306e\u80cc\u4e2d",
                            "url": "http://01.rknt.jp/link.php?id=marco05&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "\u6d77\u8cca\u65d7\u3092\u63b2\u3052\u3066",
                            "title": "\u6d77\u8cca\u65d7\u3092\u63b2\u3052\u3066",
                            "url": "http://01.rknt.jp/link.php?id=kaizokud&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "vase",
                            "title": "vase",
                            "url": "http://01.rknt.jp/link.php?id=vase0509&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "RED RANK",
                            "title": "RED RANK",
                            "url": "http://01.rknt.jp/link.php?id=flame0red&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "\u4fe1\u53f7\u6a5f",
                            "title": "\u4fe1\u53f7\u6a5f",
                            "url": "http://01.rknt.jp/link.php?id=jdtp&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "OP*BLD\uff97\uff9d\uff77\uff9d\uff78\uff9e",
                            "title": "OP*BLD\uff97\uff9d\uff77\uff9d\uff78\uff9e",
                            "url": "http://01.rknt.jp/link.php?id=opbldr&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "\u72c2\u3063\u305f\u304a\u8336\u4f1a",
                            "title": "\u72c2\u3063\u305f\u304a\u8336\u4f1a",
                            "url": "http://01.rknt.jp/link.php?id=amtp&table=m5130"
                        },
                        {
                            "path": "A@/href",
                            "text": "No.11 \u2192 No.14",
                            "title": "ranking(No.11 \u2192 No.14)",
                            "url": "./rank.php?page=1&id=m5130&genre=607835"
                        },
                        {
                            "path": "A@/href",
                            "text": "back",
                            "url": "m5130/"
                        }
                    ]
                },
                "Headers": {
                    "Accept-Ranges": "bytes",
                    "Connection": "close",
                    "Content-Length": "4951",
                    "Content-Type": "text/html",
                    "Date": "Thu, 02 Jul 2020 05:22:05 GMT",
                    "Server": "Apache",
                    "X-Crawler-Transfer-Encoding": "chunked"
                },
                "Headers-Length": "198",
                "Response-Message": {
                    "Reason": "OK",
                    "Status": "200",
                    "Version": "HTTP/1.1"
                }
            },
        }
    */

    public WarcResponsePayloadMetadata(@JsonProperty("Actual-Content-Length") String actualContentLength, @JsonProperty("Actual-Content-Type") String actualContentType, @JsonProperty("Block-Digest") String blockDigest, @JsonProperty("Trailing-Slop-Length") String trailingSlopLength, @JsonProperty ("HTTP-Response-Metadata") ResponsePayloadHttpMetadata httpResponseMetadata) {
        super(StringUtils.isBlank(actualContentLength) ? null : Long.parseLong(actualContentLength), ContentType.fromString(actualContentType), blockDigest, StringUtils.isBlank(trailingSlopLength) ? null : Long.parseLong(trailingSlopLength));
        this.entityDigest = httpResponseMetadata.entityDigest;
        this.entityLength = httpResponseMetadata.entityLength;
        this.entityTrailingSlopLength = httpResponseMetadata.entityTrailingSlopLength;
        this.htmlMetadataDoc = httpResponseMetadata.htmlMetadata;
        this.headersLength = httpResponseMetadata.headersLength;
        this.responseDoc = httpResponseMetadata.httpResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WarcResponsePayloadMetadata that = (WarcResponsePayloadMetadata) o;
        return Objects.equals(entityLength, that.entityLength) &&
                Objects.equals(entityTrailingSlopLength, that.entityTrailingSlopLength) &&
                Objects.equals(headersLength, that.headersLength) &&
                Objects.equals(entityDigest, that.entityDigest) &&
                Objects.equals(responseDoc, that.responseDoc) &&
                Objects.equals(htmlMetadataDoc, that.htmlMetadataDoc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), entityDigest, entityLength, entityTrailingSlopLength, htmlMetadataDoc, headersLength, responseDoc);
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

    public HtmlMetadataDoc getHtmlMetadataDoc() {
        return htmlMetadataDoc;
    }

    public Long getHeadersLength() {
        return headersLength;
    }

    public WarcHttpResponseDoc getResponseDoc() {
        return responseDoc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcResponsePayloadMetadata{");
        sb.append("entityDigest='").append(entityDigest).append('\'');
        sb.append(", entityLength=").append(entityLength);
        sb.append(", entityTrailingSlopLength=").append(entityTrailingSlopLength);
        sb.append(", htmlMetadataDoc=").append(htmlMetadataDoc);
        sb.append(", headersLength=").append(headersLength);
        sb.append(", actualContentLength=").append(actualContentLength);
        sb.append(", actualContentType=").append(actualContentType);
        sb.append(", blockDigest='").append(blockDigest).append('\'');
        sb.append(", trailingSlopLength=").append(trailingSlopLength);
        sb.append(", responseDoc=").append(responseDoc);
        sb.append('}');
        return sb.toString();
    }

    public static class HttpResponseMessage {
        String reason;
        Double statusCode;
        String protocol;
        Double version;

        /*
            "Reason": "OK",
            "Status": "200",
            "Version": "HTTP/1.1"
        * */
        @JsonCreator
        public HttpResponseMessage(@JsonProperty("Reason") String reason, @JsonProperty("Status") String status, @JsonProperty("Version") String version) {
            this.reason = reason;
            this.statusCode = StringUtils.isBlank(status) ? null : Double.parseDouble(status);
            this.protocol = StringUtils.isBlank(version) ? null : version.split("/")[0].trim();
            this.version = StringUtils.isBlank(version) ? null : Double.parseDouble(version.split("/")[1].trim());
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Double getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Double statusCode) {
            this.statusCode = statusCode;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public Double getVersion() {
            return version;
        }

        public void setVersion(Double version) {
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HttpResponseMessage that = (HttpResponseMessage) o;
            return Objects.equals(reason, that.reason) &&
                    Objects.equals(statusCode, that.statusCode) &&
                    Objects.equals(protocol, that.protocol) &&
                    Objects.equals(version, that.version);
        }

        @Override
        public int hashCode() {

            return Objects.hash(reason, statusCode, protocol, version);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("HttpResponseMessage{");
            sb.append("reason='").append(reason).append('\'');
            sb.append(", statusCode=").append(statusCode);
            sb.append(", protocol='").append(protocol).append('\'');
            sb.append(", version=").append(version);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class ResponsePayloadHttpMetadata {
        private final String entityDigest;
        private final Long entityLength;
        private final Long entityTrailingSlopLength;
        private final Long headersLength;
        private final boolean headersCorrupt;
        private final HtmlMetadataDoc htmlMetadata;
        private final WarcHttpResponseDoc httpResponse;

        @JsonCreator
        public ResponsePayloadHttpMetadata(@JsonProperty("Entity-Digest") String entityDigest, @JsonProperty("Entity-Length") String entityLength, @JsonProperty("Entity-Trailing-Slop-Length") String entityTrailingSlopLength, @JsonProperty("Headers-Length") String headersLength, @JsonProperty("HTML-Metadata") HtmlMetadataDoc htmlMetadata, @JsonProperty("Headers") Map<String, String> headers, @JsonProperty("Response-Message") HttpResponseMessage httpResponse, @JsonProperty("Headers-Corrupt") boolean headersCorrupt) throws Exception {

            this.entityDigest = entityDigest;
            this.entityLength = StringUtils.isBlank(entityLength) ? null : Long.parseLong(entityLength);
            this.entityTrailingSlopLength = StringUtils.isBlank(entityTrailingSlopLength) ? null : Long.parseLong(entityTrailingSlopLength);
            this.headersLength = StringUtils.isBlank(headersLength) ? null : Long.parseLong(headersLength);
            this.headersCorrupt = headersCorrupt;
            this.htmlMetadata = htmlMetadata;
            String dateStr = headers.containsKey(HttpHeaderNames.DATE.toString()) ? headers.remove(HttpHeaderNames.DATE.toString()) : null;
            this.httpResponse = new WarcHttpResponseDoc(httpResponse.protocol, httpResponse.version, httpResponse.statusCode, httpResponse.reason,
                    headers.containsKey(HttpHeaderNames.CONTENT_TYPE.toString()) ? headers.remove(HttpHeaderNames.CONTENT_TYPE.toString()) : null,
                    headers.containsKey(HttpHeaderNames.CONTENT_LENGTH.toString()) ? Long.parseLong(headers.remove(HttpHeaderNames.CONTENT_LENGTH.toString())) : null,
                    StringUtils.isBlank(dateStr) ? null : DateUtils.getDateFromDateString(dateStr),
                    headers.containsKey(HttpHeaderNames.SERVER.toString()) ? headers.remove(HttpHeaderNames.SERVER.toString()) : null,
                    headers.containsKey(HttpHeaderNames.CONNECTION.toString()) ? headers.remove(HttpHeaderNames.CONNECTION.toString()) : null,
                    headers.containsKey(HttpHeaderNames.SET_COOKIE.toString()) ? headers.remove(HttpHeaderNames.SET_COOKIE.toString()) : null,
                    headers.containsKey(HttpHeaderNames.ETAG.toString()) ? headers.remove(HttpHeaderNames.ETAG.toString()): null,
                    null
                    );

            Map<HttpHeaderNames, String> headerNameValueMap = new HashMap<>();
            for (String headerName : headers.keySet()) {
                HttpHeaderNames httpheaderName = HttpHeaderNames.getHttpHeaderNameFromString(headerName);
                if (httpheaderName != null) {
                    headerNameValueMap.put(httpheaderName, headers.get(httpheaderName.getHeaderName()));
                }
            }
            WarcHttpResponseDoc.setHttpHeadersFromMap(headerNameValueMap, this.httpResponse);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResponsePayloadHttpMetadata that = (ResponsePayloadHttpMetadata) o;
            return entityLength == that.entityLength &&
                    entityTrailingSlopLength == that.entityTrailingSlopLength &&
                    headersLength == that.headersLength &&
                    Objects.equals(headersCorrupt, that.headersCorrupt) &&
                    Objects.equals(entityDigest, that.entityDigest) &&
                    Objects.equals(htmlMetadata, that.htmlMetadata) &&
                    Objects.equals(httpResponse, that.httpResponse);
        }

        @Override
        public int hashCode() {

            return Objects.hash(entityDigest, entityLength, entityTrailingSlopLength, headersLength, htmlMetadata, httpResponse, headersCorrupt);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ResponsePayloadHttpMetadata{");
            sb.append("entityDigest='").append(entityDigest).append('\'');
            sb.append(", entityLength=").append(entityLength);
            sb.append(", entityTrailingSlopLength=").append(entityTrailingSlopLength);
            sb.append(", headersCorrupt=").append(headersCorrupt);
            sb.append(", headersLength=").append(headersLength);
            sb.append(", htmlMetadata=").append(htmlMetadata);
            sb.append(", httpResponse=").append(httpResponse);
            sb.append('}');
            return sb.toString();
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

        public HtmlMetadataDoc getHtmlMetadata() {
            return htmlMetadata;
        }

        public WarcHttpResponseDoc getHttpResponse() {
            return httpResponse;
        }

        public boolean isHeadersCorrupt() {
            return headersCorrupt;
        }
    }
}