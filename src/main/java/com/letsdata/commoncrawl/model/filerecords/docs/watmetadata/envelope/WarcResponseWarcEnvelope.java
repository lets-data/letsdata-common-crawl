package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata.WarcResponsePayloadMetadata;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCResponse;
import org.apache.commons.lang3.StringUtils;

public class WarcResponseWarcEnvelope extends WarcEnvelope {

    @JsonCreator
    public WarcResponseWarcEnvelope(@JsonProperty("Format") String format, @JsonProperty("Payload-Metadata") WarcResponsePayloadMetadata payloadMetadata, @JsonProperty("WARC-Header-Metadata") WARCResponse warcRecord, @JsonProperty("WARC-Header-Length") String warcHeaderLength) {
        super(format, WARCRecordTypes.RESPONSE, payloadMetadata, warcRecord, StringUtils.isBlank(warcHeaderLength) ? null : Long.parseLong(warcHeaderLength));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcResponseWarcEnvelope{");
        sb.append("format='").append(format).append('\'');
        sb.append(", recordType=").append(recordType);
        sb.append(", payloadMetadata=").append(payloadMetadata);
        sb.append(", warcRecord=").append(warcRecord);
        sb.append(", warcHeaderLength=").append(warcHeaderLength);
        sb.append('}');
        return sb.toString();
    }
}