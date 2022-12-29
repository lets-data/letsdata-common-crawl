package com.letsdata.commoncrawl.model.filerecords.docs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.LanguageStats;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import com.resonance.letsdata.data.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;

public class WarcMetadataDoc extends WarcDoc {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(WarcMetadataDoc.class);
    /*
    fetchTimeMs: 696
    charset-detected: GB2312
    languages-cld2: {
        "reliable":true,
        "text-bytes":10919,
        "languages":[
            {
                "code":"zh",
                "code-iso-639-3":
                "zho",
                "text-covered":0.97,
                "score":1868.0,
                "name":"Chinese"
            }
        ]
    }
    */

    private final Long fetchTimeMillis;
    private final Charset detectedCharset;
    private final Boolean reliable;
    private final Long textBytes;
    private final List<LanguageStats> languages;

    public WarcMetadataDoc(Long fetchTimeMillis, Charset detectedCharset, Boolean reliable, Long textBytes, List<LanguageStats> languages) {
        super(DocumentRecordTypes.WARC_METADATA_PAYLOAD);
        this.fetchTimeMillis = fetchTimeMillis;
        this.detectedCharset = detectedCharset;
        this.reliable = reliable;
        this.textBytes = textBytes;
        this.languages = languages;
    }

    public Long getFetchTimeMillis() {
        return fetchTimeMillis;
    }

    public Charset getDetectedCharset() {
        return detectedCharset;
    }

    public Boolean getReliable() {
        return reliable;
    }

    public Long getTextBytes() {
        return textBytes;
    }

    public List<LanguageStats> getLanguages() {
        return languages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarcMetadataDoc that = (WarcMetadataDoc) o;
        return fetchTimeMillis == that.fetchTimeMillis &&
                textBytes == that.textBytes &&
                Objects.equals(detectedCharset, that.detectedCharset) &&
                Objects.equals(reliable, that.reliable) &&
                Objects.equals(languages, that.languages);
    }

    @Override
    public int hashCode() {

        return Objects.hash(fetchTimeMillis, detectedCharset, reliable, textBytes, languages);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WarcMetadataDoc{");
        sb.append("fetchTimeMillis=").append(fetchTimeMillis);
        sb.append(", detectedCharset=").append(detectedCharset);
        sb.append(", reliable=").append(reliable);
        sb.append(", textBytes=").append(textBytes);
        sb.append(", languages=").append(languages);
        sb.append('}');
        return sb.toString();
    }

    public static WarcMetadataDoc getWarcMetadataDocFromString(String warcMetdataStr, int startIndex, int endIndex) throws Exception {
        Long fetchTimeMillis = null;
        Charset charsetDetected = null;
        WarcMetadataLanguageMetadata languageMetadata = null;

        String document = warcMetdataStr.substring(startIndex, endIndex).trim();
        String[] lines = document.split("\n");
        if (lines.length >= 1 && lines[0].startsWith("fetchTimeMs: ")) {
            fetchTimeMillis = Long.parseLong(lines[0].split(": ")[1].trim());
        }

        if (lines.length >= 2 && lines[1].startsWith("charset-detected: ")) {
            String charsetName = lines[1].split(": ")[1].trim();
            charsetDetected = Charset.forName(charsetName);
        }

        if (lines.length >= 3 && lines[2].startsWith("languages-cld2: ")) {
            String jsonString = lines[2].split(": ")[1].trim();
            languageMetadata = objectMapper.readValue(jsonString, WarcMetadataLanguageMetadata.class);
        }

        ValidationUtils.validateAssertCondition(fetchTimeMillis != null, "fetchTimeMillis should not be null", fetchTimeMillis);
        // ValidationUtils.validateAssertCondition(charsetDetected != null, "charsetDetected should not be null", charsetDetected);
        // ValidationUtils.validateAssertCondition(languageMetadata != null, "languageMetadata should not be null", languageMetadata);
        return new WarcMetadataDoc(fetchTimeMillis, charsetDetected, languageMetadata == null ? null : languageMetadata.reliable, languageMetadata == null ? null : languageMetadata.textBytes, languageMetadata == null ? null : languageMetadata.languageStats);
    }

    public static class WarcMetadataRecordKeyValuePair {
        private final String name;
        private final String value;

        @JsonCreator
        public WarcMetadataRecordKeyValuePair(@JsonProperty("Name") String name , @JsonProperty("Value") String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WarcMetadataRecordKeyValuePair that = (WarcMetadataRecordKeyValuePair) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name, value);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WarcMetadataRecordKeyValuePair{");
            sb.append("name='").append(name).append('\'');
            sb.append(", value='").append(value).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class WarcMetadataRecordList {
        private final List<WarcMetadataRecordKeyValuePair> metadataRecords;

        @JsonCreator
        public WarcMetadataRecordList(@JsonProperty("Metadata-Records") List<WarcMetadataRecordKeyValuePair> metadataRecords) {
            this.metadataRecords = metadataRecords;
        }

        public List<WarcMetadataRecordKeyValuePair> getMetadataRecords() {
            return metadataRecords;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WarcMetadataRecordList that = (WarcMetadataRecordList) o;
            return Objects.equals(metadataRecords, that.metadataRecords);
        }

        @Override
        public int hashCode() {

            return Objects.hash(metadataRecords);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WarcMetadataRecordList{");
            sb.append("metadataRecords=").append(metadataRecords);
            sb.append('}');
            return sb.toString();
        }

        public WarcMetadataDoc warcMetadataDocFromWarcMetadataRecordList() throws Exception {
            ValidationUtils.validateAssertCondition(this.getMetadataRecords() != null && this.getMetadataRecords().size() > 0, "warcMetadataRecords size should be greater than zero", this);
            Long fetchTimeMillis = null;
            String charsetDetected = null;
            String reliable = null;
            String textBytes = null;
            WarcMetadataLanguageMetadata languageMetadata = null;
            for (WarcMetadataRecordKeyValuePair keyValuePair : this.getMetadataRecords()) {
                String keyName = keyValuePair.getName();
                if (keyName.equalsIgnoreCase("fetchTimeMs")) {
                    fetchTimeMillis = Long.parseLong(keyValuePair.getValue());
                } else if (keyName.equalsIgnoreCase("charset-detected")) {
                    charsetDetected = keyValuePair.getValue();
                } else if (keyName.equalsIgnoreCase("languages-cld2")) {
                    String unescapedQuotesValue = keyValuePair.getValue().replace("\\\"", "");
                    unescapedQuotesValue = unescapedQuotesValue.replace("'", "");
                    languageMetadata = objectMapper.readValue(unescapedQuotesValue, WarcMetadataLanguageMetadata.class);
                } else {
                    logger.error("unknown keyname in warcMetadataDocFromWarcMetadataRecordList - keyName: {}", keyName);
                    throw new RuntimeException("unknown keyname in warcMetadataDocFromWarcMetadataRecordList - keyName: "+keyName);
                }
            }
            ValidationUtils.validateAssertCondition(fetchTimeMillis != null, "fetchTimeMillis should not be null", fetchTimeMillis);
            if (languageMetadata == null) {
                // logger.warn("languageMetadata is null - metadataRecords: {}", this.getMetadataRecords());
                logger.info("languageMetadata is null - metadataRecords: {}", this.getMetadataRecords());
                // throw new RuntimeException("languageMetadata is null");
            }
            return new WarcMetadataDoc(fetchTimeMillis, charsetDetected == null ? null : Charset.forName(charsetDetected), languageMetadata == null ? null : languageMetadata.reliable, languageMetadata == null ? null : languageMetadata.textBytes, languageMetadata == null ? null : languageMetadata.languageStats);

        }
    }


    public static class WarcMetadataLanguageMetadata {
        boolean reliable;
        long textBytes;
        List<LanguageStats> languageStats;

        @JsonCreator
        public WarcMetadataLanguageMetadata(@JsonProperty("reliable") boolean reliable, @JsonProperty("text-bytes") long textBytes, @JsonProperty("languages") List<LanguageStats> languageStats) {
            this.reliable = reliable;
            this.textBytes = textBytes;
            this.languageStats = languageStats;
        }

        public boolean isReliable() {
            return reliable;
        }

        public void setReliable(boolean reliable) {
            this.reliable = reliable;
        }

        public long getTextBytes() {
            return textBytes;
        }

        public void setTextBytes(long textBytes) {
            this.textBytes = textBytes;
        }

        public List<LanguageStats> getLanguageStats() {
            return languageStats;
        }

        public void setLanguageStats(List<LanguageStats> languageStats) {
            this.languageStats = languageStats;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WarcMetadataLanguageMetadata that = (WarcMetadataLanguageMetadata) o;
            return reliable == that.reliable &&
                    textBytes == that.textBytes &&
                    Objects.equals(languageStats, that.languageStats);
        }

        @Override
        public int hashCode() {

            return Objects.hash(reliable, textBytes, languageStats);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("WarcMetadataLanguageMetadata{");
            sb.append("reliable=").append(reliable);
            sb.append(", textBytes=").append(textBytes);
            sb.append(", languageStats=").append(languageStats);
            sb.append('}');
            return sb.toString();
        }
    }
}