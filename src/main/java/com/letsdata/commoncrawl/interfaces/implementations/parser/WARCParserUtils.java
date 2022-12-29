package com.letsdata.commoncrawl.interfaces.implementations.parser;

import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.Language;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.WARCHeaderKeys;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.StringFunctions;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class WARCParserUtils {
    private static final Logger logger = LoggerFactory.getLogger(WARCParserUtils.class);

    public static enum WARCFileRecordTypes {
        INFO("WARC/1.0\r\nWARC-Type: warcinfo\r\n", "WARC/1.0\r\nWARC-Type: request\r\n"),
        REQUEST("WARC/1.0\r\nWARC-Type: request\r\n", "WARC/1.0\r\nWARC-Type: response\r\n"),
        RESPONSE("WARC/1.0\r\nWARC-Type: response\r\n", "WARC/1.0\r\nWARC-Type: metadata\r\n"),
        METADATA("WARC/1.0\r\nWARC-Type: metadata\r\n", "WARC/1.0\r\nWARC-Type: request\r\n");

        private final String recordStartPhrase;
        private final String recordEndPhrase;

        WARCFileRecordTypes(String recordStartPhrase, String recordEndPhrase) {
            this.recordStartPhrase = recordStartPhrase;
            this.recordEndPhrase = recordEndPhrase;
        }

        public String getRecordStartPhrase() {
            return recordStartPhrase;
        }

        public String getRecordEndPhrase() {
            return recordEndPhrase;
        }
    }

    public static enum WATFileRecordTypes {
        INFO("WARC/1.0\r\nWARC-Type: warcinfo\r\n", "WARC/1.0\r\nWARC-Type: metadata\r\n"),
        METADATA("WARC/1.0\r\nWARC-Type: metadata\r\n", "WARC/1.0\r\nWARC-Type: metadata\r\n");

        private final String recordStartPhrase;
        private final String recordEndPhrase;

        WATFileRecordTypes(String recordStartPhrase, String recordEndPhrase) {
            this.recordStartPhrase = recordStartPhrase;
            this.recordEndPhrase = recordEndPhrase;
        }

        public String getRecordStartPhrase() {
            return recordStartPhrase;
        }

        public String getRecordEndPhrase() {
            return recordEndPhrase;
        }
    }

    public static enum WETFileRecordTypes {
        INFO("WARC/1.0\r\nWARC-Type: warcinfo\r\n", "WARC/1.0\r\nWARC-Type: conversion\r\n"),
        CONVERSION("WARC/1.0\r\nWARC-Type: conversion\r\n", "WARC/1.0\r\nWARC-Type: conversion\r\n");

        private final String recordStartPhrase;
        private final String recordEndPhrase;

        WETFileRecordTypes(String recordStartPhrase, String recordEndPhrase) {
            this.recordStartPhrase = recordStartPhrase;
            this.recordEndPhrase = recordEndPhrase;
        }

        public String getRecordStartPhrase() {
            return recordStartPhrase;
        }

        public String getRecordEndPhrase() {
            return recordEndPhrase;
        }
    }

    public static class WarcHeaderFieldsParseResult {
        private final String protocol;
        private final double version;
        private final WARCRecordTypes recordType;
        private final Date date;
        private final String filename;
        private final String recordId;
        private final ContentType contentType;
        private final int contentLength;
        private final String targetUri;
        private final String refersTo;
        private final String concurrentTo;
        private final String blockDigest;
        private final Language language;
        private final String infoId;
        private final InetAddress ipAddress;
        private final String payloadDigest;
        private final String identifiedPayloadType;
        private final String truncated;

        public WarcHeaderFieldsParseResult(String protocol, double version, WARCRecordTypes recordType, Date date, String filename, String recordId, ContentType contentType, int contentLength, String targetUri, String refersTo, String concurrentTo, String blockDigest, Language language, String infoId, InetAddress ipAddress, String payloadDigest, String identifiedPayloadType, String truncated) {
            this.protocol = protocol;
            this.version = version;
            this.recordType = recordType;
            this.date = date;
            this.filename = filename;
            this.recordId = recordId;
            this.contentType = contentType;
            this.contentLength = contentLength;
            this.targetUri = targetUri;
            this.refersTo = refersTo;
            this.concurrentTo = concurrentTo;
            this.blockDigest = blockDigest;
            this.language = language;
            this.infoId = infoId;
            this.ipAddress = ipAddress;
            this.payloadDigest = payloadDigest;
            this.identifiedPayloadType = identifiedPayloadType;
            this.truncated = truncated;
        }

        public String getProtocol() {
            return protocol;
        }

        public double getVersion() {
            return version;
        }

        public WARCRecordTypes getRecordType() {
            return recordType;
        }

        public Date getDate() {
            return date;
        }

        public String getFilename() {
            return filename;
        }

        public String getRecordId() {
            return recordId;
        }

        public ContentType getContentType() {
            return contentType;
        }

        public int getContentLength() {
            return contentLength;
        }

        public String getTargetUri() {
            return targetUri;
        }

        public String getRefersTo() {
            return refersTo;
        }

        public String getConcurrentTo() {
            return concurrentTo;
        }

        public String getBlockDigest() {
            return blockDigest;
        }

        public Language getLanguage() {
            return language;
        }

        public String getInfoId() {
            return infoId;
        }

        public InetAddress getIpAddress() {
            return ipAddress;
        }

        public String getPayloadDigest() {
            return payloadDigest;
        }

        public String getIdentifiedPayloadType() {
            return identifiedPayloadType;
        }

        public String getTruncated() {
            return truncated;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WarcHeaderFieldsParseResult that = (WarcHeaderFieldsParseResult) o;
            return Double.compare(that.version, version) == 0 &&
                    contentLength == that.contentLength &&
                    Objects.equals(protocol, that.protocol) &&
                    recordType == that.recordType &&
                    Objects.equals(date, that.date) &&
                    Objects.equals(filename, that.filename) &&
                    Objects.equals(recordId, that.recordId) &&
                    contentType == that.contentType &&
                    Objects.equals(targetUri, that.targetUri) &&
                    Objects.equals(refersTo, that.refersTo) &&
                    Objects.equals(concurrentTo, that.concurrentTo) &&
                    Objects.equals(blockDigest, that.blockDigest) &&
                    Objects.equals(language, that.language) &&
                    Objects.equals(infoId, that.infoId) &&
                    Objects.equals(ipAddress, that.ipAddress) &&
                    Objects.equals(payloadDigest, that.payloadDigest) &&
                    Objects.equals(identifiedPayloadType, that.identifiedPayloadType) &&
                    Objects.equals(truncated, that.truncated);
        }

        @Override
        public int hashCode() {

            return Objects.hash(protocol, version, recordType, date, filename, recordId, contentType, contentLength, targetUri, refersTo, concurrentTo, blockDigest, language, infoId, ipAddress, payloadDigest, identifiedPayloadType, truncated);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WarcHeaderFieldsParseResult{");
            sb.append("protocol='").append(protocol).append('\'');
            sb.append(", version=").append(version);
            sb.append(", recordType=").append(recordType);
            sb.append(", date=").append(date);
            sb.append(", filename='").append(filename).append('\'');
            sb.append(", recordId='").append(recordId).append('\'');
            sb.append(", contentType=").append(contentType);
            sb.append(", contentLength=").append(contentLength);
            sb.append(", targetUri='").append(targetUri).append('\'');
            sb.append(", refersTo='").append(refersTo).append('\'');
            sb.append(", concurrentTo='").append(concurrentTo).append('\'');
            sb.append(", blockDigest='").append(blockDigest).append('\'');
            sb.append(", language='").append(language).append('\'');
            sb.append(", infoId='").append(infoId).append('\'');
            sb.append(", ipAddress='").append(ipAddress).append('\'');
            sb.append(", payloadDigest='").append(payloadDigest).append('\'');
            sb.append(", identifiedPayloadType='").append(identifiedPayloadType).append('\'');
            sb.append(", truncated='").append(truncated).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static WarcHeaderFieldsParseResult parseWarcHeaderFields(String fileName, String fileType, byte[] byteArr, int startIndex, int endIndex) throws Exception {
        SimpleDateFormat WARC_HEADER_DATE_SIMPLE_FORMAT = new SimpleDateFormat(WARCFileReaderConstants.WARC_DATE_FORMAT, Locale.US);
        WARC_HEADER_DATE_SIMPLE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));

        int currIndex = startIndex; // sb.indexOf("WARC/1.0\r\n", WARCHeaderStartIndex);

        String protocol = null;
        double version = -1;
        WARCRecordTypes recordType = null;
        Date date = null;
        String filename = null;
        String recordId = null;
        ContentType contentType = null;
        String contentTypeContext = null;
        int contentLength = -1;
        String targetUri = null;
        String refersTo = null;
        String concurrentTo = null;
        String blockDigest = null;
        Language language = null;
        String infoId = null;
        InetAddress ipAddress = null;
        String payloadDigest = null;
        String identifiedPayloadType = null;
        String truncated = null;
        do {
            int newLineIndex = Matcher.match(byteArr, currIndex, endIndex, WARCFileReaderConstants.WARC_HEADER_LINE_END_PATTERN);
            ValidationUtils.validateAssertCondition(newLineIndex < endIndex, "new line index should be less than endIndex", startIndex, endIndex, newLineIndex);
            if (newLineIndex <= currIndex) {
                newLineIndex = endIndex;
            }

            int separatorIndex = Matcher.match(byteArr, currIndex, newLineIndex,  WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN);
            ValidationUtils.validateAssertCondition(separatorIndex < newLineIndex, "separator index should be less than new line index", currIndex, newLineIndex, separatorIndex);
            if (separatorIndex != -1) {
                String key = new String(byteArr, currIndex, separatorIndex-currIndex, "utf-8").trim();
                int valueStartIndex = separatorIndex+WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN.getBytes("utf-8").length;
                String value = new String(byteArr, valueStartIndex , newLineIndex-valueStartIndex , "utf-8").trim();
                WARCHeaderKeys headerKey = WARCHeaderKeys.fromValue(key);
                switch (headerKey) {
                    case WARCType: {
                        recordType = WARCRecordTypes.fromTextValue(value);
                        break;
                    }
                    case WARCDate: {
                        try {
                            date = WARC_HEADER_DATE_SIMPLE_FORMAT.parse(value);
                        } catch(Exception ex) {
                            logger.error("unable to parse date from warc record - date: {}, format: {}, filename: {} - ex: {}", value, WARC_HEADER_DATE_SIMPLE_FORMAT.toPattern(), fileType+fileName, ex);
                            throw new RuntimeException("unable to parse date from warc record", ex);
                        }
                        break;
                    }

                    case WARCFilename: {
                        filename = value;
                        break;
                    }
                    case WARCRecordId: {
                        recordId = value;
                        break;
                    }
                    case ContentType: {
                        if (value.contains(";")) {
                            String[] components = value.split(";");
                            contentType = ContentType.fromString(components[0].trim());
                            contentTypeContext = components[1].trim();
                        } else {
                            contentType = ContentType.fromString(value);
                            contentTypeContext = null;
                        }

                        break;
                    }
                    case ContentLength: {
                        contentLength = Integer.parseInt(value);
                        break;
                    }
                    case WARCTargetURI: {
                        targetUri = value;
                        break;
                    }
                    case WARCRefersTo: {
                        refersTo = value;
                        break;
                    }
                    case WARCConcurrentTo: {
                        concurrentTo = value;
                        break;
                    }
                    case WARCBlockDigest: {
                        blockDigest = value;
                        break;
                    }
                    case WARCIdentifiedContentLanguage: {
                        language = Language.fromISOCode(value);
                        break;
                    }
                    case WARCInfoId: {
                        infoId = value;
                        break;
                    }
                    case WARCIPAddress: {
                        ipAddress = InetAddress.getByName(value);
                        break;
                    }
                    case WARCPayloadDigest: {
                        payloadDigest = value;
                        break;
                    }
                    case WARCIdentifiedPayloadType: {
                        identifiedPayloadType = value;
                        break;
                    }
                    case WARCTruncated: {
                        truncated = value;
                        break;
                    }
                    default:{
                        logger.error("unable to parse header key warc record - unknown header key: {}, string: {}", headerKey, new String(byteArr, currIndex, separatorIndex-currIndex, "utf-8"));
                        throw new RuntimeException("unable to parse header key warc record - unknown header key");
                    }
                }
            } else {
                String headerLine = new String(byteArr, startIndex, newLineIndex-startIndex, "utf-8");
                separatorIndex = headerLine.indexOf("/");
                if (separatorIndex < 0 || separatorIndex >= newLineIndex) {
                    throw new RuntimeException("");
                }
                protocol = headerLine.substring(0, separatorIndex);
                version = Double.parseDouble(headerLine.substring(separatorIndex+1, headerLine.length()).trim());
            }
            currIndex = newLineIndex+WARCFileReaderConstants.WARC_HEADER_FIELD_NAME_SEPARATOR_PATTERN.getBytes("utf-8").length;
        } while (currIndex < endIndex);

        // assert mandatory fields
        ValidationUtils.validateAssertCondition(protocol != null && StringUtils.equalsIgnoreCase(protocol, "WARC"), "protocol should be WARC ");
        ValidationUtils.validateAssertCondition(version > 0, "version should be greater than zero");
        switch(fileType) {
            case "WAT": {
                ValidationUtils.validateAssertCondition(recordType != null && (recordType == WARCRecordTypes.INFO || recordType == WARCRecordTypes.METADATA) , "recordType should be WARCINFO or METADATA for WAT files");
                ValidationUtils.validateAssertCondition(contentType != null && (contentType == ContentType.APPLICATION_JSON || contentType == ContentType.APPLICATION_WARC_FIELDS) , "contentType should application/warc-fields or application/json for WAT files");
                ValidationUtils.validateAssertCondition(contentTypeContext == null, "contentTypeContext should be null for WAT files", contentTypeContext);
                break;
            }
            case "WARC": {
                ValidationUtils.validateAssertCondition(recordType != null && (recordType == WARCRecordTypes.INFO || recordType == WARCRecordTypes.REQUEST || recordType == WARCRecordTypes.RESPONSE || recordType == WARCRecordTypes.METADATA) , "recordType should be WARCINFO, REQUEST, RESPONSE or METADATA for WAT files");
                ValidationUtils.validateAssertCondition(contentType != null && (contentType == ContentType.APPLICATION_HTTP || contentType == ContentType.APPLICATION_WARC_FIELDS) , "contentType should application/warc-fields or application/http for WARC files");
                if (contentType == ContentType.APPLICATION_HTTP) {
                    ValidationUtils.validateAssertCondition(contentTypeContext != null, "contentTypeContext should not be null", contentTypeContext);
                    WARCRecordTypes contextRecordType = WARCRecordTypes.fromTextValue(contentTypeContext.split("=")[1].trim());
                    ValidationUtils.validateAssertCondition(contextRecordType == recordType, "contextRecordType should equal record type when content type is application / http for WARC files");
                } else {
                    ValidationUtils.validateAssertCondition(contentTypeContext == null, "contentTypeContext should be null when contentType is not application/http for WARC files", contentTypeContext);
                }
                break;
            }
            case "WET": {
                ValidationUtils.validateAssertCondition(recordType != null && (recordType == WARCRecordTypes.INFO || recordType == WARCRecordTypes.CONVERSION) , "recordType should be WARCINFO or CONVERSION for WET files");
                ValidationUtils.validateAssertCondition(contentType != null && (contentType == ContentType.TEXT_PLAIN || contentType == ContentType.APPLICATION_WARC_FIELDS) , "contentType should text/plain or application/json for WET files");
                ValidationUtils.validateAssertCondition(contentTypeContext == null, "contentTypeContext should be null for WET files", contentTypeContext);
                break;
            }
            default:
                logger.error("unknown common crawl document type {}"+fileType);
                throw new RuntimeException("unknown common crawl document type {}"+fileType);
        }

        ValidationUtils.validateAssertCondition(date != null, "date should not be null");
        StringFunctions.validateStringIsNotBlank(recordId, "recordId should not be empty");
        ValidationUtils.validateAssertCondition(contentLength > 0, "contentLength should be greater than zero");

        return new WarcHeaderFieldsParseResult(protocol, version, recordType, date, filename, recordId, contentType, contentLength, targetUri, refersTo, concurrentTo, blockDigest, language, infoId, ipAddress, payloadDigest, identifiedPayloadType, truncated);
    }

    public static AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> getExpectedRecordTypes(String recordType) {
        if (recordType == null) {
            return null;
        } else {
            StringFunctions.validateStringIsNotBlank(recordType, "recordType should not be null or empty");
            String[] components = recordType.split(",");
            ValidationUtils.validateAssertCondition(components.length == 2, "next expected record is invalid");
            WARCRecordTypes warcRecordTypes = WARCRecordTypes.fromTextValue(components[0].toLowerCase());
            DocumentRecordTypes documentRecordTypes = DocumentRecordTypes.valueOf(components[1].toUpperCase());
            return new AbstractMap.SimpleEntry<>(warcRecordTypes, documentRecordTypes);
        }
    }
}
