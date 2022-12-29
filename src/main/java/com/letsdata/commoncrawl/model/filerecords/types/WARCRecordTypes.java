package com.letsdata.commoncrawl.model.filerecords.types;

import java.util.HashMap;
import java.util.Map;

public enum WARCRecordTypes {
    INFO("warcinfo"),
    REQUEST("request"),
    RESPONSE("response"),
    METADATA("metadata"),
    CONVERSION("conversion"),
    RESOURCE("resource"),
    REVISIT("revisit"),
    CONTINUATION("continuation");

    private String value;

    WARCRecordTypes(String value) {
        this.value = value;
    }

    private static final Map<String, WARCRecordTypes> valueTypeLookup = new HashMap<>();

    public static WARCRecordTypes fromTextValue(String value) {
        if (valueTypeLookup.size() == 0) {
            for (WARCRecordTypes warcRecordType : WARCRecordTypes.values()) {
                if (valueTypeLookup.containsKey(warcRecordType.value)) {
                    throw new RuntimeException("Duplicate value in WARRecordTypes");
                }
                valueTypeLookup.put(warcRecordType.value, warcRecordType);
            }
        }

        return valueTypeLookup.get(value.toLowerCase());
    }

    @Override
    public String toString() {
        return this.value;
    }
}
