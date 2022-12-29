package com.letsdata.commoncrawl.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ContentType {
    TEXT_HTML(1, "text/html"),
    APPLICATION_JSON(2, "application/json"),
    TEXT_PLAIN(3, "text/plain"),
    APPLICATION_WARC_FIELDS(4, "application/warc-fields"),
    APPLICATION_HTTP(5, "application/http"),;

    private final int contentTypeId;
    private final String strValue;

    private ContentType(int contentTypeId, String strValue) {
        this.contentTypeId = contentTypeId;
        this.strValue = strValue;
    }

    public int getContentTypeId() {
        return contentTypeId;
    }

    public String getStrValue() {
        return strValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContentType{");
        sb.append("contentTypeId=").append(contentTypeId);
        sb.append("strValue=").append(strValue);
        sb.append('}');
        return sb.toString();
    }

    private static Map<Integer, ContentType> idContentTypeMap = null;
    public static ContentType fromContentTypeId(int contentTypeId) {
        if (idContentTypeMap == null) {
            synchronized (ContentType.class) {
                if (idContentTypeMap == null) {
                    Map<Integer, ContentType> lookupMap = new HashMap<>();
                    for (ContentType contentType : ContentType.values()) {
                        if (lookupMap.containsKey(contentType.contentTypeId)) {
                            throw new RuntimeException("duplicate content type id in the lookup map");
                        }

                        lookupMap.put(contentType.contentTypeId, contentType);
                    }
                    idContentTypeMap = Collections.unmodifiableMap(lookupMap);
                }
            }
        }

        return idContentTypeMap.get(contentTypeId);
    }

    private static Map<String, ContentType> strValueContentTypeMap = null;
    public static ContentType fromString(String contentTypeStr) {
        if (strValueContentTypeMap == null) {
            synchronized (ContentType.class) {
                if (strValueContentTypeMap == null) {
                    Map<String, ContentType> lookupMap = new HashMap<>();
                    for (ContentType contentType : ContentType.values()) {
                        if (lookupMap.containsKey(contentType.strValue)) {
                            throw new RuntimeException("duplicate content type string in the lookup map");
                        }

                        lookupMap.put(contentType.strValue, contentType);
                    }
                    strValueContentTypeMap = Collections.unmodifiableMap(lookupMap);
                }
            }
        }

        return strValueContentTypeMap.get(contentTypeStr);
    }
}
