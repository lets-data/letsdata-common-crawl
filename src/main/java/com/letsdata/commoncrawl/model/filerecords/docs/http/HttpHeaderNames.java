package com.letsdata.commoncrawl.model.filerecords.docs.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum HttpHeaderNames {
    USER_AGENT("User-Agent"),
    ACCEPT("Accept"),
    ACCEPT_CHARSET("Accept-Charset"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_ENCODING("Accept-Encoding"),
    HOST("Host"),

    DATE("Date"),
    SERVER("Server"),
    P3P("P3P"),

    // Cookies
    COOKIE("Cookie"),
    SET_COOKIE("Set-Cookie"),

    // Authentication
    WWW_AUTHENTICATE("WWW-Authenticate"),
    AUTHORIZATION("Authorization"),
    PROXY_AUTHENTICATE("Proxy-Authenticate"),
    PROXY_AUTHORIZATION("Proxy-Authorization"),

    // connection
    CONNECTION("Connection"),
    KEEP_ALIVE("Keep-Alive"),

    // caching
    AGE("Age"),
    PRAGMA("Pragma"),
    CACHE_CONTROL("Cache-Control"),
    CLEAR_SITE_DATA("Clear-Site-Data"),
    WARNING("Warning"),

    // Conditionals
    VARY("Vary"),
    IF_MATCH("If-Match"),
    IF_NONE_MATCH("If-None-Match"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_UNMODIFIED_SINCE("If-Unmodified-Since"),

    // controls
    EXPECT("Expect"),
    MAX_FORWARDS("Max-Forwards"),
    CONTENT_DISPOSITION("Content-Disposition"), // present save as for download or show inline

    //content
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_LOCATION("Content-Location"),


    // cors
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
    ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials"),
    ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers"),
    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods"),
    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers"),
    ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age"),
    ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers"),
    ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method"),
    TIMING_ALLOW_ORIGIN("Timing-Allow-Origin"),

    // user state tracking
    DNT("DNT"),
    TK("Tk"),

    // resource
    EXPIRES("Expires"),
    ETAG("ETag"), // resource versions
    LAST_MODIFIED("Last-Modified"),
    LINK("Link"),

    X_ASPNET_VERSION("X-AspNet-Version"),
    X_POWERED_BY("X-Powered-By"),
    X_CACHE("X-Cache"),
    X_CRAWLER_TRANSFER_ENCODING("X-Crawler-Transfer-Encoding"),
    X_CRAWLER_CONTENT_ENCODING("X-Crawler-Content-Encoding"),
    X_CRAWLER_CONTENT_LENGTH("X-Crawler-Content-Length"),
    X_FRAMEWORK("X-Framework"),
    X_CLOUD_TRACE_CONTEXT("X-Cloud-Trace-Context"),
    X_DNS_PREFETCH_CONTROL("X-DNS-Prefetch-Control"),
    X_FIREFOX_SPDY("X-Firefox-Spdy "),
    X_PINGBACK("X-Pingback"),
    X_REQUESTED_WITH("X-Requested-With"),
    X_ROBOTS_TAG("X-Robots-Tag"),
    X_UA_COMPATOBLE("X-UA-Compatible"),

    // Proxy
    FORWARDED("Forwarded"),
    X_FORWARDED_FOR("X-Forwarded-For"),
    X_FORWARDED_HOST("X-Forwarded-Host"),
    X_FORWARDED_PROTO("X-Forwarded-Proto"),
    VIA("Via"),
    LOCATION("Location"),
    FROM("From"),
    REFERRER("Referer"),
    REFERRER_POLICY("Referrer-Policy"),
    ALLOW("Allow"),

    // ranges
    RANGE("Range"),
    ACCEPT_RANGES("Accept-Ranges"),
    IF_RANGE("If-Range"),
    CONTENT_RANGE("Content-Range"),

    // security
    CROSS_ORIGIN_EMBEDDER_POLICY("Cross-Origin-Embedder-Policy"),
    CROSS_ORIGIN_OPENER_POLICY("Cross-Origin-Opener-Policy"),
    CROSS_ORIGIN_RESOURCE_POLICY("Cross-Origin-Resource-Policy"),
    CONTENT_SECURITY_POLICY("Content-Security-Policy"),
    CONTENT_SECURITY_POLICY_REPORT_ONLY("Content-Security-Policy-Report-Only"),
    EXPECT_CT("Expect-CT"),
    FEATURE_POLICY("Feature-Policy"),
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    X_CONTENT_TYPE_OPTIONS("X-Content-Type-Options"),
    X_DOWNLOAD_OPTIONS("X-Download-Options"),
    X_FRAME_OPTIONS("X-Frame-Options"),
    X_PERMITTED_CROSS_DOMAIN_POLICIES("X-Permitted-Cross-Domain-Policies"),
    X_XSS_PROTECTION("X-XSS-Protection"),

    PUBLIC_KEY_PINS("Public-Key-Pins"),
    PUBLIC_KEY_PINS_REPORT_ONLY("Public-Key-Pins-Report-Only"),

    SEC_FETCH_SITE("Sec-Fetch-Site"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SEC_FETCH_USER("Sec-Fetch-User"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),

    LAST_EVENT_ID("Last-Event-ID"),
    PING_FROM("Ping-From"),
    PING_TO("Ping-To"),
    REPORT_TO("Report-To"),

    TRANSFER_ENCODING("Transfer-Encoding"),
    TE("TE"),
    TRAILER("Trailer"),

    //websockets
    SEC_WEBSOCKET_KEY("Sec-WebSocket-Key"),
    SEC_WEBSOCKET_EXTENSIONS("Sec-WebSocket-Extensions"),
    SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept"),
    SEC_WEBSOCKET_PROTOCOL("Sec-WebSocket-Protocol"),
    SEC_WEBSOCKET_VERSION("Sec-WebSocket-Version"),

    ACCEPT_PUSH_POLICY("Accept-Push-Policy"),
    ACCEPT_SIGNATURE("Accept-Signature"),
    ALT_SVC("Alt-Svc"),
    LARGE_ALLOCATION("Large-Allocation"),
    PUSH_POLICY("Push-Policy"),
    RETRY_AFTER("Retry-After"),
    SIGNATURE("Signature"),
    SIGNED_HEADERS("Signed-Headers"),
    SERVER_TIMING("Server-Timing"),
    SERVICE_WORKER_ALLOWED("Service-Worker-Allowed"),
    SOURCE_MAP("SourceMap"),
    UPGRADE("Upgrade"),

    CF_CACHE_STATUS("CF-Cache-Status"),
    CF_REQUEST_ID("cf-request-id"),
    CF_RAY("CF-RAY");

    String headerName;

    HttpHeaderNames(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String toString() {
        return headerName;
    }

    private static Map<String, HttpHeaderNames> stringHeaderNameMap = null;
    public static HttpHeaderNames getHttpHeaderNameFromString(String headerName) {
        if (stringHeaderNameMap == null) {
            synchronized (HttpHeaderNames.class) {
                if (stringHeaderNameMap == null) {
                    Map<String, HttpHeaderNames> lookupMap = new HashMap<>();
                    for (HttpHeaderNames currHttpHeader : HttpHeaderNames.values()) {
                        if (lookupMap.containsKey(currHttpHeader.headerName)) {
                            throw new RuntimeException("duplicate headerName in lookup map "+currHttpHeader.headerName);
                        }

                        lookupMap.put(currHttpHeader.headerName, currHttpHeader);
                    }
                    stringHeaderNameMap = Collections.unmodifiableMap(lookupMap);
                }
            }
        }

        return stringHeaderNameMap.get(headerName);
    }
}
