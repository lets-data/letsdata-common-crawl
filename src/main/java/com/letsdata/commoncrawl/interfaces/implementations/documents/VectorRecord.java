package com.letsdata.commoncrawl.interfaces.implementations.documents;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.Map;

public class VectorRecord implements DocumentInterface {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String url;
    private final String docId;
    private final String recordType;
    private final Map<String, Object> documentMetadata;

    private final Double[] docTextVectors;
    private final Double[] docDescriptionVectors;
    public VectorRecord(@JsonProperty("url") String url, @JsonProperty("documentId") String docId, @JsonProperty("recordType") String recordType, @JsonProperty("documentMetadata") Map<String, Object> documentMetadata,
                        @JsonProperty("docTextVectors") Double[] docTextVectors, @JsonProperty("docDescriptionVectors") Double[] docDescriptionVectors) {
        this.url = url;
        this.docId = docId;
        this.recordType = recordType;
        this.documentMetadata = documentMetadata;
        this.docTextVectors = docTextVectors;
        this.docDescriptionVectors = docDescriptionVectors;
    }

    @Override
    public String getDocumentId() {
        return docId;
    }

    @Override
    public String getRecordType() {
        return recordType;
    }

    @Override
    public Map<String, Object> getDocumentMetadata() {
        return documentMetadata;
    }

    @Override
    public String getPartitionKey() {
        return url;
    }

    @JsonIgnore
    @Override
    public String serialize() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("VectorRecord serialize threw exception", e);
        }
    }

    public String getUrl() {
        return url;
    }

    public Double[] getDocTextVectors() {
        return docTextVectors;
    }

    public Double[] getDocDescriptionVectors() {
        return docDescriptionVectors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof VectorRecord)) return false;

        VectorRecord that = (VectorRecord) o;

        return new EqualsBuilder().append(getUrl(), that.getUrl()).append(docId, that.docId).append(getRecordType(), that.getRecordType()).append(getDocumentMetadata(), that.getDocumentMetadata()).append(getDocTextVectors(), that.getDocTextVectors()).append(getDocDescriptionVectors(), that.getDocDescriptionVectors()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getUrl()).append(docId).append(getRecordType()).append(getDocumentMetadata()).append(getDocTextVectors()).append(getDocDescriptionVectors()).toHashCode();
    }

    @Override
    public String toString() {
        return "VectorRecord{" +
                "url='" + url + '\'' +
                ", docId='" + docId + '\'' +
                ", recordType='" + recordType + '\'' +
                ", documentMetadata=" + documentMetadata +
                ", docTextVectors=" + Arrays.toString(docTextVectors) +
                ", docDescriptionVectors=" + Arrays.toString(docDescriptionVectors) +
            '}';
    }
}
