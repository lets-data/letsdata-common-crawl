package com.letsdata.commoncrawl.model.filerecords.docs;

import com.letsdata.commoncrawl.model.CrawlDataRecordErrorException;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

import java.util.Map;
import java.util.Objects;

public class WarcErrorDoc extends WarcDoc {
    private final String docValue;
    private final CrawlDataRecordErrorException exception;
    private final Map<String, Long> recordStartOffset;
    private final Map<String, Long> recordEndOffset;

    public WarcErrorDoc(DocumentRecordTypes documentRecordTypes, String value, CrawlDataRecordErrorException ex, Map<String, Long> recordStartOffset, Map<String, Long> recordEndOffset) {
        super(validateDocumentTypeIsErrorType(documentRecordTypes));
        this.docValue = value;
        this.exception = ex;
        this.recordStartOffset = recordStartOffset;
        this.recordEndOffset = recordEndOffset;
    }

    private static DocumentRecordTypes validateDocumentTypeIsErrorType(DocumentRecordTypes documentRecordType) {
        /*ValidationUtils.validateAssertCondition(documentRecordType == DocumentRecordTypes.WARC_ERROR ||
                documentRecordType == DocumentRecordTypes.WAT_ERROR ||
                documentRecordType == DocumentRecordTypes.WET_ERROR, "document record type expected to be one of the error document record types", documentRecordType);*/
        return documentRecordType;
    }

    public String getDocValue() {
        return docValue;
    }

    public CrawlDataRecordErrorException getException() {
        return exception;
    }

    public Map<String, Long> getRecordStartOffset() {
        return recordStartOffset;
    }

    public Map<String, Long> getRecordEndOffset() {
        return recordEndOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WarcErrorDoc that = (WarcErrorDoc) o;
        return recordStartOffset == that.recordStartOffset &&
                recordEndOffset == that.recordEndOffset &&
                Objects.equals(docValue, that.docValue) &&
                Objects.equals(exception, that.exception);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), docValue, exception, recordStartOffset, recordEndOffset);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcErrorDoc{");
        sb.append("docValue='").append(docValue).append('\'');
        sb.append(", exception=").append(exception);
        sb.append(", recordStartOffset=").append(recordStartOffset);
        sb.append(", recordEndOffset=").append(recordEndOffset);
        sb.append('}');
        return sb.toString();
    }
}
