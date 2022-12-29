package com.letsdata.commoncrawl.model.filerecords.warc;

import com.letsdata.commoncrawl.model.CommonCrawlFileType;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.Language;
import com.letsdata.commoncrawl.model.filerecords.docs.WetConversionDoc;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;

public class WARCConversion extends AbstractWARCRecord {
    private static final Logger logger = LoggerFactory.getLogger(WARCInfo.class);

    private final Date date;
    private final String refersTo;
    private final String blockDigest;
    private final Language language;

    public WARCConversion(CommonCrawlFileType commonCrawlFileType, String protocol, double version, WARCRecordTypes recordType, ContentType contentType, int contentLength, WetConversionDoc wetConversionDoc, String targetUri, Date date, String recordId, String refersTo, String blockDigest, Language language) {
        super(commonCrawlFileType, protocol, version, recordType, date, recordId, targetUri, contentType, contentLength, wetConversionDoc);
        this.date = date;
        this.refersTo = refersTo;
        this.blockDigest = blockDigest;
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WARCConversion that = (WARCConversion) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(recordId, that.recordId) &&
                Objects.equals(refersTo, that.refersTo) &&
                Objects.equals(blockDigest, that.blockDigest) &&
                language == that.language;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), date, recordId, refersTo, blockDigest, language);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WARCConversion{");
        sb.append(", date=").append(date);
        sb.append(", recordId='").append(recordId).append('\'');
        sb.append(", targetUri=").append(targetUri);
        sb.append(", refersTo='").append(refersTo).append('\'');
        sb.append(", blockDigest='").append(blockDigest).append('\'');
        sb.append(", language=").append(language);
        sb.append(", commonCrawlFileType=").append(commonCrawlFileType);
        sb.append(", protocol='").append(protocol).append('\'');
        sb.append(", version=").append(version);
        sb.append(", recordType=").append(recordType);
        sb.append(", contentType=").append(contentType);
        sb.append(", contentLength=").append(contentLength);
        sb.append(", warcDoc=").append(warcDoc);
        sb.append('}');
        return sb.toString();
    }

    public Date getDate() {
        return date;
    }

    public String getRecordId() {
        return recordId;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public String getRefersTo() {
        return refersTo;
    }

    public String getBlockDigest() {
        return blockDigest;
    }

    public Language getLanguage() {
        return language;
    }
}
