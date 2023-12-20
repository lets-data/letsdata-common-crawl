package com.letsdata.commoncrawl.interfaces.implementations.sagemaker;

import com.letsdata.commoncrawl.interfaces.implementations.documents.CompositeIndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.documents.IndexRecord;
import com.letsdata.commoncrawl.interfaces.implementations.documents.VectorRecord;
import com.letsdata.commoncrawl.model.filerecords.warc.AbstractWARCRecord;
import com.resonance.letsdata.data.documents.interfaces.DocumentInterface;
import com.resonance.letsdata.data.readers.interfaces.sagemaker.SagemakerVectorsInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class CommonCrawlSagemakerReader implements SagemakerVectorsInterface {
    private static final Logger logger = LoggerFactory.getLogger(CommonCrawlSagemakerReader.class);

    @Override
    public Map<String, String> extractDocumentElementsForVectorization(DocumentInterface documentInterface) {
        if (documentInterface == null) {
            logger.error("extractDocumentElementsForVectorization - documentInterface is null");
            throw new RuntimeException("extractDocumentElementsForVectorization - documentInterface is null");
        }

        if (!(documentInterface instanceof CompositeIndexRecord)) {
            logger.error("extractDocumentElementsForVectorization - documentInterface is expected to be of type CompositeIndexRecord");
            throw new RuntimeException("extractDocumentElementsForVectorization - documentInterface is expected to be of type CompositeIndexRecord");
        }

        if (!(((CompositeIndexRecord)documentInterface).getDocumentList().getKey() instanceof IndexRecord)) {
            logger.error("extractDocumentElementsForVectorization - indexRecord not found in CompositeIndexRecord");
            throw new RuntimeException("extractDocumentElementsForVectorization - indexRecord not found in CompositeIndexRecord");
        }

        try {
            IndexRecord indexRecord = (IndexRecord) ((CompositeIndexRecord)documentInterface).getDocumentList().getKey();
            Map<String, String> docMap = new HashMap<>();
            if (indexRecord.getDocText() != null) {
                docMap.put("DocText", indexRecord.getDocText());
            }

            if (indexRecord.getDescription() != null) {
                docMap.put("DocDescription", indexRecord.getDescription());
            }

            return docMap;
        } catch (Exception ex) {
            logger.error("extractDocumentElementsForVectorization threw an exception - doc: {}, ex: {}", documentInterface, ex);
            throw new RuntimeException("extractDocumentElementsForVectorization threw an exception", ex);
        }
    }

    @Override
    public DocumentInterface constructVectorDoc(DocumentInterface documentInterface, Map<String, Double[]> vectorsMap) {
        if (documentInterface == null) {
            logger.error("constructFeatureAndVectorDocs - documentInterface is null");
            throw new RuntimeException("constructFeatureAndVectorDocs - documentInterface is null");
        }

        if (vectorsMap == null || vectorsMap.isEmpty()) {
            logger.error("constructFeatureAndVectorDocs - vectorsMap is null or empty");
            throw new RuntimeException("constructFeatureAndVectorDocs - vectorsMap is null or empty");
        }

        if (!(documentInterface instanceof CompositeIndexRecord)) {
            logger.error("constructFeatureAndVectorDocs - documentInterface is expected to be of type CompositeIndexRecord");
            throw new RuntimeException("constructFeatureAndVectorDocs - documentInterface is expected to be of type CompositeIndexRecord");
        }

        if (!(((CompositeIndexRecord)documentInterface).getDocumentList().getKey() instanceof IndexRecord)) {
            logger.error("constructFeatureAndVectorDocs - indexRecord not found in CompositeIndexRecord");
            throw new RuntimeException("constructFeatureAndVectorDocs - indexRecord not found in CompositeIndexRecord");
        }

        IndexRecord indexRecord = (IndexRecord) ((CompositeIndexRecord)documentInterface).getDocumentList().getKey();
        Double[] docTextVectors = vectorsMap.get("DocText");
        Double[] docDescriptionVectors = vectorsMap.get("DocDescription");
        String recordType = "VECTOR";
        String documentId = indexRecord.getDocumentId();

        // momento vector index key should be less than 256 bytes. 400 threshold to allow for some testing
        if (documentId.length() > 400) {
            documentId = AbstractWARCRecord.computeMD5Hash(documentId);
        }
        return new VectorRecord(indexRecord.getUrl(), documentId, recordType, indexRecord.getDocumentMetadata(), docTextVectors, docDescriptionVectors);
    }
}
