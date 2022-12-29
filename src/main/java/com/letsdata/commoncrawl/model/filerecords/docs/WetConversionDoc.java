package com.letsdata.commoncrawl.model.filerecords.docs;

import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

import java.util.Objects;

public class WetConversionDoc extends WarcDoc {
    private final String docText;

    public WetConversionDoc(String text) {
        super(DocumentRecordTypes.WET_CONVERSION_PAYLOAD);
        this.docText = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WetConversionDoc that = (WetConversionDoc) o;
        return Objects.equals(docText, that.docText);
    }

    @Override
    public int hashCode() {

        return Objects.hash(docText);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WetConversionDoc{");
        sb.append("docText='").append(docText).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getDocText() {
        return docText;
    }
}
