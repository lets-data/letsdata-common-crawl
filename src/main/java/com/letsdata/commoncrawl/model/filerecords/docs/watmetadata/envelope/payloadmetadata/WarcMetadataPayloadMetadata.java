package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.filerecords.docs.WarcMetadataDoc;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class WarcMetadataPayloadMetadata extends PayloadMetadata {

    /*
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
    }*/
    private final WarcMetadataDoc warcMetadataDoc;

    public WarcMetadataPayloadMetadata(@JsonProperty("Actual-Content-Length") String actualContentLength, @JsonProperty("Actual-Content-Type") String actualContentType, @JsonProperty("Block-Digest") String blockDigest, @JsonProperty("Trailing-Slop-Length") String trailingSlopLength, @JsonProperty("WARC-Metadata-Metadata") WarcMetadataDoc.WarcMetadataRecordList warcMetadataRecordList) throws Exception {
        super(StringUtils.isBlank(actualContentLength) ? null : Long.parseLong(actualContentLength), ContentType.fromString(actualContentType), blockDigest, StringUtils.isBlank(trailingSlopLength) ? null : Long.parseLong(trailingSlopLength));
        this.warcMetadataDoc = warcMetadataRecordList.warcMetadataDocFromWarcMetadataRecordList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WarcMetadataPayloadMetadata that = (WarcMetadataPayloadMetadata) o;
        return Objects.equals(warcMetadataDoc, that.warcMetadataDoc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), warcMetadataDoc);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcMetadataPayloadMetadata{");
        sb.append("actualContentLength=").append(actualContentLength);
        sb.append(", actualContentType=").append(actualContentType);
        sb.append(", blockDigest='").append(blockDigest).append('\'');
        sb.append(", trailingSlopLength=").append(trailingSlopLength);
        sb.append(", warcMetadataDoc=").append(warcMetadataDoc);
        sb.append('}');
        return sb.toString();
    }

    public WarcMetadataDoc getWarcMetadataDoc() {
        return warcMetadataDoc;
    }


}