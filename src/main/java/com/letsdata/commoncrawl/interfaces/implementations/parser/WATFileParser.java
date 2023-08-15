package com.letsdata.commoncrawl.interfaces.implementations.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.CrawlDataRecordErrorException;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcErrorDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.warcinfo.WATWarcInfoDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataWarcInfoDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataWarcMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataWarcRequestDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataWarcResponseDoc;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.interfaces.implementations.documents.WARCError;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCInfo;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCMetadata;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import com.resonance.letsdata.data.readers.interfaces.parsers.SingleFileStateMachineParser;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import com.resonance.letsdata.data.readers.model.RecordHintType;
import com.resonance.letsdata.data.readers.model.RecordParseHint;
import com.resonance.letsdata.data.util.Matcher;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class WATFileParser implements SingleFileStateMachineParser {
    private static String ParserFileType = CommonCrawlFileType.WAT.name();
    private static Logger logger = LoggerFactory.getLogger(WARCFileParser.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public WATFileParser() {

    }

    @Override
    public String getS3FileType() {
        return ParserFileType;
    }

    @Override
    public String getResolvedS3FileName(String fileType, String fileName) {
        ValidationUtils.validateAssertCondition(fileType != null && fileType.equalsIgnoreCase(ParserFileType), "WATFileParser.getResolvedS3FileName file type is unexpected");
        return fileName;
    }

    @Override
    public RecordParseHint getNextRecordStartPattern(String s3FileType, String nextExpectedRecordType, DocumentInterface lastProcessedDoc) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WATFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordTypes = WARCParserUtils.getExpectedRecordTypes(nextExpectedRecordType);
        WARCRecordTypes warcRecordTypes = nextRecordTypes.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordTypes.getValue();
        switch(warcRecordTypes)
        {
            case INFO: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WATFileRecordTypes.INFO.getRecordStartPhrase(), -1);
            }
            case METADATA: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WATFileRecordTypes.METADATA.getRecordStartPhrase(), -1);
            }
            default: {
                throw new RuntimeException("unexpected warc record type");
            }
        }
    }

    @Override
    public RecordParseHint getNextRecordEndPattern(String s3FileType, String nextExpectedRecordType, DocumentInterface lastProcessedDoc) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WATFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordTypes = WARCParserUtils.getExpectedRecordTypes(nextExpectedRecordType);
        WARCRecordTypes warcRecordTypes = nextRecordTypes.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordTypes.getValue();
        switch (warcRecordTypes) {
            case INFO: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WATFileRecordTypes.INFO.getRecordEndPhrase(), -1);
            }
            case METADATA: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WATFileRecordTypes.METADATA.getRecordEndPhrase(), -1);
            }
            default: {
                throw new RuntimeException("unexpected warc record type");
            }
        }
    }

    @Override
    public String getNextExpectedRecordType(String s3FileType, String lastProcessedRecordType) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WATFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> lastRecordTypes = WARCParserUtils.getExpectedRecordTypes(lastProcessedRecordType);
        if (lastRecordTypes == null) {
            return WARCRecordTypes.INFO.toString()+","+DocumentRecordTypes.WAT_WARCINFO_PAYLOAD.name();
        } else {
            WARCRecordTypes warcRecordTypes = lastRecordTypes.getKey();
            DocumentRecordTypes documentRecordTypes = lastRecordTypes.getValue();
            if (warcRecordTypes == WARCRecordTypes.INFO) {
                return WARCRecordTypes.METADATA.toString()+","+DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD.name();
            } else if (warcRecordTypes == WARCRecordTypes.METADATA) {
                if (documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD || documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD) {
                    return WARCRecordTypes.METADATA.toString()+","+DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD.name();
                } else if (documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD) {
                    return WARCRecordTypes.METADATA.toString()+","+DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD.name();
                } else if (documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD) {
                    return WARCRecordTypes.METADATA.toString()+","+DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD.name();
                } else {
                    logger.error("Unexpected lastProcessedDocumentType for WAT files - {}", warcRecordTypes.name());
                    throw new RuntimeException("Unexpected lastProcessedDocumentType for WAT files"+documentRecordTypes.name());
                }
            } else {
                logger.error("Unexpected lastProcessedRecordType for WAT files- {}", warcRecordTypes.name());
                throw new RuntimeException("Unexpected lastProcessedRecordType for WAT files"+warcRecordTypes.name());
            }
        }
    }

    @Override
    public ParseDocumentResult parseDocument(String s3FileType, String s3Filename, long offsetBytes, String lastProcessedRecordType, DocumentInterface lastProcessedDoc, byte[] byteArr, int startIndex, int endIndex) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WATFileParser.parseDocument file type is unexpected");
        ValidationUtils.validateAssertCondition(byteArr != null && startIndex >= 0 && byteArr.length > endIndex && endIndex > startIndex, "WATFileParser.parseDocument byte array offsets are invalid");
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
        ValidationUtils.validateAssertCondition(recordEndIndex >= 0 && recordEndIndex > recordStartIndex && recordEndIndex < byteArr.length, "WATWarcInfoDoc endIndex is less than 0 or less than startIndex", startIndex, endIndex);

        switch (headers.getRecordType()) {
            case INFO: {
                ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.INFO, "nextExpectedRecordType should be WARCINFO", warcRecordTypes);
                ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WAT_WARCINFO_PAYLOAD, "nextExpectedDocumentType should be WAT_WARCINFO_PAYLOAD", documentRecordTypes);
                WATWarcInfoDoc warcInfoDoc = null;
                try {
                    warcInfoDoc = WATWarcInfoDoc.getWATWarcInfoDoc(byteArr, recordStartIndex, recordEndIndex);
                    WARCInfo warcInfo = new WARCInfo(CommonCrawlFileType.WAT, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), warcInfoDoc, headers.getFilename(), headers.getRecordId());
                    return new ParseDocumentResult(WARCRecordTypes.METADATA.toString()+","+DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD.name(), warcInfo, ParseDocumentResultStatus.SUCCESS);
                } catch (Exception e) {
                    throw new RuntimeException("exception in getting warc info doc from wat file", e);
                }
            }
            case METADATA: {
                ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.METADATA, "nextExpectedRecordType should be METADATA", warcRecordTypes);
                String errorNextRecordType = null;
                try {
                    switch (documentRecordTypes) {
                        case WAT_METADATA_WARC_WARCINFO_PAYLOAD:
                            ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD, "nextExpectedDocumentType should be WAT_METADATA_WARC_WARCINFO_PAYLOAD", documentRecordTypes);
                            errorNextRecordType = DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD.name();
                            WatMetadataWarcInfoDoc watWarcInfoDoc;
                            try {
                                watWarcInfoDoc = objectMapper.readValue(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, WatMetadataWarcInfoDoc.class);
                            } catch (IOException ex) {
                                String warcJSON = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
                                logger.error("exception in reading WATWARCInfo from JSON: " + warcJSON, ex);
                                throw new RuntimeException("exception in reading WATWARCInfo from JSON", ex);
                            }
                            WARCMetadata WATMetadataWARCInfo = new WARCMetadata(CommonCrawlFileType.WAT, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), watWarcInfoDoc, headers.getTargetUri(), headers.getRecordId(), headers.getRefersTo(), headers.getConcurrentTo(), DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD, headers.getInfoId());
                            return new ParseDocumentResult(WARCRecordTypes.METADATA.toString() + "," + DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD.name(), WATMetadataWARCInfo, ParseDocumentResultStatus.SUCCESS);
                        case WAT_METADATA_WARC_REQUEST_PAYLOAD:
                            ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD, "nextExpectedDocumentType should be WAT_METADATA_WARC_REQUEST_PAYLOAD", documentRecordTypes);
                            errorNextRecordType = DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD.name();
                            WatMetadataWarcRequestDoc watRequestDoc;
                            try {
                                watRequestDoc = objectMapper.readValue(byteArr, recordStartIndex, endIndex - recordStartIndex, WatMetadataWarcRequestDoc.class);
                            } catch (IOException ex) {
                                String warcJSON = new String(byteArr, startIndex, recordEndIndex - startIndex, StandardCharsets.UTF_8);
                                logger.error("exception in reading WatMetadataWarcRequestDoc from JSON: " + warcJSON, ex);
                                throw new RuntimeException("exception in reading WatMetadataWarcRequestDoc from JSON", ex);
                            }

                            WARCMetadata warcRequestMetadata = new WARCMetadata(CommonCrawlFileType.WAT, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), watRequestDoc, headers.getTargetUri(), headers.getRecordId(), headers.getRefersTo(), headers.getConcurrentTo(), DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD, headers.getInfoId());
                            return new ParseDocumentResult(WARCRecordTypes.METADATA.toString() + "," + DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD.name(), warcRequestMetadata, ParseDocumentResultStatus.SUCCESS);
                        case WAT_METADATA_WARC_RESPONSE_PAYLOAD:
                            ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD, "nextExpectedDocumentType should be WAT_METADATA_WARC_RESPONSE_PAYLOAD", documentRecordTypes);
                            errorNextRecordType = DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD.name();
                            WatMetadataWarcResponseDoc watResponseDoc;
                            try {
                                watResponseDoc = objectMapper.readValue(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, WatMetadataWarcResponseDoc.class);
                            } catch (IOException ex) {
                                String warcJSON = new String(byteArr, startIndex, recordEndIndex - startIndex, StandardCharsets.UTF_8);
                                logger.error("exception in reading WatMetadataWarcResponseDoc from JSON: " + warcJSON, ex);
                                throw new RuntimeException("exception in reading WatMetadataWarcResponseDoc from JSON", ex);
                            }

                            WARCMetadata warcResponseMetadata = new WARCMetadata(CommonCrawlFileType.WAT, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), watResponseDoc, headers.getTargetUri(), headers.getRecordId(), headers.getRefersTo(), headers.getConcurrentTo(), DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD, headers.getInfoId());
                            return new ParseDocumentResult(WARCRecordTypes.METADATA.toString() + "," + DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD.name(), warcResponseMetadata, ParseDocumentResultStatus.SUCCESS);
                        case WAT_METADATA_WARC_METADATA_PAYLOAD:
                            ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD, "nextExpectedDocumentType should be WAT_METADATA_WARC_METADATA_PAYLOAD", documentRecordTypes);
                            errorNextRecordType = DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD.name();
                            WatMetadataWarcMetadataDoc watMetadataDoc;
                            try {
                                watMetadataDoc = objectMapper.readValue(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, WatMetadataWarcMetadataDoc.class);
                            } catch (IOException ex) {
                                String warcJSON = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
                                logger.error("exception in reading WatMetadataWarcMetadataDoc from JSON: " + warcJSON, ex);
                                throw new RuntimeException("exception in reading WatMetadataWarcMetadataDoc from JSON", ex);
                            }

                            WARCMetadata warcMetadataMetadata = new WARCMetadata(CommonCrawlFileType.WAT, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), watMetadataDoc, headers.getTargetUri(), headers.getRecordId(), headers.getRefersTo(), headers.getConcurrentTo(), DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD, headers.getInfoId());
                            return new ParseDocumentResult(WARCRecordTypes.METADATA.toString() + "," + DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD.name(), warcMetadataMetadata, ParseDocumentResultStatus.SUCCESS);
                        default: {
                            logger.error("Unexpected next expected record type in WAT file - recordType: {}", documentRecordTypes);
                            throw new RuntimeException("Unexpected next expected record type in WAT file");
                        }
                    }
                } catch (Exception ex) {
                    String warcJSON = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
                    Map<String, String> recordStartOffset = new HashMap<>();
                    recordStartOffset.put(this.getS3FileType(), Long.toString((offsetBytes + startIndex)));
                    Map<String, String> recordEndOffset = new HashMap<>();
                    recordEndOffset.put(this.getS3FileType(), Long.toString((offsetBytes + endIndex)));
                    WarcErrorDoc errorDoc = new WarcErrorDoc(documentRecordTypes,"exception in reading WAT record from JSON: "+warcJSON, new CrawlDataRecordErrorException(ex), recordStartOffset, recordEndOffset);
                    WARCError warcError = new WARCError(CommonCrawlFileType.WAT, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getRecordId(), headers.getTargetUri(), headers.getContentType(), headers.getContentLength(), errorDoc);
                    return new ParseDocumentResult(WARCRecordTypes.METADATA.toString()+","+errorNextRecordType, warcError, ParseDocumentResultStatus.ERROR);
                }
            }
            default: {
                logger.error("Unexpected record type in WAT file - recordType: {}", headers.getRecordType());
                throw new RuntimeException("Unexpected record type in WAT file");
            }
        }
    }
}
