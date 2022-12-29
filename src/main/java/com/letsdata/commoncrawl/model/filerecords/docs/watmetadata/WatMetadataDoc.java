package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.letsdata.commoncrawl.model.filerecords.docs.WarcDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.WarcEnvelope;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

public abstract class WatMetadataDoc extends WarcDoc {

    protected Container container;
    protected WarcEnvelope envelope;

    public WatMetadataDoc(DocumentRecordTypes documentRecordTypes, Container container, WarcEnvelope envelope) {
        super(documentRecordTypes);
        this.container = container;
        this.envelope = envelope;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public WarcEnvelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(WarcEnvelope envelope) {
        this.envelope = envelope;
    }
}
