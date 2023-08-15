package com.letsdata.commoncrawl.interfaces.implementations.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.CrawlDataRecordErrorException;
import com.letsdata.commoncrawl.model.WARCFileReaderConstants;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcErrorDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.WetConversionDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.warcinfo.WETWarcInfoDoc;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCConversion;
import com.letsdata.commoncrawl.interfaces.implementations.documents.WARCError;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCInfo;
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

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class WETFileParser implements SingleFileStateMachineParser {
    private static String ParserFileType = CommonCrawlFileType.WET.name();
    private static Logger logger = LoggerFactory.getLogger(WARCFileParser.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public WETFileParser() {

    }

    @Override
    public String getS3FileType() {
        return ParserFileType;
    }

    @Override
    public String getResolvedS3FileName(String fileType, String fileName) {
        ValidationUtils.validateAssertCondition(fileType != null && fileType.equalsIgnoreCase(ParserFileType), "WETFileParser.getResolvedS3FileName file type is unexpected");
        return fileName;
    }
    
    @Override
    public RecordParseHint getNextRecordStartPattern(String s3FileType, String nextExpectedRecordType, DocumentInterface lastProcessedDoc) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WETFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordTypes = WARCParserUtils.getExpectedRecordTypes(nextExpectedRecordType);
        WARCRecordTypes warcRecordTypes = nextRecordTypes.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordTypes.getValue();
        switch(warcRecordTypes)
        {
            case INFO: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WETFileRecordTypes.INFO.getRecordStartPhrase(), -1);
            }
            case CONVERSION: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WETFileRecordTypes.CONVERSION.getRecordStartPhrase(), -1);
            }
            default: {
                throw new RuntimeException("unexpected warc record type");
            }
        }
    }

    @Override
    public RecordParseHint getNextRecordEndPattern(String s3FileType, String nextExpectedRecordType, DocumentInterface lastProcessedDoc) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WETFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> nextRecordTypes = WARCParserUtils.getExpectedRecordTypes(nextExpectedRecordType);
        WARCRecordTypes warcRecordTypes = nextRecordTypes.getKey();
        DocumentRecordTypes documentRecordTypes = nextRecordTypes.getValue();
        switch(warcRecordTypes)
        {
            case INFO: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WETFileRecordTypes.CONVERSION.getRecordEndPhrase(), -1);
            }
            case CONVERSION: {
                return new RecordParseHint(RecordHintType.PATTERN, WARCParserUtils.WETFileRecordTypes.CONVERSION.getRecordEndPhrase(), -1);
            }
            default: {
                throw new RuntimeException("unexpected warc record type");
            }
        }
    }

    @Override
    public String getNextExpectedRecordType(String s3FileType, String lastProcessedRecordType) {
        ValidationUtils.validateAssertCondition(s3FileType != null && s3FileType.equalsIgnoreCase(ParserFileType), "WETFileParser.getNextRecordStartPattern file type is unexpected");
        AbstractMap.SimpleEntry<WARCRecordTypes, DocumentRecordTypes> lastRecordTypes = WARCParserUtils.getExpectedRecordTypes(lastProcessedRecordType);
        if (lastRecordTypes == null) {
            return WARCRecordTypes.INFO.toString()+","+DocumentRecordTypes.WET_WARCINFO_PAYLOAD.name();
        } else {
            WARCRecordTypes warcRecordTypes = lastRecordTypes.getKey();
            DocumentRecordTypes documentRecordTypes = lastRecordTypes.getValue();
            ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.INFO || warcRecordTypes ==  WARCRecordTypes.CONVERSION, "lastProcessedRecordType should be INFO or CONVERSION for WET files", lastProcessedRecordType);
            return WARCRecordTypes.CONVERSION+","+DocumentRecordTypes.WET_CONVERSION_PAYLOAD;
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
        String errorNextRecordType = null;
        String errorNextDocType = null;
        try {
            switch (headers.getRecordType()) {
                case INFO: {
                    ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.INFO, "nextExpectedRecordType should be WARCINFO", warcRecordTypes);
                    ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WET_WARCINFO_PAYLOAD, "nextExpectedDocumentType should be WET_WARCINFO_PAYLOAD", documentRecordTypes);
                    errorNextRecordType = WARCRecordTypes.CONVERSION.toString();
                    errorNextDocType = DocumentRecordTypes.WET_CONVERSION_PAYLOAD.name();
                    WETWarcInfoDoc warcInfoDoc = null;
                    try {
                        warcInfoDoc = WETWarcInfoDoc.getWETWarcInfoDoc(byteArr, recordStartIndex, recordEndIndex);
                        WARCInfo warcInfo = new WARCInfo(CommonCrawlFileType.WET, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getContentType(), headers.getContentLength(), warcInfoDoc, headers.getFilename(), headers.getRecordId());
                        return new ParseDocumentResult(WARCRecordTypes.CONVERSION.toString() + "," + DocumentRecordTypes.WET_CONVERSION_PAYLOAD.name(), warcInfo, ParseDocumentResultStatus.SUCCESS);
                    } catch (Exception e) {
                        throw new RuntimeException("Exception in parseDocument", e);
                    }
                }
                case CONVERSION: {
                    ValidationUtils.validateAssertCondition(warcRecordTypes == WARCRecordTypes.CONVERSION, "nextExpectedRecordType should be CONVERSION", warcRecordTypes);
                    ValidationUtils.validateAssertCondition(documentRecordTypes == DocumentRecordTypes.WET_CONVERSION_PAYLOAD, "nextExpectedDocumentType should be WET_CONVERSION_PAYLOAD", documentRecordTypes);
                    errorNextRecordType = WARCRecordTypes.CONVERSION.toString();
                    errorNextDocType = DocumentRecordTypes.WET_CONVERSION_PAYLOAD.name();
                    String wetConversionDocString = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
                    WARCConversion warcConversion = new WARCConversion(CommonCrawlFileType.WET, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getContentType(), headers.getContentLength(), new WetConversionDoc(wetConversionDocString), headers.getTargetUri(), headers.getDate(), headers.getRecordId(), headers.getRefersTo(), headers.getBlockDigest(), headers.getLanguage());
                    return new ParseDocumentResult(WARCRecordTypes.CONVERSION.toString() + "," + DocumentRecordTypes.WET_CONVERSION_PAYLOAD.name(), warcConversion, ParseDocumentResultStatus.SUCCESS);
                }
                default: {
                    logger.error("Unexpected record type in WET file - recordType: {}", headers.getRecordType());
                    throw new RuntimeException("Unexpected record type in WET file");
                }
            }
        } catch (Exception ex) {
            String warcJSON = new String(byteArr, recordStartIndex, recordEndIndex - recordStartIndex, StandardCharsets.UTF_8);
            Map<String, String> recordStartOffset = new HashMap<>();
            recordStartOffset.put(this.getS3FileType(), Long.toString((offsetBytes + startIndex)));
            Map<String, String> recordEndOffset = new HashMap<>();
            recordEndOffset.put(this.getS3FileType(), Long.toString((offsetBytes + endIndex)));
            WarcErrorDoc errorDoc = new WarcErrorDoc(documentRecordTypes,"exception in reading WET record from JSON: "+warcJSON, new CrawlDataRecordErrorException(ex), recordStartOffset, recordEndOffset);
            WARCError warcError = new WARCError(CommonCrawlFileType.WET, headers.getProtocol(), headers.getVersion(), headers.getRecordType(), headers.getDate(), headers.getRecordId(), headers.getTargetUri(), headers.getContentType(), headers.getContentLength(), errorDoc);
            return new ParseDocumentResult(errorNextRecordType+","+errorNextDocType, warcError, ParseDocumentResultStatus.ERROR);
        }
    }
}
