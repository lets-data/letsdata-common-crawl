package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

public enum WatMetadataTypeKeys {
    WARCInfoMetadata("WARC-Info-Metadata"),
    WARCRequestMetadata("HTTP-Request-Metadata"),
    WARCResponseMetadata("HTTP-Response-Metadata"),
    WARCMetadataMetadata("WARC-Metadata-Metadata");

    String keyValue;

    WatMetadataTypeKeys(String value) {
        this.keyValue = value;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public String toString() {
        return keyValue;
    }
}