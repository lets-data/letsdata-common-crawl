package com.letsdata.commoncrawl.interfaces.implementations.parser;

import com.letsdata.commoncrawl.interfaces.implementations.documents.WARCError;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.CrawlDataRecordErrorException;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.*;
import com.letsdata.commoncrawl.model.filerecords.docs.warcinfo.WARCWarcInfoDoc;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.*;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import com.resonance.letsdata.data.readers.interfaces.parsers.SingleFileStateMachineParser;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import com.resonance.letsdata.data.readers.model.RecordHintType;
import com.resonance.letsdata.data.readers.model.RecordParseHint;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.ValidationUtils;
import com.letsdata.commoncrawl.interfaces.implementations.parser.WARCParserUtils.WARCFileRecordTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class WARCFileParser implements SingleFileStateMachineParser {
    private static String ParserFileType = CommonCrawlFileType.WARC.name();
    private static Logger logger = LoggerFactory.getLogger(WARCFileParser.class);

    public WARCFileParser() {

    }

    @Override
    public String getS3FileType() {
        return ParserFileType;
    }

    @Override
    public String getResolvedS3FileName(String fileType, String fileName) {
        ValidationUtils.validateAssertCondition(fileType != null && fileType.equalsIgnoreCase(ParserFileType), "WARCFileParser.getResolvedS3FileName file type is unexpected");
        return fileName;
    }

    @Override
    public RecordParseHint getNextRecordStartPattern(String s3FileType, String nextExpectedRecordType, DocumentInterface lastProcessedDoc) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WARCFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordTypes = WARCParserUtils.getExpectedRecordTypes(nextExpectedRecordType);
        WARCRecordTypes warcRecordTypes = nextRecordTypes.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordTypes.getValue();
        switch(warcRecordTypes)
        {
            case INFO: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.INFO.getRecordStartPhrase(), -1);
            }
            case REQUEST: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.REQUEST.getRecordStartPhrase(), -1);
            }
            case RESPONSE: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.RESPONSE.getRecordStartPhrase(), -1);
            }
            case METADATA: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.METADATA.getRecordStartPhrase(), -1);
            }
            default: {
                throw new RuntimeException("unexpected warc record type");
            }
        }
    }

    @Override
    public RecordParseHint getNextRecordEndPattern(String s3FileType, String nextExpectedRecordType, DocumentInterface lastProcessedDoc) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WARCFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordTypes = WARCParserUtils.getExpectedRecordTypes(nextExpectedRecordType);

        WARCRecordTypes warcRecordTypes = nextRecordTypes.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordTypes.getValue();
        switch(warcRecordTypes)
        {
            case INFO: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.INFO.getRecordEndPhrase(), -1);
            }
            case REQUEST: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.REQUEST.getRecordEndPhrase(), -1);
            }
            case RESPONSE: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.RESPONSE.getRecordEndPhrase(), -1);
            }
            case METADATA: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCFileRecordTypes.METADATA.getRecordEndPhrase(), -1);
            }
            default: {
                throw new RuntimeException("unexpected warc record type");
            }
        }
    }

    @Override
    public String getNextExpectedRecordType(String s3FileType, String lastProcessedRecordType) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WARCFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> lastRecordTypes = WARCParserUtils.getExpectedRecordTypes(lastProcessedRecordType);
        if (lastRecordTypes == null) {
            return WARCRecordTypes.INFO.toString()+","+DocumentRecordTypes.WARC_WARCINFO_PAYLOAD.name();
        } else {
            WARCRecordTypes warcRecordTypes = lastRecordTypes.getKey();
            DocumentRecordTypes documentRecordTypes = lastRecordTypes.getValue();
            if (warcRecordTypes == WARCRecordTypes.INFO) {
                ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_WARCINFO_PAYLOAD, "document record type should be WARC_WARCINFO_PAYLOAD");
                return WARCRecordTypes.REQUEST+","+DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD;
            } else if (warcRecordTypes == WARCRecordTypes.REQUEST) {
                ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD, "document record type should be WARC_HTTP_REQUEST_PAYLOAD");
                return WARCRecordTypes.RESPONSE+","+DocumentRecordTypes.WARC_HTTP_RESPONSE_PAYLOAD;
            } else if (warcRecordTypes == WARCRecordTypes.RESPONSE) {
                ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_HTTP_RESPONSE_PAYLOAD, "document record type should be WARC_HTTP_RESPONSE_PAYLOAD");
                return WARCRecordTypes.METADATA+","+DocumentRecordTypes.WARC_METADATA_PAYLOAD;
            } else if (warcRecordTypes == WARCRecordTypes.METADATA) {
                ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_METADATA_PAYLOAD, "document record type should be WARC_METADATA_PAYLOAD");
                return WARCRecordTypes.REQUEST+","+DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD;
            } else {
                logger.error("Unexpected lastProcessedRecordType - {}", warcRecordTypes.name());
                throw new RuntimeException("Unexpected lastProcessedRecordType "+warcRecordTypes.name());
            }
        }
    }

    @Override
    public ParseDocumentResult parseDocument(String s3FileType, String s3Filename, long offsetBytes, String lastProcessedRecordType, DocumentInterface lastProcessedDoc, byte[] byteArr, int startIndex, int endIndex) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WARCFileParser.parseDocument file type is unexpected");
        ValidationUtils.validateAssertCondition(byteArr != null && startIndex >= 0 && byteArr.length > endIndex && endIndex > startIndex, "WARCFileParser.parseDocument byte array offsets are invalid");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordType = WARCParserUtils.getExpectedRecordTypes(getNextExpectedRecordType(s3FileType, lastProcessedRecordType));
        WARCRecordTypes warcRecordTypes = nextRecordType.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordType.getValue();
        int headerEndIndex = Matcher.match(byteArr, startIndex, endIndex, WARCFileReaderConstants.WARC_RECORD_HEADER_END_PATTERN);
        ValidationUtils.validateAssertCondition(headerEndIndex >= 0 && headerEndIndex > startIndex && headerEndIndex < endIndex, "Header endIndex is less than 0 or less than startIndex", startIndex, headerEndIndex);

        WARCParserUtils.WarcHeaderFieldsParseResult headers = null;
        try {
            headers = WARCParserUtils.parseWarcHeaderFields(s3Filename, s3FileType, byteArr, startIndex, headerEndIndex);
            ValidationUtils.validateAssertCondition(headers != null, "headers parse result should not be null", headers);
        } catch (Exception e) {
            throw new RuntimeException("parseWarcHeaderFields threw an exception", e);
        }

        int recordStartIndex = headerEndIndex+WARCFileReaderConstants.WARC_RECORD_HEADER_END_PATTERN.getBytes(StandardCharsets.UTF_8).length;
        int recordEndIndex = recordStartIndex+headers.getContentLength();
        String errorNextRecordType = null;
        String errorNextDocType = null;
        try {
            switch (headers.getRecordType()) {
                case INFO: {
                    ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.INFO, "nextExpectedRecordType should be WARCINFO when processing WARC files", warcRecordTypes.name());
                    ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_WARCINFO_PAYLOAD, "nextExpectedDocumentType should be WARC_WARCINFO_PAYLOAD", documentRecordTypes.name());
                    ValidationUtils.validateAssertCondition(endIndex >= 0 && endIndex > startIndex, "WARCWarcInfoDoc endIndex is less than 0 or less than startIndex", startIndex, endIndex);
                    errorNextRecordType = WARCRecordTypes.REQUEST.toString();
                    errorNextDocType = DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD.name();
                    WARCWarcInfoDoc warcInfoDoc = null;
                    try {
                        warcInfoDoc = WARCWarcInfoDoc.getWARCWarcInfoDoc(byteArr, recordStartIndex, recordEndIndex);
                        WARCInfo warcInfo = new WARCInfo(CommonCrawlFileType.WARC, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), warcInfoDoc, headers.getFilename(), headers.getRecordId());
                        return new ParseDocumentResult(WARCRecordTypes.REQUEST.toString() + "," + DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD.name(), warcInfo, ParseDocumentResultStatus.SUCCESS);
                    } catch (Exception e) {
                        throw new RuntimeException("exception in getting warc info doc from warc file", e);
                    }
                }
                case REQUEST: {
                    ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.REQUEST, "nextExpectedRecordType should be REQUEST", warcRecordTypes);
                    ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD, "nextExpectedDocumentType should be WARC_HTTP_REQUEST_PAYLOAD", documentRecordTypes);
                    errorNextRecordType = WARCRecordTypes.RESPONSE.toString();
                    errorNextDocType = DocumentRecordTypes.WARC_HTTP_RESPONSE_PAYLOAD.name();
                    try {
                        WarcHttpRequestDoc warcHttpRequestDoc = WarcHttpRequestDoc.getWarcHttpRequestDocFromString(byteArr, recordStartIndex, recordEndIndex);
                        WARCRequest warcRequest = new WARCRequest(CommonCrawlFileType.WARC, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), warcHttpRequestDoc, headers.getRecordId(), headers.getInfoId(), headers.getIpAddress(), headers.getTargetUri());
                        return new ParseDocumentResult(WARCRecordTypes.RESPONSE.toString() + "," + DocumentRecordTypes.WARC_HTTP_RESPONSE_PAYLOAD.name(), warcRequest, ParseDocumentResultStatus.SUCCESS);
                    } catch (Exception e) {
                        throw new RuntimeException("exception in getting warc request doc from warc file", e);
                    }
                }
                case RESPONSE: {
                    ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.RESPONSE, "nextExpectedRecordType should be RESPONSE", warcRecordTypes);
                    ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_HTTP_RESPONSE_PAYLOAD, "nextExpectedDocumentType should be WARC_HTTP_RESPONSE_PAYLOAD", documentRecordTypes);
                    errorNextRecordType = WARCRecordTypes.METADATA.toString();
                    errorNextDocType = DocumentRecordTypes.WARC_METADATA_PAYLOAD.name();
                    DocumentInterface warcResponse;
                    WarcDoc warcHttpResponseDoc;
                    try {
                        warcHttpResponseDoc = WarcHttpResponseDoc.getWarcHttpResponseDocFromString(byteArr, recordStartIndex, recordEndIndex);
                        warcResponse = new WARCResponse(CommonCrawlFileType.WARC, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getContentType(), headers.getDate(), headers.getContentLength(), (WarcHttpResponseDoc) warcHttpResponseDoc, headers.getRecordId(), headers.getInfoId(), headers.getConcurrentTo(), headers.getIpAddress(), headers.getTargetUri(), headers.getPayloadDigest(), headers.getBlockDigest(), headers.getIdentifiedPayloadType(), headers.getTruncated());
                        return new ParseDocumentResult(WARCRecordTypes.METADATA.toString() + "," + DocumentRecordTypes.WARC_METADATA_PAYLOAD.name(), warcResponse, ParseDocumentResultStatus.SUCCESS);
                    } catch (CrawlDataRecordErrorException cde) {
                        Map<String, Long> offsetBytesStartMap = new HashMap<>();
                        offsetBytesStartMap.put(s3FileType, offsetBytes + startIndex);
                        Map<String, Long> offsetBytesEndMap = new HashMap<>();
                        offsetBytesEndMap.put(s3FileType, offsetBytes + endIndex);
                        warcHttpResponseDoc = new WarcErrorDoc(DocumentRecordTypes.WARC_ERROR, new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8), cde, offsetBytesStartMap, offsetBytesEndMap);
                        warcResponse = new WARCError(CommonCrawlFileType.WARC, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getRecordId(), headers.getTargetUri(), headers.getContentType(), headers.getContentLength(), (WarcErrorDoc) warcHttpResponseDoc);
                        return new ParseDocumentResult(WARCRecordTypes.METADATA.toString() + "," + DocumentRecordTypes.WARC_METADATA_PAYLOAD.name(), warcResponse, ParseDocumentResultStatus.ERROR);
                    } catch (Exception e) {
                        throw new RuntimeException("exception in getting warc response doc from warc file", e);
                    }
                }
                case METADATA: {
                    ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.METADATA, "nextExpectedRecordType should be METADATA", warcRecordTypes);
                    ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WARC_METADATA_PAYLOAD, "nextExpectedDocumentType should be WARC_METADATA_PAYLOAD", documentRecordTypes);
                    errorNextRecordType = WARCRecordTypes.REQUEST.toString();
                    errorNextDocType = DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD.name();
                    try {
                        String warcMetadataString = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
                        WarcMetadataDoc warcMetadataDoc = WarcMetadataDoc.getWarcMetadataDocFromString(warcMetadataString, 0, warcMetadataString.length());
                        WARCMetadata warcMetadata = new WARCMetadata(CommonCrawlFileType.WARC, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), warcMetadataDoc, headers.getTargetUri(), headers.getRecordId(), headers.getRefersTo(), headers.getConcurrentTo(), DocumentRecordTypes.WARC_METADATA_PAYLOAD, headers.getInfoId());
                        return new ParseDocumentResult(WARCRecordTypes.REQUEST.toString() + "," + DocumentRecordTypes.WARC_HTTP_REQUEST_PAYLOAD.name(), warcMetadata, ParseDocumentResultStatus.SUCCESS);
                    } catch (Exception e) {
                        throw new RuntimeException("exception in getting warc metadata doc from warc file", e);
                    }
                }
                default: {
                    logger.error("Unexpected record type in WET file - recordType: {}", headers.getRecordType());
                    throw new RuntimeException("Unexpected record type in WET file");
                }
            }
        } catch (Exception ex) {
            String warcJSON = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
            Map<String, Long> recordStartOffset = new HashMap<>();
            recordStartOffset.put(this.getS3FileType(), (long)(offsetBytes + startIndex));
            Map<String, Long> recordEndOffset = new HashMap<>();
            recordEndOffset.put(this.getS3FileType(), (long)(offsetBytes + endIndex));
            WarcErrorDoc errorDoc = new WarcErrorDoc(documentRecordTypes,"exception in reading WARC record from JSON: "+warcJSON, new CrawlDataRecordErrorException(ex), recordStartOffset, recordEndOffset);
            WARCError warcError = new WARCError(CommonCrawlFileType.WARC, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getRecordId(), headers.getTargetUri(), headers.getContentType(), headers.getContentLength(), errorDoc);
            return new ParseDocumentResult(errorNextRecordType+","+errorNextDocType, warcError, ParseDocumentResultStatus.ERROR);
        }
    }
}
