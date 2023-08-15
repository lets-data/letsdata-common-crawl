package com.letsdata.commoncrawl.interfaces.implementations.documents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsdata.commoncrawl.model.ContentType;
import com.letsdata.commoncrawl.model.Language;
import com.letsdata.commoncrawl.model.LanguageStats;
import com.letsdata.commoncrawl.model.filerecords.warc.AbstractWARCRecord;
import com.resonance.letsdata.data.documents.interfaces.SingleDocInterface;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class IndexRecord implements SingleDocInterface {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(IndexRecord.class);
    private final String url;
    // <title>Resonance - a music social network - Discover, listen, share and play</title>
    private final String title;
    // <meta name="description"                content="Resonance is an iOS social networking app for music. Share & personalize new music discovery from friends & family. Discover, listen, share & play - Let's Resonate!">
    private final String description;
    // <meta name="keywords" content="Resonance, resonate, music, social, network, discover, listen, share, play, streaming, online, album, artist, playlist, song, friends, family, apple">
    private final String keywords;
    // <link rel="icon" href="https://theresonancelabs.s3.amazonaws.com/letsresonatewebsite/downloadresonance128x128.png">
    private final String icon;
    // <link rel="canonical" href="https://letsresonate.net/index.html">
    private final String canonical;
    // <meta property="og:url"                 content="https://letsresonate.net/index.html" />
    private final String og_url;
    // <meta property="og:site_name"           content="Lets Resonate"/>
    private final String og_site_name;
    // <meta property="og:type"                content="article"/>
    private final String og_type;
    // <meta property="og:image"               content="https://theresonancelabs.s3.amazonaws.com/letsresonatewebsite/WebsiteHeaderLowRes.png" />
    private final String og_image;
    // <meta property="og:title"               content="Resonance - a music social network - Discover, listen, share and play - Let's Resonate!" />
    private final String og_title;
    // <meta property="og:description"         content="Resonance is an iOS social networking app for music. Share & personalize new music discovery from friends & family. Discover, listen, share & play - Let's Resonate!" />
    private final String og_description;
    private final ContentType contentType;
    private final Boolean reliable;
    private final Long textBytes;
    private final List<LanguageStats> languages;
    private final Long docTextContentLength;
    private final String docText;
    private final String charset;
    private final String documentId;
    private final String recordType;
    private final String partitionKey;
    private final boolean isSingleDoc;
    private final Map<String, Object> documentMetadata;


    public IndexRecord(String url, String title, String description, String keywords, String icon, String canonical, String og_url, String og_site_name, String og_type, String og_image, String og_title, String og_description,
                       String contentType, Boolean reliable, Long textBytes, List<LanguageStats> languages, Long docTextContentLength, String docText, String charset) {
        this.documentId = url;
        this.partitionKey = url;
        this.recordType = "IndexRecord";
        this.isSingleDoc = true;
        this.documentMetadata = new HashMap<>();
        this.url = url;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.icon = icon;
        this.canonical = canonical;
        this.og_url = og_url;
        this.og_site_name = og_site_name;
        this.og_type = og_type;
        this.og_image = og_image;
        this.og_title = og_title;
        this.og_description = og_description;
        this.contentType = StringUtils.isBlank(contentType) ? null : ContentType.fromString(contentType);
        this.reliable = reliable;
        this.textBytes = textBytes;
        this.languages = languages;
        this.docTextContentLength = docTextContentLength;
        this.docText = docText;
        this.charset = charset;
    }

    @JsonCreator
    public IndexRecord(@JsonProperty("documentId") String documentId, @JsonProperty("recordType") String recordType, @JsonProperty("partitionKey") String partitionKey, @JsonProperty("singleDoc") Boolean isSingleDoc, @JsonProperty("documentMetadata") Map<String, Object> documentMetadata,
                       @JsonProperty("url") String url, @JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("keywords") String keywords, @JsonProperty("icon") String icon,
                       @JsonProperty("canonical") String canonical, @JsonProperty("og_url") String og_url, @JsonProperty("og_site_name") String og_site_name, @JsonProperty("og_type") String og_type,
                       @JsonProperty("og_image") String og_image, @JsonProperty("og_title") String og_title, @JsonProperty("og_description") String og_description, @JsonProperty("contentType") String contentType,
                       @JsonProperty("reliable") Boolean reliable, @JsonProperty("textBytes") Long textBytes, @JsonProperty("languages") List<LanguageStats> languages, @JsonProperty("docTextContentLength") Long docTextContentLength,
                       @JsonProperty("docText") String docText, @JsonProperty("charset") String charset) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.icon = icon;
        this.canonical = canonical;
        this.og_url = og_url;
        this.og_site_name = og_site_name;
        this.og_type = og_type;
        this.og_image = og_image;
        this.og_title = og_title;
        this.og_description = og_description;
        this.contentType = StringUtils.isBlank(contentType) ? null : ContentType.fromString(contentType);
        this.reliable = reliable;
        this.textBytes = textBytes;
        this.languages = languages;
        this.docTextContentLength = docTextContentLength;
        this.docText = docText;
        this.charset = charset;
        this.documentId = documentId;
        this.partitionKey = partitionKey;
        this.recordType = recordType;
        this.isSingleDoc = isSingleDoc;
        this.documentMetadata = documentMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexRecord that = (IndexRecord) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(keywords, that.keywords) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(canonical, that.canonical) &&
                Objects.equals(og_url, that.og_url) &&
                Objects.equals(og_site_name, that.og_site_name) &&
                Objects.equals(og_type, that.og_type) &&
                Objects.equals(og_image, that.og_image) &&
                Objects.equals(og_title, that.og_title) &&
                Objects.equals(og_description, that.og_description) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(reliable, that.reliable) &&
                Objects.equals(textBytes, that.textBytes) &&
                Objects.equals(languages, that.languages) &&
                Objects.equals(docTextContentLength, that.docTextContentLength) &&
                Objects.equals(docText, that.docText) &&
                Objects.equals(charset, that.charset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title, description, keywords, icon, canonical, og_url, og_site_name, og_type, og_image, og_title, og_description, contentType, reliable, textBytes, languages, docTextContentLength, docText, charset);
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getIcon() {
        return icon;
    }

    public String getCanonical() {
        return canonical;
    }

    public String getOg_url() {
        return og_url;
    }

    public String getOg_site_name() {
        return og_site_name;
    }

    public String getOg_type() {
        return og_type;
    }

    public String getOg_image() {
        return og_image;
    }

    public String getOg_title() {
        return og_title;
    }

    public String getOg_description() {
        return og_description;
    }

    public String getContentType() {
        return contentType == null ? null : contentType.getStrValue();
    }

    public String getCharset() {
        return charset;
    }

    @JsonIgnore
    public ContentType getContentTypeObject() {
        return contentType;
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

    public Long getDocTextContentLength() {
        return docTextContentLength;
    }

    public String getDocText() {
        return docText;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IndexRecord{");
        sb.append("url='").append(url).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", keywords='").append(keywords).append('\'');
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", canonical='").append(canonical).append('\'');
        sb.append(", og_url='").append(og_url).append('\'');
        sb.append(", og_site_name='").append(og_site_name).append('\'');
        sb.append(", og_type='").append(og_type).append('\'');
        sb.append(", og_image='").append(og_image).append('\'');
        sb.append(", og_title='").append(og_title).append('\'');
        sb.append(", og_description='").append(og_description).append('\'');
        sb.append(", contentType=").append(contentType);
        sb.append(", reliable=").append(reliable);
        sb.append(", textBytes=").append(textBytes);
        sb.append(", languages=").append(languages);
        sb.append(", docTextContentLength=").append(docTextContentLength);
        sb.append(", docText='").append(docText).append('\'');
        sb.append(", charset='").append(charset).append('\'');
        sb.append('}');
        return sb.toString();
    }

    // defined for quick testing
    public static void main(String[] args) throws Exception {
        IndexRecord indexRecord = new IndexRecord("http://url", "title", "desscription", "keywords", "icon", "canonical", "ogurl", "ogsitename", "ogtype", "ogimage", "ogtitle", "ogdescription", "text/html", true, 1024L, Arrays.asList(new LanguageStats(Language.ENGLISH, 1.0, 100.0)), 1024L, "docText", "utf-8");
        String json = objectMapper.writeValueAsString(indexRecord);
        System.out.println(json);
        IndexRecord deserialized = objectMapper.readValue(json, IndexRecord.class);
        System.out.println("equals: "+Objects.equals(indexRecord, deserialized));
        String jsonDeserialized = objectMapper.writeValueAsString(deserialized);
        System.out.println(jsonDeserialized);
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public String getRecordType() {
        return recordType;
    }

    @Override
    public Map<String, Object> getDocumentMetadata() {
        return documentMetadata;
    }

    @JsonIgnore
    @Override
    public String serialize() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("getDocumentContents threw exception", e);
        }
    }

    @Override
    public String getPartitionKey() {
        return partitionKey;
    }

    @Override
    public boolean isSingleDoc() {
        return isSingleDoc;
    }
}
