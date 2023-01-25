package com.letsdata.commoncrawl.interfaces.implementations.documents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resonance.letsdata.data.documents.interfaces.CompositeDocInterface;
import com.resonance.letsdata.data.documents.interfaces.ErrorDocInterface;
import com.resonance.letsdata.data.documents.interfaces.SingleDocInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CompositeIndexRecord implements CompositeDocInterface {
    private static final Logger logger = LoggerFactory.getLogger(CompositeIndexRecord.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String documentId;
    private final Map.Entry<SingleDocInterface, List<ErrorDocInterface>> documentsMap;

    public CompositeIndexRecord(final String documentId, final Map.Entry<SingleDocInterface, List<ErrorDocInterface>> documentsMap) {
        this.documentsMap = documentsMap;
        this.documentId = documentId;
    }

    @Override
    public Map.Entry<SingleDocInterface, List<ErrorDocInterface>> getDocumentList() {
        return documentsMap;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public String getRecordType() {
        return documentsMap.getKey().getRecordType();
    }

    @Override
    public Map<String, Object> getDocumentMetadata() {
        return documentsMap.getKey().getDocumentMetadata();
    }

    @Override
    public String serialize() {
        try {
            return objectMapper.writeValueAsString(documentsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("getDocumentContents serialize exception", e);
        }
    }

    @Override
    public String getPartitionKey() {
        return documentsMap.getKey().getPartitionKey();
    }
}