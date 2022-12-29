package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata.WarcInfoPayloadMetadata;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCInfo;
import org.apache.commons.lang3.StringUtils;

public class WarcInfoWarcEnvelope extends WarcEnvelope {
    /*
    "envelope": {
        "Format": "WARC",
        "Payload-Metadata": {
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
        },
        "WARC-Header-Length": "259",
        "WARC-Header-Metadata": {
            "Content-Length": "499",
            "Content-Type": "application/warc-fields",
            "WARC-Date": "2020-07-02T04:57:58Z",
            "WARC-Filename": "CC-MAIN-20200702045758-20200702075758-00000.warc.gz",
            "WARC-Record-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>",
            "WARC-Type": "warcinfo"
        }
    }

    * */

    @JsonCreator
    public WarcInfoWarcEnvelope(@JsonProperty("Format") String format, @JsonProperty("Payload-Metadata") WarcInfoPayloadMetadata payloadMetadata, @JsonProperty("WARC-Header-Metadata") WARCInfo warcRecord, @JsonProperty("WARC-Header-Length") String warcHeaderLength) {
        super(format, WARCRecordTypes.INFO, payloadMetadata, warcRecord, StringUtils.isBlank(warcHeaderLength) ? null : Long.parseLong(warcHeaderLength));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcInfoWarcEnvelope{");
        sb.append("format='").append(format).append('\'');
        sb.append(", recordType=").append(recordType);
        sb.append(", payloadMetadata=").append(payloadMetadata);
        sb.append(", warcRecord=").append(warcRecord);
        sb.append(", warcHeaderLength=").append(warcHeaderLength);
        sb.append('}');
        return sb.toString();
    }
}