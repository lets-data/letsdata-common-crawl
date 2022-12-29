package com.letsdata.commoncrawl.model.filerecords.docs.warcinfo;

import com.letsdata.commoncrawl.model.filerecords.docs.WarcDoc;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;

public abstract class WarcInfoDoc extends WarcDoc {
    public WarcInfoDoc(DocumentRecordTypes documentRecordTypes) {
        super(documentRecordTypes);
    }
}