package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata;

import com.letsdata.commoncrawl.model.ContentType;

import java.util.Objects;

public abstract class PayloadMetadata {
    protected final Long actualContentLength;
    protected final ContentType actualContentType;
    protected final String blockDigest;
    protected final Long trailingSlopLength;

    public PayloadMetadata(Long actualContentLength, ContentType actualContentType, String blockDigest, Long trailingSlopLength) {
        this.actualContentLength = actualContentLength;
        this.actualContentType = actualContentType;
        this.blockDigest = blockDigest;
        this.trailingSlopLength = trailingSlopLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayloadMetadata that = (PayloadMetadata) o;
        return actualContentLength == that.actualContentLength &&
                trailingSlopLength == that.trailingSlopLength &&
                actualContentType == that.actualContentType &&
                Objects.equals(blockDigest, that.blockDigest);
    }

    @Override
    public int hashCode() {

        return Objects.hash(actualContentLength, actualContentType, blockDigest, trailingSlopLength);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PayloadMetadata{");
        sb.append("actualContentLength=").append(actualContentLength);
        sb.append(", actualContentType=").append(actualContentType);
        sb.append(", blockDigest='").append(blockDigest).append('\'');
        sb.append(", trailingSlopLength=").append(trailingSlopLength);
        sb.append('}');
        return sb.toString();
    }

    public Long getActualContentLength() {
        return actualContentLength;
    }

    public ContentType getActualContentType() {
        return actualContentType;
    }

    public String getBlockDigest() {
        return blockDigest;
    }

    public Long getTrailingSlopLength() {
        return trailingSlopLength;
    }
}
