package com.letsdata.commoncrawl.interfaces.implementations.dynamodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.interfaces.implementations.documents.IndexRecord;
import com.resonance.letsdata.data.documents.implementation.ErrorDoc;
import com.resonance.letsdata.data.documents.implementation.SkipDoc;
import com.resonance.letsdata.data.readers.interfaces.dynamodb.DynamoDBTableItemReader;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonCrawlDDBItemReader implements DynamoDBTableItemReader {
    private static final Logger logger = LoggerFactory.getLogger(CommonCrawlDDBItemReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ParseDocumentResult parseDynamoDBItem(String tableName, int segmentNumber, Map<String, Object> keys, Map<String, Object> item) {
        try {
            String docId = null;
            for (Object keyValue : keys.values()) {
                if (docId == null) {
                    docId = keyValue.toString();
                } else {
                    docId += ('|' + keyValue.toString());
                }
            }

            if (item == null || item.size() <= 0) {
                logger.error("item is null, returning error - tableName: " + tableName + ", keys: " + keys);
                Map<String, String> offset = new HashMap<>();
                offset.put("keys", objectMapper.writeValueAsString(keys));
                String errorMessage = "DYNAMODB error record - keys: "+keys+", tableName: "+tableName;
                ErrorDoc errorDoc = new ErrorDoc(offset, offset, errorMessage, docId, "DDB_ERROR_RECORD", null, errorMessage, Integer.toString(segmentNumber));

                return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
            }

            logger.debug("processing record, returning success - docId: " + docId);

            IndexRecord documentInterface = objectMapper.readValue(objectMapper.writeValueAsString(item), IndexRecord.class);

            return new ParseDocumentResult(null, documentInterface, ParseDocumentResultStatus.SUCCESS);

        } catch (Exception ex) {
            logger.error("Exception in reading the document - tableName: " + tableName + ", keys: " + keys + ", ex: " + ex);

            String docIdUUID = UUID.randomUUID().toString();
            Map<String, String> offset = new HashMap<>();
            try {
                offset.put("keys", objectMapper.writeValueAsString(keys));
            } catch (Exception e) {
                offset.put("keys", "error calculating offset");
            }
            String errorMessage = "Exception in processing item keys: "+keys+", ex: " + ex.getMessage();
            ErrorDoc errorDoc = new ErrorDoc(offset, offset, errorMessage, docIdUUID, "DDB_ERROR", null, errorMessage, Integer.toString(segmentNumber));
            return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
        }
    }
}
