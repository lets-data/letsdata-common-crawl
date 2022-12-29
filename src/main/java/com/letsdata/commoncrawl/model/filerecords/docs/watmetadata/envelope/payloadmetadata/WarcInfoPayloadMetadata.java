package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.filerecords.docs.warcinfo.WARCWarcInfoDoc;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class WarcInfoPayloadMetadata extends PayloadMetadata {

        /*"Payload-Metadata": {
            "Actual-Content-Length": "499",
            "Actual-Content-Type": "application/warc-fields",
            "Block-Digest": "sha1:YW2NZZGHUAZSWSLMMW6Q4OYRDZ2IM7GM",
            "Headers-Corrupt": true,
            "Trailing-Slop-Length": "0",
            "WARC-Info-Metadata": {
                "description": "Wide crawl of the web for July 2020",
                "format": "WARC File Format 1.1",
                "hostname": "ip-10-67-67-207.ec2.internal",
                "isPartOf": "CC-MAIN-2020-29",
                "operator": "Common Crawl Admin (info@commoncrawl.org)",
                "publisher": "Common Crawl",
                "robots": "checked via crawler-commons 1.2-SNAPSHOT (https://github.com/crawler-commons/crawler-commons)",
                "software": "Apache Nutch 1.17 (modified, https://github.com/commoncrawl/nutch/)"
            }
        }
        */

    private final WARCWarcInfoDoc warcInfoMetadata;

    @JsonCreator
    public WarcInfoPayloadMetadata(@JsonProperty("Actual-Content-Length") String actualContentLength, @JsonProperty("Actual-Content-Type") String actualContentType, @JsonProperty("Block-Digest") String blockDigest, @JsonProperty("Headers-Corrupt") boolean headersCorrupt, @JsonProperty("Trailing-Slop-Length") String trailingSlopLength, @JsonProperty("WARC-Info-Metadata") WARCWarcInfoDoc warcInfoMetadata) {
        super(StringUtils.isBlank(actualContentLength) ? null : Long.parseLong(actualContentLength), ContentType.fromString(actualContentType), blockDigest, StringUtils.isBlank(trailingSlopLength) ? null : Long.parseLong(trailingSlopLength));
        this.warcInfoMetadata = warcInfoMetadata;
    }

    public WARCWarcInfoDoc getWARCWarcInfoDoc() {
        return warcInfoMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WarcInfoPayloadMetadata that = (WarcInfoPayloadMetadata) o;
        return Objects.equals(warcInfoMetadata, that.warcInfoMetadata);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), warcInfoMetadata);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcInfoPayloadMetadata{");
        sb.append("actualContentLength=").append(actualContentLength);
        sb.append(", actualContentType=").append(actualContentType);
        sb.append(", blockDigest='").append(blockDigest).append('\'');
        sb.append(", trailingSlopLength=").append(trailingSlopLength);
        sb.append(", warcInfoMetadata=").append(warcInfoMetadata);
        sb.append('}');
        return sb.toString();
    }
}