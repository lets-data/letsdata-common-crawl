package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata.WarcRequestPayloadMetadata;
import com.letsdata.commoncrawl.model.filerecords.types.WARCRecordTypes;
import com.letsdata.commoncrawl.model.filerecords.warc.WARCRequest;
import org.apache.commons.lang3.StringUtils;

public class WarcRequestWarcEnvelope extends WarcEnvelope {

    @JsonCreator
    public WarcRequestWarcEnvelope(@JsonProperty("Format") String format, @JsonProperty("Payload-Metadata") WarcRequestPayloadMetadata payloadMetadata, @JsonProperty("WARC-Header-Metadata") WARCRequest warcRecord, @JsonProperty("WARC-Header-Length") String warcHeaderLength) {
        super(format, WARCRecordTypes.REQUEST, payloadMetadata, warcRecord, StringUtils.isBlank(warcHeaderLength) ? null : Long.parseLong(warcHeaderLength));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcRequestWarcEnvelope{");
        sb.append("format='").append(format).append('\'');
        sb.append(", recordType=").append(recordType);
        sb.append(", payloadMetadata=").append(payloadMetadata);
        sb.append(", warcRecord=").append(warcRecord);
        sb.append(", warcHeaderLength=").append(warcHeaderLength);
        sb.append('}');
        return sb.toString();
    }
}
