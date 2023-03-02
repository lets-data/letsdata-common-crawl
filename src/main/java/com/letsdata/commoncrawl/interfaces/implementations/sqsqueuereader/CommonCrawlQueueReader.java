package com.letsdata.commoncrawl.interfaces.implementations.sqsqueuereader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.interfaces.implementations.documents.IndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.reader.CommonCrawlReader;
import com.resonance.letsdata.data.documents.implementation.ErrorDoc;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import com.resonance.letsdata.data.readers.interfaces.sqs.QueueMessageReader;
import com.resonance.letsdata.data.readers.model.ParseDocumentResult;
import com.resonance.letsdata.data.readers.model.ParseDocumentResultStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CommonCrawlQueueReader implements QueueMessageReader {
    private static final Logger logger = LoggerFactory.getLogger(CommonCrawlQueueReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ParseDocumentResult parseMessage(String messageId, String messageGroupId, String messageDeduplicationId, Map<String, String> messageAttributes, String messageBody) {
        if (StringUtils.isBlank(messageBody)) {
            logger.error("message body is blank, returning error - messageId: {}, messageGroupId: {}, messageDeduplicationId: {} , messageAttributes: {}, messageBody: {}", messageId, messageGroupId, messageDeduplicationId, messageAttributes, messageBody);
            ErrorDoc errorDoc = new ErrorDoc(null, null, "empty message body - msgId: "+messageId, messageId, null, null, "empty message body", messageBody);
            return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
        }

        try {
            logger.debug("processing message - messageId: {}", messageId);
            DocumentInterface documentInterface = objectMapper.readValue(messageBody, IndexRecord.class);
            logger.debug("returning success - docId: {}", documentInterface.getDocumentId());
            return new ParseDocumentResult(null, documentInterface, ParseDocumentResultStatus.SUCCESS);
        } catch (JsonProcessingException ex) {
            logger.error("JsonProcessingException in reading the document - messageId: {}, messageGroupId: {}, messageDeduplicationId: {} , messageAttributes: {}, messageBody: {}", messageId, messageGroupId, messageDeduplicationId, messageAttributes, messageBody);
            ErrorDoc errorDoc = new ErrorDoc(null, null, "JsonProcessingException - " + ex.getMessage(), messageId, null, null, "JsonProcessingException - " + ex.getMessage(), messageBody);
            return new ParseDocumentResult(null, errorDoc, ParseDocumentResultStatus.ERROR);
        }
    }
}
