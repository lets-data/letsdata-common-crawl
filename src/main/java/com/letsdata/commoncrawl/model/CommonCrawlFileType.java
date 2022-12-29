package com.letsdata.commoncrawl.model;

public enum CommonCrawlFileType {

    WARC("WARC/1.0\r\nWARC-Type: request\r\n", ".warc", ".warc.crc", ".warc.gz"),
    WAT("WARC/1.0\r\nWARC-Type: metadata\r\nWARC-Target-URI: ", ".wat", ".wat.crc", ".warc.wat.gz"),
    WET("WARC/1.0\r\nWARC-Type: conversion", ".wet", ".wet.crc", ".warc.wet.gz");

    String recordStartPhrase;
    String extension;
    String crcExtension;
    String gzipExtension;

    CommonCrawlFileType(String recordStartPhrase, String fileExtension, String crcFileExtension, String gzipFileExtension) {
        this.recordStartPhrase = recordStartPhrase;
        this.extension = fileExtension;
        this.crcExtension = crcFileExtension;
        this.gzipExtension = gzipFileExtension;
    }

    public String getRecordStartPhrase() {
        return recordStartPhrase;
    }

    public void setRecordStartPhrase(String recordStartPhrase) {
        this.recordStartPhrase = recordStartPhrase;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getGzipExtension() {
        return gzipExtension;
    }

    public void setGzipExtension(String gzipExtension) {
        this.gzipExtension = gzipExtension;
    }

    public String getCrcExtension() {
        return crcExtension;
    }

    public void setCrcExtension(String crcExtension) {
        this.crcExtension = crcExtension;
    }
}
