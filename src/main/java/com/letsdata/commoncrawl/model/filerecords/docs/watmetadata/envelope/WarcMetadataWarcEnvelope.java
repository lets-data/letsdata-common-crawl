package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata.WarcMetadataPayloadMetadata;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCMetadata;
import org.apache.commons.lang3.StringUtils;

public class WarcMetadataWarcEnvelope extends WarcEnvelope {

    /*
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
    * */
    @JsonCreator
    public WarcMetadataWarcEnvelope(@JsonProperty("Format") String format, @JsonProperty("Payload-Metadata") WarcMetadataPayloadMetadata payloadMetadata, @JsonProperty("WARC-Header-Metadata") WARCMetadata warcRecord, @JsonProperty("WARC-Header-Length") String warcHeaderLength) {
        super(format, WARCRecordTypes.METADATA, payloadMetadata, warcRecord, StringUtils.isBlank(warcHeaderLength) ? null : Long.parseLong(warcHeaderLength));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcMetadataWarcEnvelope{");
        sb.append("format='").append(format).append('\'');
        sb.append(", recordType=").append(recordType);
        sb.append(", payloadMetadata=").append(payloadMetadata);
        sb.append(", warcRecord=").append(warcRecord);
        sb.append(", warcHeaderLength=").append(warcHeaderLength);
        sb.append('}');
        return sb.toString();
    }
}
