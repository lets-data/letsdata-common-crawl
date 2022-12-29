package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.WarcRequestWarcEnvelope;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

public class WatMetadataWarcRequestDoc extends WatMetadataDoc {
    /*
    {
        "Container": {
            "Compressed": true,
            "Filename": "CC-MAIN-20200702045758-20200702075758-00000.warc.gz",
            "Gzip-Metadata": {
                "Deflate-Length": "432",
                "Footer-Length": "8",
                "Header-Length": "10",
                "Inflated-CRC": "-1404157712",
                "Inflated-Length": "639"
            },
            "Offset": "479"
        },
        "WarcEnvelope": {
            "Format": "WARC",
            "Payload-Metadata": {
                "Actual-Content-Length": "272",
                "Actual-Content-Type": "application/http; msgtype=request",
                "Block-Digest": "sha1:JQ6YBRMZSSKE7EMW4C4W2FSWWE3TL2QT",
                "HTTP-Request-Metadata": {
                    "Entity-Digest": "sha1:3I42H3S6NNFQ2MSVX7XZKYAYSCX5QBYJ",
                    "Entity-Length": "0",
                    "Entity-Trailing-Slop-Length": "0",
                    "Headers": {
                        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*\/*;q=0.8",
                        "Accept-Encoding": "br,gzip",
                        "Accept-Language": "en-US,en;q=0.5",
                        "Connection": "Keep-Alive",
                        "Host": "000.266sbc.com",
                        "User-Agent": "CCBot/2.0 (https://commoncrawl.org/faq/)"
                    },
                    "Headers-Length": "270",
                    "Request-Message": {
                        "Method": "GET",
                        "Path": "/zhongkaozw/zhongkaosc/",
                        "Version": "HTTP/1.1"
                    }
                },
                "Trailing-Slop-Length": "4"
            },
            "WARC-Header-Length": "363",
            "WARC-Header-Metadata": {
                "Content-Length": "272",
                "Content-Type": "application/http; msgtype=request",
                "WARC-Date": "2020-07-02T05:29:36Z",
                "WARC-IP-Address": "107.160.155.30",
                "WARC-Record-ID": "<urn:uuid:776fac7c-b1ee-40ba-8444-2f0da1c33400>",
                "WARC-Target-URI": "http://000.266sbc.com/zhongkaozw/zhongkaosc/",
                "WARC-Type": "request",
                "WARC-Warcinfo-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>"
            }
        }
    }
    */

    @JsonCreator
    public WatMetadataWarcRequestDoc(@JsonProperty("Container")  Container container, @JsonProperty("Envelope") WarcRequestWarcEnvelope envelope) {
        super(DocumentRecordTypes.WAT_METADATA_WARC_REQUEST_PAYLOAD, container, envelope);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WatMetadataWarcRequestDoc{");
        sb.append("container=").append(container);
        sb.append(", envelope=").append(envelope);
        sb.append('}');
        return sb.toString();
    }
}