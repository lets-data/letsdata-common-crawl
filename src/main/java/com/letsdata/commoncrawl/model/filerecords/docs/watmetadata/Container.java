package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/*"Container": {
    "Compressed": true,
    "Filename": "CC-MAIN-20200702045758-20200702075758-00000.warc.gz",
    "Gzip-Metadata": {
        "Deflate-Length": "479",
        "Footer-Length": "8",
        "Header-Length": "10",
        "Inflated-CRC": "-231675272",
        "Inflated-Length": "762"
    },
    "Offset": "0"
}*/
public class Container {
    private final boolean compressed;
    private final String filename;
    private final Long offset;
    private final GzipMetadata gzipMetadata;

    @JsonCreator
    public Container(@JsonProperty("Compressed") boolean compressed, @JsonProperty("Filename") String filename, @JsonProperty("Offset") String offset, @JsonProperty("Gzip-Metadata") GzipMetadata gzipMetadata) {
        this.compressed = compressed;
        this.filename = filename;
        this.offset = StringUtils.isBlank(offset) ? null : Long.parseLong(offset);
        this.gzipMetadata = gzipMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Container container = (Container) o;
        return compressed == container.compressed &&
                offset == container.offset &&
                Objects.equals(filename, container.filename) &&
                Objects.equals(gzipMetadata, container.gzipMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compressed, filename, offset, gzipMetadata);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Container{");
        sb.append("compressed=").append(compressed);
        sb.append(", filename='").append(filename).append('\'');
        sb.append(", offset=").append(offset);
        sb.append(", gzipMetadata=").append(gzipMetadata);
        sb.append('}');
        return sb.toString();
    }

    public boolean isCompressed() {
        return compressed;
    }

    public String getFilename() {
        return filename;
    }

    public Long getOffset() {
        return offset;
    }

    public GzipMetadata getGzipMetadata() {
        return gzipMetadata;
    }
}