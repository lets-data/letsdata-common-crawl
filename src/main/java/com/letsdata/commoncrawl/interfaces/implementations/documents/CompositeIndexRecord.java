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

    public static final SingleDocInterface NULL_DOC = new SingleDocInterface() {
        @Override
        public String getDocumentId() {
            return "NULL";
        }

        @Override
        public String getRecordType() {
            return "NULL,NULL";
        }

        @Override
        public Map<String, Object> getDocumentMetadata() {
            return new HashMap<>();
        }

        @Override
        public String serialize() {
            return "";
        }

        @Override
        public String getPartitionKey() {
            return "";
        }

        @Override
        public boolean isSingleDoc() { return true; }
    };

    private final String documentId;
    private final LinkedHashMap<SingleDocInterface, List<ErrorDocInterface>> documentsMap;

    public CompositeIndexRecord(final String documentId, final LinkedHashMap<SingleDocInterface, List<ErrorDocInterface>> documentsMap) {
        this.documentsMap = documentsMap;
        this.documentId = documentId;
    }

    @Override
    public LinkedHashMap<SingleDocInterface, List<ErrorDocInterface>> getDocumentList() {
        return documentsMap;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public String getRecordType() {
        SingleDocInterface doc = documentsMap.keySet().iterator().next();
        if (!doc.getDocumentId().equalsIgnoreCase(NULL_DOC.getDocumentId())) {
            return doc.getDocumentId();
        } else {
            return documentsMap.get(doc).get(0).getDocumentId();
        }
    }

    @Override
    public Map<String, Object> getDocumentMetadata() {
        SingleDocInterface doc = documentsMap.keySet().iterator().next();
        if (!doc.getDocumentId().equalsIgnoreCase(NULL_DOC.getDocumentId())) {
            return doc.getDocumentMetadata();
        } else {
            return documentsMap.get(doc).get(0).getDocumentMetadata();
        }
    }

    @Override
    public String serialize() {
        Map<String, AbstractMap.SimpleEntry<SingleDocInterface, List<ErrorDocInterface>>> serializeMap = new HashMap<>();
        int i = 0;
        for(SingleDocInterface doc : documentsMap.keySet()) {
            serializeMap.put("doc"+i, new AbstractMap.SimpleEntry<>(doc, documentsMap.get(doc)));
        }

        try {
            return objectMapper.writeValueAsString(serializeMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("getDocumentContents serialize exception", e);
        }
    }

    @Override
    public String getPartitionKey() {
        SingleDocInterface doc = documentsMap.keySet().iterator().next();
        if (!doc.getDocumentId().equalsIgnoreCase(NULL_DOC.getDocumentId())) {
            return doc.getPartitionKey();
        } else {
            return documentsMap.get(doc).get(0).getPartitionKey();
        }
    }
}