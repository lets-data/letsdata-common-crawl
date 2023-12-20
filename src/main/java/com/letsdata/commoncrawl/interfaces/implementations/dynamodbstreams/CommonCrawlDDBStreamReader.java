package com.letsdata.commoncrawl.interfaces.implementations.dynamodbstreams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.interfaces.implementations.documents.IndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.kinesisstreamreader.CommonCrawlStreamReader;
import com.resonance.letsdata.data.documents.implementation.ErrorDoc;
import com.resonance.letsdata.data.documents.implementation.SkipDoc;
import com.resonance.letsdata.data.readers.interfaces.dynamodbstreams.DynamoDBStreamsRecordReader;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonCrawlDDBStreamReader implements DynamoDBStreamsRecordReader {
    private static final Logger logger = LoggerFactory.getLogger(CommonCrawlStreamReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ParseDocumentResult parseRecord(String streamArn, String shardId, String eventId, String eventName, String identityPrincipalId, String identityType, String sequenceNumber, Long sizeBytes, String streamViewType, Date approximateCreationDateTime, Map<String, Object> keys, Map<String, Object> oldImage, Map<String, Object> newImage) {
        try {
            String docId = null;
            for (Object keyValue : keys.values()) {
                if (docId == null) {
                    docId = keyValue.toString();
                } else {
                    docId += ('|' + keyValue.toString());
                }
            }

            if (newImage == null || newImage.size() <= 0) {
                logger.error("newImage is null, returning skip - streamArn: " + streamArn + ", shardId: " +
                        shardId + ", eventName: " + eventName + ", sequenceNumber: " + sequenceNumber +
                        ", approximateArrivalTimestamp: " + approximateCreationDateTime + ", keys: " + keys);
                Map<String, String> offset = new HashMap<>();
                offset.put("sequenceNumber", sequenceNumber);
                String skipMessage = "DYNAMODBSTREAMS_SKIP delete record - sequenceNumber: "+sequenceNumber+", shardId: "+shardId;
                SkipDoc skipDoc = new SkipDoc(offset, offset, skipMessage, docId, "DDB_DELETE_RECORD", null, skipMessage, shardId);

                return new ParseDocumentResult(null, skipDoc, ParseDocumentResultStatus.SKIP);
            }

            logger.debug("processing record - sequenceNumber: " + sequenceNumber);
            logger.debug("returning success - docId: " + docId);

            IndexRecord documentInterface = objectMapper.readValue(objectMapper.writeValueAsString(newImage), IndexRecord.class);

            return new ParseDocumentResult(null, documentInterface, ParseDocumentResultStatus.SUCCESS);

        } catch (Exception ex) {
            logger.error("Exception in reading the document - streamArn: " + streamArn +
                    ", shardId: " + shardId + ", eventName: " + eventName + ", sequenceNumber: " + sequenceNumber +
                    ", approximateArrivalTimestamp: " + approximateCreationDateTime + ", keys: " + keys +
                    ", ex: " + ex);

            String docIdUUID = UUID.randomUUID().toString();
            Map<String, String> offset = new HashMap<>();
            offset.put("sequenceNumber", sequenceNumber);
            String errorMessage = "Exception in processing record seqNum: "+sequenceNumber+", shardId: "+shardId+", ex: " + ex.getMessage();
            ErrorDoc errorDoc = new ErrorDoc(offset, offset, errorMessage, docIdUUID, "DDB_ERROR", null, errorMessage, shardId);
            return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
        }
    }
}
