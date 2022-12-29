package com.letsdata.commoncrawl.model;

public class CrawlDataRecordErrorException extends Exception {

    private static final long serialVersionUID = 6219635958041401517L;

    public CrawlDataRecordErrorException() {
        super();
    }

    public CrawlDataRecordErrorException(String message) {
        super(message);
    }

    public CrawlDataRecordErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrawlDataRecordErrorException(Throwable cause) {
        super(cause);
    }
}
