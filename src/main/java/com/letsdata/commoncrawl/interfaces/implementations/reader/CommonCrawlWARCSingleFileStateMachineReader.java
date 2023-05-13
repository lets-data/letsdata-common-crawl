package com.letsdata.commoncrawl.interfaces.implementations.reader;

import com.letsdata.commoncrawl.interfaces.implementations.documents.CompositeIndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.documents.WARCError;
import com.letsdata.commoncrawl.interfaces.implementations.parser.WARCFileParser;
import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.AbstractWARCRecord;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCMetadata;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCRequest;
import com.resonance.letsdata.data.documents.implementation.ErrorDoc;
import com.resonance.letsdata.data.documents.interfaces.CompositeDocInterface;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import com.resonance.letsdata.data.documents.interfaces.ErrorDocInterface;
import com.resonance.letsdata.data.documents.interfaces.SingleDocInterface;
import com.resonance.letsdata.data.readers.interfaces.SingleFileStateMachineReader;
import com.resonance.letsdata.data.readers.interfaces.SystemFileReader;
import com.resonance.letsdata.data.readers.interfaces.parsers.SingleFileStateMachineParser;
import com.resonance.letsdata.data.readers.model.ParseCompositeDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import com.resonance.letsdata.data.readers.model.SingleFileReaderState;
import com.resonance.letsdata.data.util.StringFunctions;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class CommonCrawlWARCSingleFileStateMachineReader implements SingleFileStateMachineReader {
        private static final String ExpectedFileType = CommonCrawlFileType.WARC.name();

        private static final Logger logger = LoggerFactory.getLogger(CommonCrawlReader.class);

        private final SingleFileStateMachineParser fileParser;

        public CommonCrawlWARCSingleFileStateMachineReader() {
            fileParser = new WARCFileParser();
        }

        @Override
        public String getS3FileType() {
            return ExpectedFileType;
        }

        @Override
        public String getResolvedS3FileName(String s3FileType, String fileName) {
            StringFunctions.validateStringsAreEqualIgnoreCase(s3FileType, ExpectedFileType, "expectedFileType");
            String resolvedFileName = fileName;
            return resolvedFileName;
        }

        @Override
        public SingleFileStateMachineParser getReaderParserInterfacesForS3FileType(String s3FileType) {
            StringFunctions.validateStringsAreEqualIgnoreCase(s3FileType, ExpectedFileType, "expectedFileType");
            return fileParser;
        }

        @Override
        public String getNextExpectedRecordType(String s3FileType, String lastProcessedRecordType) {
            StringFunctions.validateStringsAreEqualIgnoreCase(s3FileType, ExpectedFileType, "expectedFileType");
            String nextExpectedRecordType = fileParser.getNextExpectedRecordType(s3FileType, lastProcessedRecordType);
            return nextExpectedRecordType;
        }

        private void skipFileHeaders(SystemFileReader fileReader) throws Exception {
            Set<String> headerRecordTypes = new HashSet<>();
            headerRecordTypes.add(WARCRecordTypes.INFO.toString()+","+ DocumentRecordTypes.WARC_WARCINFO_PAYLOAD.toString());

            DocumentInterface document = fileReader.nextRecord(true);
            AbstractWARCRecord warcRecord = (document instanceof AbstractWARCRecord) ? (AbstractWARCRecord) document : null;
            while (warcRecord != null && headerRecordTypes.contains(warcRecord.getRecordType())) {
                fileReader.nextRecord(false);
                document = fileReader.nextRecord(true);
                warcRecord = (document instanceof AbstractWARCRecord) ? (AbstractWARCRecord) document : null;
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

        private Map<String, String> getLastExpectedRecordType(String s3FileType, SystemFileReader fileReader) {
            Map<String, String> lastExpectedRecordType = new HashMap<>();
            if (fileReader.getLastRecordType() == null) {
                lastExpectedRecordType.put(s3FileType, null);
            } else {
                lastExpectedRecordType.put(s3FileType, fileReader.getLastRecordType());
            }

            return lastExpectedRecordType;
        }

        @Override
        public ParseCompositeDocumentResult parseDocument(String s3FileType, String lastProcessedRecordType, DocumentInterface lastProcessedDoc, SystemFileReader fileReader) {
            StringFunctions.validateStringsAreEqualIgnoreCase(s3FileType, ExpectedFileType, "expectedFileType");
            ValidationUtils.validateAssertCondition(fileReader.getFileName() != null, "fileReader filename is unexpected");
            try {

                SystemFileReader warcReader = fileReader;
                ValidationUtils.validateAssertCondition(warcReader != null, "warcReader is null");

                if (warcReader.getOffsetBytes() == 0) {
                    try {
                        skipFileHeaders(warcReader);
                    } catch (Exception ex) {
                        logger.error("Error in skipping file headers in the file", ex);
                        throw new RuntimeException("Error in skipping file headers in the file", ex);
                    }
                }


                WARCRequest request = null;
                ErrorDocInterface requestError = null;

                AbstractWARCRecord response = null;
                ErrorDocInterface responseError = null;

                WARCMetadata metadata = null;
                ErrorDocInterface metadataError = null;

                List<ErrorDocInterface> errorRecordsList = null;

                AbstractMap.SimpleEntry<WARCRequest, ErrorDocInterface> warcNextRecord = getNextRecordFromFileReader(WARCRequest.class, warcReader, false);
                request = warcNextRecord.getKey();
                requestError = warcNextRecord.getValue();
                if (request == null && requestError == null) {
                    ValidationUtils.validateAssertCondition(warcReader.getState() == SingleFileReaderState.COMPLETED, "warcFileReader state should be completed when nextRecord is null", warcReader);
                    Map<String, String> s3FileTypeNextRecordTypeMap = new HashMap<>();
                    s3FileTypeNextRecordTypeMap.put(CommonCrawlFileType.WARC.name(), null);

                    Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
                    s3FileTypeOffsetMap.put(CommonCrawlFileType.WARC.name(), warcReader.getOffsetBytes());

                    Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(s3FileType, fileReader);
                    return new ParseCompositeDocumentResult(null, null, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.COMPLETED, ParseDocumentResultStatus.SUCCESS);
                }
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


                if (errorRecordsList != null && errorRecordsList.size() > 0) {
                    Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(s3FileType, fileReader);
                    String nextRecordType = getNextExpectedRecordType(s3FileType, lastProcessedRecordTypeMap.get(s3FileType));
                    Map<String, String> nextRecordTypeMap = new HashMap<>();
                    nextRecordTypeMap.put(s3FileType, nextRecordType);

                    Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
                    s3FileTypeOffsetMap.put(s3FileType, warcReader.getOffsetBytes());

                    String errorDocId = request == null ? requestError == null ? UUID.randomUUID().toString() : requestError.getDocumentId() : request.getDocumentId();
                    ErrorDoc errorDoc = new ErrorDoc(s3FileTypeOffsetMap, s3FileTypeOffsetMap, "errors in getting documents", errorDocId, errorRecordsList.get(0).getRecordType(), null,  null, errorDocId);;
                    Map.Entry<SingleDocInterface, List<ErrorDocInterface>> docMap = new AbstractMap.SimpleEntry<>(errorDoc, errorRecordsList);
                    CompositeDocInterface compositeDoc = new CompositeIndexRecord(errorDocId, docMap);
                    return new ParseCompositeDocumentResult(nextRecordTypeMap, compositeDoc, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.PROCESSING, ParseDocumentResultStatus.ERROR);
                }

                CompositeDocInterface compositeDoc = new CompositeIndexRecord(response.getDocumentId(), new AbstractMap.SimpleEntry<>(response, errorRecordsList));

                Map<String, String> lastProcessedRecordTypeMap = getLastExpectedRecordType(s3FileType, fileReader);
                String nextRecordType = getNextExpectedRecordType(s3FileType, lastProcessedRecordTypeMap.get(s3FileType));
                Map<String, String> nextRecordTypeMap = new HashMap<>();
                nextRecordTypeMap.put(s3FileType, nextRecordType);

                Map<String, Long> s3FileTypeOffsetMap = new HashMap<>();
                s3FileTypeOffsetMap.put(s3FileType, warcReader.getOffsetBytes());

                return new ParseCompositeDocumentResult(nextRecordTypeMap, compositeDoc, lastProcessedRecordTypeMap, s3FileTypeOffsetMap, SingleFileReaderState.PROCESSING, ParseDocumentResultStatus.SUCCESS);
            } finally {

            }
        }
    }
