package com.letsdata.commoncrawl.model;

public class WARCFileReaderConstants {
    public static final String WARC_INFO_EXTRACTED_DATE_FORMAT = "E, dd MMM yyyy HH:mm:ss z";
    public static final String WARC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String EPOCH_MILLIS_FORMAT= "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String WARC_RECORD_START_PATTERN = "WARC/1.0\r\nWARC-Type:";
    public static final String WARC_RECORD_HEADER_END_PATTERN = "\r\n\r\n";
    public static final String WARC_HEADER_LINE_END_PATTERN = "\r\n";
    public static final String WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN = ": ";
}
