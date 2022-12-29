package com.letsdata.commoncrawl.model.filerecords.docs;

import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

import java.util.Objects;

public abstract class WarcDoc {
    private final DocumentRecordTypes documentRecordTypes;

    public WarcDoc(DocumentRecordTypes documentRecordTypes) {
        this.documentRecordTypes = documentRecordTypes;
    }

    public DocumentRecordTypes getDocumentRecordTypes() {
        return documentRecordTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcDoc warcDoc = (WarcDoc) o;
        return documentRecordTypes == warcDoc.documentRecordTypes;
    }

    @Override
    public int hashCode() {

        return Objects.hash(documentRecordTypes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcDoc{");
        sb.append("documentRecordTypes=").append(documentRecordTypes);
        sb.append('}');
        return sb.toString();
    }
}
