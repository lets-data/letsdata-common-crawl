package com.letsdata.commoncrawl.interfaces.implementations.kinesisstreamreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.interfaces.implementations.documents.IndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.sqsqueuereader.CommonCrawlQueueReader;
import com.resonance.letsdata.data.documents.implementation.ErrorDoc;
import com.resonance.letsdata.data.readers.interfaces.kinesis.KinesisRecordReader;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import com.resonance.letsdata.data.util.ValidationUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonCrawlStreamReader implements KinesisRecordReader {
    private static final Logger logger = LoggerFactory.getLogger(CommonCrawlStreamReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ParseDocumentResult parseMessage(String streamArn, String shardId, String partitionKey, String sequenceNumber, Date approximateArrivalTimestamp, ByteBuffer data) {
        if (data == null || data.array() == null || data.array().length <= 0) {
            logger.error("record data is null or empty, returning error - streamArn: {}, shardId: {}, partitionKey: {} , sequenceNumber: {}, approximateArrivalTimestamp: {}, data: {}", streamArn, shardId, partitionKey, sequenceNumber, approximateArrivalTimestamp, data);
            Map<String, String> offsetMap = createOffsetMap(shardId, sequenceNumber);
            ErrorDoc errorDoc = new ErrorDoc(null, null, "empty record data - sequenceNumber: "+sequenceNumber+", shardId: "+shardId, sequenceNumber, null, null, "empty message body", partitionKey);
            return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
        }

        try {
            logger.debug("processing record - sequenceNumber: {}", sequenceNumber);
            IndexRecord documentInterface = objectMapper.readValue(data.array(), IndexRecord.class);
            logger.debug("returning success - docId: {}", documentInterface.getDocumentId());
            return new ParseDocumentResult(null, documentInterface, ParseDocumentResultStatus.SUCCESS);
        } catch (IOException ex) {
            logger.error("IOException in reading the document - streamArn: {}, shardId: {}, partitionKey: {} , sequenceNumber: {}, approximateArrivalTimestamp: {}, data: {}, ex: {}", streamArn, shardId, partitionKey, sequenceNumber, approximateArrivalTimestamp, data, ex);
            // Map<String, Long> offsetMap = createOffsetMap(shardId, sequenceNumber);
            ErrorDoc errorDoc = new ErrorDoc(null, null, "IOException - " + ex.getMessage(), sequenceNumber, null, null, "JsonProcessingException - " + ex.getMessage(), partitionKey);
            return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
        }
    }

    private Map<String, String> createOffsetMap(String shardId, String sequenceNumber) {
        ValidationUtils.validateAssertCondition(shardId != null, "shardId should not be null");
        ValidationUtils.validateAssertCondition(sequenceNumber != null, "sequenceNumber should not be null");
        Map<String, String> offsetMap = new HashMap<>();
        offsetMap.put(shardId, sequenceNumber);
        return offsetMap;
    }
}
