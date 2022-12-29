package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.WarcInfoWarcEnvelope;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

public class WatMetadataWarcInfoDoc extends WatMetadataDoc {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /*
    {
        "Container": {
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
        },
        "WarcEnvelope": {
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
    }
    */

    @JsonCreator
    public WatMetadataWarcInfoDoc(@JsonProperty("Container") Container container, @JsonProperty("Envelope") WarcInfoWarcEnvelope envelope) {
        super(DocumentRecordTypes.WAT_METADATA_WARC_WARCINFO_PAYLOAD, container, envelope);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WatMetadataWarcInfoDoc{");
        sb.append("container=").append(container);
        sb.append(", envelope=").append(envelope);
        sb.append('}');
        return sb.toString();
    }

    public static WatMetadataWarcInfoDoc fromString(String document) {
        return objectMapper.convertValue(document, WatMetadataWarcInfoDoc.class);
    }
}