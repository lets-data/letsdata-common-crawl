package com.letsdata.commoncrawl.model;

import java.util.HashMap;
import java.util.Map;

public enum WARCHeaderKeys {
    WARCType("WARC-Type"),
    WARCDate("WARC-Date"),
    WARCFilename("WARC-Filename"),
    WARCTargetURI("WARC-Target-URI"),
    WARCRecordId("WARC-Record-ID"),
    WARCRefersTo("WARC-Refers-To"),
    WARCConcurrentTo("WARC-Concurrent-To"),
    ContentType("Content-Type"),
    ContentLength("Content-Length"),
    WARCBlockDigest("WARC-Block-Digest"),
    WARCIdentifiedContentLanguage("WARC-Identified-Content-Language"),
    WARCIPAddress("WARC-IP-Address"),
    WARCInfoId("WARC-Warcinfo-ID"),
    WARCPayloadDigest("WARC-Payload-Digest"),
    WARCIdentifiedPayloadType("WARC-Identified-Payload-Type"),
    WARCTruncated("WARC-Truncated");

    String headerKeyValue;

    WARCHeaderKeys(String value) {
        this.headerKeyValue = value;
    }

    public String getHeaderKeyValue() {
        return headerKeyValue;
    }

    public void setHeaderKeyValue(String headerKeyValue) {
        this.headerKeyValue = headerKeyValue;
    }

    @Override
    public String toString() {
        return headerKeyValue;
    }

    private static final Map<String, WARCHeaderKeys> valueHeaderKeyMap = new HashMap();
    static {
        initLookupMap();
    }

    private static void initLookupMap() {
        if (valueHeaderKeyMap.size() == 0) {
            for (WARCHeaderKeys warcHeaderKey : WARCHeaderKeys.values()) {
                if (valueHeaderKeyMap.containsKey(warcHeaderKey.getHeaderKeyValue())) {
                    throw new RuntimeException("duplicate key in warcHeaderKeyMap");
                }
                valueHeaderKeyMap.put(warcHeaderKey.getHeaderKeyValue(), warcHeaderKey);
            }
        }
    }

    public static WARCHeaderKeys fromValue(String value) {
        initLookupMap();
        return valueHeaderKeyMap.get(value);
    }
}
