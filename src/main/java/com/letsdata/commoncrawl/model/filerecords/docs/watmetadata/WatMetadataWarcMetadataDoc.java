package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.WarcMetadataWarcEnvelope;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

public class WatMetadataWarcMetadataDoc extends WatMetadataDoc {
        /*
        {
            "Container": {
                "Compressed": true,
                "Filename": "CC-MAIN-20200702045758-20200702075758-00000.warc.gz",
                "Gzip-Metadata": {
                    "Deflate-Length": "428",
                    "Footer-Length": "8",
                    "Header-Length": "10",
                    "Inflated-CRC": "1806903743",
                    "Inflated-Length": "597"
                },
                "Offset": "12303"
            },
            "WarcEnvelope": {
                "Format": "WARC",
                "Payload-Metadata": {
                    "Actual-Content-Length": "203",
                    "Actual-Content-Type": "application/metadata-fields",
                    "Block-Digest": "sha1:AQYVJWIBGCCP3OD6D7I654QXA57ALGAQ",
                    "Trailing-Slop-Length": "0",
                    "WARC-Metadata-Metadata": {
                        "Metadata-Records": [
                            {
                                "Name": "fetchTimeMs",
                                "Value": "696"
                            },
                            {
                                "Name": "charset-detected",
                                "Value": "GB2312"
                            },
                            {
                                "Name": "languages-cld2",
                                "Value": "{\"reliable\":true,\"text-bytes\":10919,\"languages\":[{\"code\":\"zh\",\"code-iso-639-3\":\"zho\",\"text-covered\":0.97,\"score\":1868.0,\"name\":\"Chinese\"}]}"
                            }
                        ]
                    }
                },
                "WARC-Header-Length": "390",
                "WARC-Header-Metadata": {
                    "Content-Length": "203",
                    "Content-Type": "application/warc-fields",
                    "WARC-Concurrent-To": "<urn:uuid:34b4755b-3ca3-464a-8ad2-988e2e872efa>",
                    "WARC-Date": "2020-07-02T05:29:36Z",
                    "WARC-Record-ID": "<urn:uuid:9f1abc20-3b8c-4780-801a-daf99b6ba613>",
                    "WARC-Target-URI": "http://000.266sbc.com/zhongkaozw/zhongkaosc/",
                    "WARC-Type": "metadata",
                    "WARC-Warcinfo-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>"
                }
            }
        }
        */
    @JsonCreator
    public WatMetadataWarcMetadataDoc(@JsonProperty("Container") Container container, @JsonProperty("Envelope") WarcMetadataWarcEnvelope envelope) {
        super(DocumentRecordTypes.WAT_METADATA_WARC_METADATA_PAYLOAD, container, envelope);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WatMetadataWarcMetadataDoc{");
        sb.append("container=").append(container);
        sb.append(", envelope=").append(envelope);
        sb.append('}');
        return sb.toString();
    }
}