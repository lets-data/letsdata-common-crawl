package com.letsdata.commoncrawl.interfaces.implementations.reader;

import com.letsdata.commoncrawl.interfaces.implementations.documents.WARCError;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.LanguageStats;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.WetConversionDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.WatMetadataWarcResponseDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata.WarcMetadataPayloadMetadata;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.*;
import com.letsdata.commoncrawl.interfaces.implementations.documents.CompositeIndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.documents.IndexRecord;
import com.resonance.letsdata.data.documents.implementation.ErrorDoc;
import com.resonance.letsdata.data.documents.implementation.SkipDoc;
import com.resonance.letsdata.data.documents.interfaces.CompositeDocInterface;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import com.resonance.letsdata.data.documents.interfaces.ErrorDocInterface;
import com.resonance.letsdata.data.documents.interfaces.SingleDocInterface;
import com.resonance.letsdata.data.readers.interfaces.MultipleFileStateMachineReader;
import com.resonance.letsdata.data.readers.interfaces.SystemFileReader;
import com.resonance.letsdata.data.readers.interfaces.parsers.SingleFileStateMachineParser;
import com.resonance.letsdata.data.readers.model.ParseCompositeDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import com.resonance.letsdata.data.readers.model.SingleFileReaderState;
import com.resonance.letsdata.data.util.StringFunctions;
import com.resonance.letsdata.data.util.ValidationUtils;
import com.letsdata.commoncrawl.interfaces.implementations.parser.WARCFileParser;
import com.letsdata.commoncrawl.interfaces.implementations.parser.WATFileParser;
import com.letsdata.commoncrawl.interfaces.implementations.parser.WETFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommonCrawlReader implements MultipleFileStateMachineReader {
    private static final Set<String> CommonCrawlReaderExpectedFileTypes = new HashSet<>(Arrays.asList(CommonCrawlFileType.WARC.name(), CommonCrawlFileType.WAT.name(), CommonCrawlFileType.WET.name()));

    private static final Logger logger = LoggerFactory.getLogger(CommonCrawlReader.class);

    private final Map<String, String> resolvedFileNames;
    private final Map<String, SingleFileStateMachineParser> fileParserMap;

    public CommonCrawlReader() {
        resolvedFileNames = new HashMap<>();
        fileParserMap = new HashMap<>();
    }

    @Override
    public Set<String> getS3FileTypes() {
        return CommonCrawlReaderExpectedFileTypes;
    }

    @Override
    public Map<String, String> getResolvedS3FileNames(Map<String, String> fileTypeFileNameMap) {
        ValidationUtils.validateAssertCondition(fileTypeFileNameMap != null && fileTypeFileNameMap.size() == CommonCrawlReaderExpectedFileTypes.size(), "fileTypeFileNameMap size should equal expected");
        for (String fileType : fileTypeFileNameMap.keySet())
        {
            String fileTypeUpperCased = fileType.toUpperCase();
            ValidationUtils.validateAssertCondition(CommonCrawlReaderExpectedFileTypes.contains(fileTypeUpperCased), "fileType is unexpected");
            if (!resolvedFileNames.containsKey(fileTypeUpperCased)) {
                // no resolution being done, expect incoming file names to be resolved but custom logic can be put here
                resolvedFileNames.put(fileTypeUpperCased, fileTypeFileNameMap.get(fileType));
            }
        }

        return resolvedFileNames;
    }

    @Override
    public Map<String, SingleFileStateMachineParser> getParserInterfacesForS3FileTypes(Set<String> fileTypeSet) {
        ValidationUtils.validateAssertCondition(fileTypeSet != null && fileTypeSet.size() == CommonCrawlReaderExpectedFileTypes.size(), "fileTypeFileNameMap size should equal expected");
        for (String fileType : fileTypeSet)
        {
            String fileTypeUpperCased = fileType.toUpperCase();
            ValidationUtils.validateAssertCondition(CommonCrawlReaderExpectedFileTypes.contains(fileTypeUpperCased), "fileType is unexpected");
            if (!fileParserMap.containsKey(fileTypeUpperCased)) {
                switch (fileTypeUpperCased)
                {
                    case "WARC": {
                        fileParserMap.put(fileTypeUpperCased, new WARCFileParser());
                        break;
                    }
                    case "WAT": {
                        fileParserMap.put(fileTypeUpperCased, new WATFileParser());
                        break;
                    }
                    case "WET": {
                        fileParserMap.put(fileTypeUpperCased, new WETFileParser());
                        break;
                    }
                    default:{
                        throw new RuntimeException("unexpected");
                    }
                }
            }
        }

        return fileParserMap;
    }

    @Override
    public Map<String, String> getNextExpectedRecordType(Map<String, String> lastRecordTypeMap) {
        Map<String, String> nextExpectedRecordType = null;
        if(lastRecordTypeMap == null) {
            nextExpectedRecordType = new HashMap<>();
            for(String fileType : lastRecordTypeMap.keySet()){
                nextExpectedRecordType.put(fileType, fileParserMap.get(fileType).getNextExpectedRecordType(fileType, null));
            }
            return nextExpectedRecordType;
        } else {
            ValidationUtils.validateAssertCondition(lastRecordTypeMap != null && lastRecordTypeMap.size() == CommonCrawlReaderExpectedFileTypes.size(), "lastRecordTypeMap size should equal expected");
            nextExpectedRecordType = new HashMap<>();
            for(String fileType : lastRecordTypeMap.keySet()){
                nextExpectedRecordType.put(fileType, fileParserMap.get(fileType.toUpperCase()).getNextExpectedRecordType(fileType, lastRecordTypeMap.get(fileType)));
            }
            return nextExpectedRecordType;
        }
    }

    public Map<String, String> getLastExpectedRecordType(Map<String, SystemFileReader> fileTypeReaderMap) {
        Map<String, String> lastExpectedRecordType = new HashMap<>();
        for(String fileType : fileTypeReaderMap.keySet()){
            if (fileTypeReaderMap.get(fileType).getLastRecordType() == null) {
                lastExpectedRecordType.put(fileType, null);
            } else {
                lastExpectedRecordType.put(fileType, fileTypeReaderMap.get(fileType).getLastRecordType());
            }
        }
        return lastExpectedRecordType;
    }

    private void skipFileHeaders(Map<String, SystemFileReader> fileTypeReaderMap) throws Exception {
        Set<String> headerRecordTypes = new HashSet<>();
        headerRecordTypes.add(WARCRecordTypes.INFO.toString()+","+ DocumentRecordTypes.WARC_WARCINFO_PAYLOAD.toString());
        headerRecordTypes.add(WARCRecordTypes.INFO.toString()+","+ DocumentRecordTypes.WAT_WARCINFO_PAYLOAD.toString());
        headerRecordTypes.add(WARCRecordTypes.INFO.toString()+","+ DocumentRecordTypes.WET_WARCINFO_PAYLOAD.toString());
        headerRecordTypes.add(WARCRecordTypes.METADATA.toString()+","+ DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD.toString());

        for (SystemFileReader fileReader : fileTypeReaderMap.values()) {
            DocumentInterface document = fileReader.nextRecord(true);
            AbstractWARCRecord warcRecord = (document instanceof AbstractWARCRecord) ? (AbstractWARCRecord) document : null;
            while (warcRecord != null && headerRecordTypes.contains(warcRecord.getRecordType())) {
                fileReader.nextRecord(false);
                document = fileReader.nextRecord(true);
                warcRecord = (document instanceof AbstractWARCRecord) ? (AbstractWARCRecord) document : null;
            }
        }
    }

    private static <T> AbstractMap.SimpleEntry<T, ErrorDocInterface> getNextRecordFromFileReader(Class<T> tClass, SystemFileReader fileReader, boolean peek) {
        try {
            T warcRecord = null;
            ErrorDocInterface errorRecord = null;
            DocumentInterface document = fileReader.nextRecord(peek);
            if (document == null) {
                warcRecord = null;
                errorRecord = null;
            } else if (tClass.isAssignableFrom(document.getClass())) {
                warcRecord = (T) document;
                errorRecord = null;
            } else if (document instanceof ErrorDocInterface) {
                warcRecord = null;
                errorRecord = (ErrorDocInterface) document;
            } else {
                logger.error("Unknown document type read from fileReader - fileReader: {}, document: {}", fileReader.getFileName(), document);
                throw new RuntimeException("Unknown document type read from fileReader");
            }

            return new AbstractMap.SimpleEntry<T, ErrorDocInterface>(warcRecord, errorRecord);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception in reading next record from the file", ex);
            throw new RuntimeException("Exception in reading next record from the file", ex);
        }
    }

    private void validateTargetURI(DocumentInterface ... records) {
        String targetURI = null;
        for (DocumentInterface record : records) {
            if ((record instanceof AbstractWARCRecord)) {
                if (targetURI == null) {
                    targetURI = ((AbstractWARCRecord) record).getTargetUri();
                } else {
                    StringFunctions.validateStringsAreASCIIEqual(targetURI, ((AbstractWARCRecord) record).getTargetUri(), "metadata record from wat file should have the same target uri");
                }
            } else if ((record instanceof WARCError)) {
                if (targetURI == null) {
                    targetURI = ((WARCError) record).getTargetUri();
                } else {
                    StringFunctions.validateStringsAreASCIIEqual(targetURI, ((WARCError) record).getTargetUri(), "metadata record from wat file should have the same target uri");
                }
            } else {
                logger.error("Unknown record type in validateTargetURI - {}", record);
                throw new RuntimeException("Unknown record type");
            }
        }
    }


    @Override
    public ParseCompositeDocumentResult parseDocument(Map<String, String> fileNameFileTypeMap, DocumentInterface lastProcessedDocument, Map<String, SystemFileReader> fileTypeReaderMap) {
        ValidationUtils.validateAssertCondition(CommonCrawlReaderExpectedFileTypes.size() == fileNameFileTypeMap.size(), "fileTypeFileNameMpa size is unexpected");
        ValidationUtils.validateAssertCondition(CommonCrawlReaderExpectedFileTypes.size() == fileTypeReaderMap.size(), "fileTypeReaderMap size is unexpected");
        try {

            SystemFileReader warcReader = fileTypeReaderMap.get(CommonCrawlFileType.WARC.name());
            ValidationUtils.validateAssertCondition(warcReader != null, "warcReader is null");
            SystemFileReader watReader = fileTypeReaderMap.get(CommonCrawlFileType.WAT.name());
            ValidationUtils.validateAssertCondition(watReader != null, "watReader is null");
            SystemFileReader wetReader = fileTypeReaderMap.get(CommonCrawlFileType.WET.name());
            ValidationUtils.validateAssertCondition(wetReader != null, "wetReader is null");

            if (warcReader.getOffsetBytes() == 0) {
                ValidationUtils.validateAssertCondition(watReader.getOffsetBytes() == 0 && wetReader.getOffsetBytes() == 0, "wat and wet reader offset bytes should be > 0 when warc reader offset bytes are > 0");
                try {
                    skipFileHeaders(fileTypeReaderMap);
                } catch (Exception ex) {
                    logger.error("Error in skipping file headers in the file", ex);
                    throw new RuntimeException("Error in skipping file headers in the file", ex);
                }
            } else {
                ValidationUtils.validateAssertCondition(watReader.getOffsetBytes() > 0 && wetReader.getOffsetBytes()  > 0, "wat and wet reader offset bytes should be > 0 when warc reader offset bytes are > 0");
            }

            AbstractWARCRecord requestMetadata = null;
            ErrorDocInterface requestMetadataError = null;

            AbstractWARCRecord responseMetadata = null;
            ErrorDocInterface responseMetadataError = null;

            WARCMetadata documentMetadata = null;
            ErrorDocInterface documentMetadataError = null;

            WARCRequest request = null;
            ErrorDocInterface requestError = null;

            AbstractWARCRecord response = null;
            ErrorDocInterface responseError = null;

            WARCMetadata metadata = null;
            ErrorDocInterface metadataError = null;

            WARCConversion conversion = null;
            ErrorDocInterface conversionError = null;


            List<ErrorDocInterface> errorRecordsList = null;

            Map<String, Long> startOffsetMap = new HashMap<>();
            startOffsetMap.put(warcReader.getFileType(), warcReader.getOffsetBytes());
            startOffsetMap.put(watReader.getFileType(), watReader.getOffsetBytes());
            startOffsetMap.put(wetReader.getFileType(), wetReader.getOffsetBytes());

            AbstractMap.SimpleEntry<AbstractWARCRecord, ErrorDocInterface> watNextRecord = getNextRecordFromFileReader(AbstractWARCRecord.class, watReader, false);
            requestMetadata = watNextRecord.getKey();
            requestMetadataError = watNextRecord.getValue();
            if (requestMetadata == null && requestMetadataError == null) {
                logger.info("requestMetadata from WAT file is null - possible end of file: {}", watReader.getFileName());
                AbstractMap.SimpleEntry<WARCRequest, ErrorDocInterface> warcNextRecord = getNextRecordFromFileReader(WARCRequest.class, warcReader, false);
                request = warcNextRecord.getKey();
                requestError = warcNextRecord.getValue();
                AbstractMap.SimpleEntry<WARCConversion, ErrorDocInterface> wetNextRecord = getNextRecordFromFileReader(WARCConversion.class, wetReader, false);
                conversion = wetNextRecord.getKey();
                conversionError = wetNextRecord.getValue();
                if (request == null && requestError == null && conversion == null && conversionError == null) {
                    ValidationUtils.validateAssertCondition(warcReader.getState() == SingleFileReaderState.COMPLETED, "warcFileReader state should be completed when nextRecord is null", warcReader);
                    ValidationUtils.validateAssertCondition(watReader.getState() == SingleFileReaderState.COMPLETED, "watFileReader state should be completed when nextRecord is null", watReader);
                    ValidationUtils.validateAssertCondition(wetReader.getState() == SingleFileReaderState.COMPLETED, "wetFileReader state should be completed when nextRecord is null", wetReader);
                    Map<String, String> s3FileTypeNextRecordTypeMap = new HashMap<>();
                    s3FileTypeNextRecordTypeMap.put(CommonCrawlFileType.WARC.name(), null);
                    s3FileTypeNextRecordTypeMap.put(CommonCrawlFileType.WAT.name(), null);
                    s3FileTypeNextRecordTypeMap.put(CommonCrawlFileType.WET.name(), null);

                    Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
                    s3FileTypeOffsetMap.put(warcReader.getFileType(), warcReader.getOffsetBytes());
                    s3FileTypeOffsetMap.put(watReader.getFileType(), watReader.getOffsetBytes());
                    s3FileTypeOffsetMap.put(wetReader.getFileType(), wetReader.getOffsetBytes());

                    Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(fileTypeReaderMap);
                    return new ParseCompositeDocumentResult(null, null, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.COMPLETED, ParseDocumentResultStatus.SUCCESS);
                } else {
                    logger.error("requestMetadata is null but request {}, requestError {}, document {} and documentError {} records are not null", request, requestError, conversion, conversionError);
                    throw new RuntimeException("requestMetadata is null but request and document records are not null");
                }
            } else if (requestMetadata == null && requestMetadataError != null) {
                if (errorRecordsList == null) {
                    errorRecordsList = new ArrayList<>();
                }
                errorRecordsList.add((WARCError)requestMetadataError);
            } else if (requestMetadata != null && requestMetadataError != null) {
                logger.error("requestMetadata & requestMetadataError are both not null but requestMetadata {}, requestMetadataError {}", requestMetadata, requestMetadataError);
                throw new RuntimeException("requestMetadata & requestMetadataError are both not null");
            } else {
                ValidationUtils.validateAssertCondition(requestMetadata instanceof WARCMetadata, "requestMetadata record from WAT file should be of type WARCMetadata", request);
                ValidationUtils.validateAssertCondition(((WARCMetadata)requestMetadata).getDocumentRecordType() == DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD, "requestMetadata documentRecordType should be WAT_METADATA_WARC_REQUEST_PAYLOAD", ((WARCMetadata)requestMetadata).getDocumentRecordType());
            }

            watNextRecord = getNextRecordFromFileReader(AbstractWARCRecord.class, watReader, false);
            responseMetadata = watNextRecord.getKey();
            responseMetadataError = watNextRecord.getValue();

            if (responseMetadata == null && responseMetadataError != null) {
                if (errorRecordsList == null) {
                    errorRecordsList = new ArrayList<>();
                }
                errorRecordsList.add((WARCError)responseMetadataError);
            } else if (responseMetadata != null && responseMetadataError == null) {
                ValidationUtils.validateAssertCondition(responseMetadata instanceof WARCMetadata, "responseMetadata documentRecordType should be WARCMetadata", responseMetadata);
                ValidationUtils.validateAssertCondition(((WARCMetadata)responseMetadata).getDocumentRecordType() == DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD, "requestMetadata documentRecordType should be WAT_METADATA_WARC_RESPONSE_PAYLOAD", ((WARCMetadata)responseMetadata).getDocumentRecordType());
            } else {
                logger.error("responseMetadata & responseMetadataError are both not null or both null - responseMetadata {}, responseMetadataError {}", requestMetadata, requestMetadataError);
                throw new RuntimeException("responseMetadata & responseMetadataError are both not null or both null");
            }

            AbstractMap.SimpleEntry<WARCMetadata, ErrorDocInterface> metadataNextRecord = getNextRecordFromFileReader(WARCMetadata.class, watReader, false);
            documentMetadata = metadataNextRecord.getKey();
            documentMetadataError = metadataNextRecord.getValue();
            if (documentMetadata == null && documentMetadataError != null) {
                if (errorRecordsList == null) {
                    errorRecordsList = new ArrayList<>();
                }
                errorRecordsList.add((WARCError)documentMetadataError);
            } else if (documentMetadata != null && documentMetadataError == null) {
                ValidationUtils.validateAssertCondition(documentMetadata.getDocumentRecordType() == DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD, "requestMetadata documentRecordType should be WAT_METADATA_WARC_METADATA_PAYLOAD", documentMetadata.getDocumentRecordType());
            } else {
                logger.error("documentMetadata & documentMetadataError are both not null or both null - documentMetadata {}, documentMetadataError {}", documentMetadata, documentMetadataError);
                throw new RuntimeException("documentMetadata & documentMetadataError are both not null or both null");
            }

            validateTargetURI(requestMetadata == null ? requestMetadataError : requestMetadata, responseMetadata == null ? responseMetadataError : responseMetadata, documentMetadata == null ? documentMetadataError : documentMetadata);

            AbstractMap.SimpleEntry<WARCRequest, ErrorDocInterface> warcRequestRecord = getNextRecordFromFileReader(WARCRequest.class, warcReader, false);
            request = warcRequestRecord.getKey();
            requestError = warcRequestRecord.getValue();
            if (request == null && requestError != null) {
                if (errorRecordsList == null) {
                    errorRecordsList = new ArrayList<>();
                }
                errorRecordsList.add((WARCError)requestError);
            } else if (request != null && requestError == null) {

            } else {
                logger.error("request & requestError are both not null or both null - request {}, requestError {}", request, requestError);
                throw new RuntimeException("request & requestError are both not null or both null");
            }

            AbstractMap.SimpleEntry<AbstractWARCRecord, ErrorDocInterface> warcResponseRecord = getNextRecordFromFileReader(AbstractWARCRecord.class, warcReader, false);
            response = warcResponseRecord.getKey();
            responseError = warcResponseRecord.getValue();
            if (response == null && responseError != null) {
                if (errorRecordsList == null) {
                    errorRecordsList = new ArrayList<>();
                }
                errorRecordsList.add((WARCError)responseError);
            } else if (response != null && responseError == null) {

            } else {
                logger.error("response & responseError are both not null or both null - response {}, responseError {}", response, responseError);
                throw new RuntimeException("response & responseError are both not null or both null");
            }


            AbstractMap.SimpleEntry<WARCMetadata, ErrorDocInterface> warcMetadataRecord = getNextRecordFromFileReader(WARCMetadata.class, warcReader, false);
            metadata = warcMetadataRecord.getKey();
            metadataError = warcMetadataRecord.getValue();
            if (metadata == null && metadataError != null) {
                if (errorRecordsList == null) {
                    errorRecordsList = new ArrayList<>();
                }
                errorRecordsList.add((WARCError)metadataError);
            } else if (metadata != null && metadataError == null) {
                ValidationUtils.validateAssertCondition(metadata.getDocumentRecordType() == DocumentRecordTypes.WARC_METADATA_PAYLOAD, "metadata documentRecordType should be WARC_METADATA_PAYLOAD", metadata.getDocumentRecordType());
            } else {
                logger.error("metadata & metadataError are both not null or both null - metadata {}, metadataError {}", metadata, metadataError);
                throw new RuntimeException("metadata & metadataError are both not null or both null");
            }

            validateTargetURI(requestMetadata == null ? requestMetadataError : requestMetadata, request == null ? requestError : request);
            validateTargetURI( request == null ? requestError : request, response == null ? responseError : response, metadata == null ? metadataError : metadata);

            AbstractMap.SimpleEntry<WARCConversion, ErrorDocInterface> wetNextRecord = getNextRecordFromFileReader(WARCConversion.class, wetReader, true);
            conversion = wetNextRecord.getKey();
            conversionError = wetNextRecord.getValue();
            if (conversion == null && conversionError != null) {
                if (StringFunctions.equalsIgnoreCase(requestMetadata.getTargetUri(), ((WARCError)conversionError).getTargetUri())) {
                    if (errorRecordsList == null) {
                        errorRecordsList = new ArrayList<>();
                    }
                    errorRecordsList.add((WARCError) conversionError);
                    getNextRecordFromFileReader(WARCConversion.class, wetReader, false);
                }
            } else if (conversion != null && conversionError == null && StringFunctions.equalsIgnoreCase(requestMetadata.getTargetUri(), conversion.getTargetUri())) {
                getNextRecordFromFileReader(WARCConversion.class, wetReader, false);
            } else if (conversion == null && conversionError == null) {
                logger.warn("document record not found for targetUri {} - skipping record - fileName: {}", requestMetadata.getTargetUri(), warcReader.getFileName());
                Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(fileTypeReaderMap);
                Map<String, String> nextRecordType = getNextExpectedRecordType(lastProcessedRecordTypeMap);
                Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
                s3FileTypeOffsetMap.put(warcReader.getFileType(), warcReader.getOffsetBytes());
                s3FileTypeOffsetMap.put(watReader.getFileType(), watReader.getOffsetBytes());
                s3FileTypeOffsetMap.put(wetReader.getFileType(), wetReader.getOffsetBytes());

                String skipDocId = requestMetadata == null ? requestMetadataError == null ? UUID.randomUUID().toString() : requestMetadataError.getDocumentId() : requestMetadata.getDocumentId();
                String recordType = WARCRecordTypes.CONVERSION+","+DocumentRecordTypes.WET_CONVERSION_PAYLOAD;
                SkipDoc skipDoc = new SkipDoc(startOffsetMap, s3FileTypeOffsetMap, "conversion docs are null", skipDocId, recordType, null, null, skipDocId);
                Map.Entry<SingleDocInterface, List<ErrorDocInterface>> docMap = new AbstractMap.SimpleEntry<>(skipDoc, errorRecordsList);
                CompositeDocInterface compositeDoc = new CompositeIndexRecord(skipDocId, docMap);
                return new ParseCompositeDocumentResult(nextRecordType, compositeDoc, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.PROCESSING, ParseDocumentResultStatus.SKIP);
            }

            logger.debug("Processing record - filename:{}, targetUri: {}", warcReader.getFileName(), requestMetadata.getTargetUri());

            if (errorRecordsList != null && errorRecordsList.size() > 0) {
                Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(fileTypeReaderMap);
                Map<String, String> nextRecordType = getNextExpectedRecordType(lastProcessedRecordTypeMap);
                Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
                s3FileTypeOffsetMap.put(warcReader.getFileType(), warcReader.getOffsetBytes());
                s3FileTypeOffsetMap.put(watReader.getFileType(), watReader.getOffsetBytes());
                s3FileTypeOffsetMap.put(wetReader.getFileType(), wetReader.getOffsetBytes());

                String errorDocId = requestMetadata == null ? requestMetadataError == null ? UUID.randomUUID().toString() : requestMetadataError.getDocumentId() : requestMetadata.getDocumentId();
                String recordType = errorRecordsList.get(0).getRecordType();
                ErrorDoc errorDoc = new ErrorDoc(startOffsetMap, s3FileTypeOffsetMap, "errors in getting documents", errorDocId, recordType, null,  null, errorDocId);
                Map.Entry<SingleDocInterface, List<ErrorDocInterface>> docMap = new AbstractMap.SimpleEntry<>(errorDoc, errorRecordsList);
                CompositeDocInterface compositeDoc = new CompositeIndexRecord(errorDocId, docMap);
                return new ParseCompositeDocumentResult(nextRecordType, compositeDoc, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.PROCESSING, ParseDocumentResultStatus.ERROR);
            }

            WatMetadataWarcResponseDoc responseDoc = ((WatMetadataWarcResponseDoc)responseMetadata.getWarcDoc());
            WatMetadataWarcResponseDoc.HtmlHeadMetaValues headMetaValues = responseDoc.getHtmlHeadMetaValues();

            WarcMetadataDoc warcMetadataDoc = null;
            if (documentMetadata.getWarcDoc() != null && ((WatMetadataDoc) documentMetadata.getWarcDoc()).getEnvelope() != null && ((WatMetadataDoc) documentMetadata.getWarcDoc()).getEnvelope().getPayloadMetadata() != null) {
                warcMetadataDoc = ((WarcMetadataPayloadMetadata) ((WatMetadataDoc) documentMetadata.getWarcDoc()).getEnvelope().getPayloadMetadata()).getWarcMetadataDoc();
            }

            String charset = warcMetadataDoc == null || warcMetadataDoc.getDetectedCharset() == null ? null : warcMetadataDoc.getDetectedCharset().name();
            Boolean reliable = warcMetadataDoc == null ? null : warcMetadataDoc.getReliable();
            Long textBytes = warcMetadataDoc == null ? null : warcMetadataDoc.getTextBytes();
            List<LanguageStats> languageStats =  warcMetadataDoc == null ? null : warcMetadataDoc.getLanguages();

            IndexRecord indexRecord = new IndexRecord(requestMetadata.getTargetUri(), responseDoc.getTitle(), responseDoc.getDescription(), headMetaValues == null ? null : headMetaValues.getKeywords(), headMetaValues == null ? null : headMetaValues.getIcon(), headMetaValues == null ? null : headMetaValues.getCanonical(), headMetaValues == null ? null : headMetaValues.getOg_url(), headMetaValues == null ? null : headMetaValues.getOg_site_name(), headMetaValues == null ? null : headMetaValues.getOg_type(), headMetaValues == null ? null : headMetaValues.getOg_image(), headMetaValues == null ? null : headMetaValues.getOg_title(), headMetaValues == null ? null : headMetaValues.getOg_description(), headMetaValues == null ? null : headMetaValues.getContentType(), reliable, textBytes, languageStats, (long)conversion.getContentLength(), ((WetConversionDoc)conversion.getWarcDoc()).getDocText(), charset);
            CompositeDocInterface compositeDoc = new CompositeIndexRecord(indexRecord.getDocumentId(), new AbstractMap.SimpleEntry<>(indexRecord, errorRecordsList));

            Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(fileTypeReaderMap);
            Map<String, String> nextRecordType = getNextExpectedRecordType(lastProcessedRecordTypeMap);
            Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
            s3FileTypeOffsetMap.put(warcReader.getFileType(), warcReader.getOffsetBytes());
            s3FileTypeOffsetMap.put(watReader.getFileType(), watReader.getOffsetBytes());
            s3FileTypeOffsetMap.put(wetReader.getFileType(), wetReader.getOffsetBytes());

            return new ParseCompositeDocumentResult(nextRecordType, compositeDoc, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.PROCESSING, ParseDocumentResultStatus.SUCCESS);
        } finally {

        }
    }
}
