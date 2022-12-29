package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class GzipMetadata {
    private final Long deflateLength;
    private final Long footerLength;
    private final Long headerLength;
    private final Long inflatedCRC;
    private final Long inflatedLength;

    public GzipMetadata(@JsonProperty("Deflate-Length") String deflateLength, @JsonProperty("Footer-Length") String footerLength, @JsonProperty("Header-Length") String headerLength, @JsonProperty("Inflated-CRC") String inflatedCRC, @JsonProperty("Inflated-Length") String inflatedLength) {
        this.deflateLength = StringUtils.isBlank(deflateLength) ? null : Long.parseLong(deflateLength);
        this.footerLength = StringUtils.isBlank(footerLength) ? null : Long.parseLong(footerLength);
        this.headerLength = StringUtils.isBlank(headerLength) ? null : Long.parseLong(headerLength);
        this.inflatedCRC = StringUtils.isBlank(inflatedCRC) ? null : Long.parseLong(inflatedCRC);
        this.inflatedLength = StringUtils.isBlank(inflatedLength) ? null : Long.parseLong(inflatedLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GzipMetadata that = (GzipMetadata) o;
        return Objects.equals(deflateLength, that.deflateLength) &&
                Objects.equals(footerLength, that.footerLength) &&
                Objects.equals(headerLength, that.headerLength) &&
                Objects.equals(inflatedCRC, that.inflatedCRC) &&
                Objects.equals(inflatedLength, that.inflatedLength);
    }

    @Override
    public int hashCode() {

        return Objects.hash(deflateLength, footerLength, headerLength, inflatedCRC, inflatedLength);
    }

    public Long getDeflateLength() {
        return deflateLength;
    }

    public Long getFooterLength() {
        return footerLength;
    }

    public Long getHeaderLength() {
        return headerLength;
    }

    public Long getInflatedCRC() {
        return inflatedCRC;
    }

    public Long getInflatedLength() {
        return inflatedLength;
    }
}