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

    @JsonCreator
    public IndexRecord(@JsonProperty("url") String url, @JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("keywords") String keywords, @JsonProperty("icon") String icon, @JsonProperty("canonical") String canonical, @JsonProperty("og_url") String og_url, @JsonProperty("og_site_name") String og_site_name, @JsonProperty("og_type") String og_type, @JsonProperty("og_image") String og_image, @JsonProperty("og_title") String og_title, @JsonProperty("og_description") String og_description, @JsonProperty("contentType") String contentType, @JsonProperty("reliable") Boolean reliable, @JsonProperty("textBytes") Long textBytes, @JsonProperty("languages") List<LanguageStats> languages, @JsonProperty("docTextContentLength") Long docTextContentLength, @JsonProperty("docText") String docText, @JsonProperty("charset") String charset) {
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

    @JsonIgnore
    public AbstractMap.SimpleEntry<String, ByteBuffer> getPartitionKeyAndDataBytes() throws Exception {
        String partitionKey = getPartitionKey();
        byte[] dataArr = objectMapper.writeValueAsString(this).getBytes("utf-8");
        ByteArrayOutputStream byteArrayOutputStream = null;
        GZIPOutputStream gzipOutputStream = null;
        byte[] dataArrCompressed = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(dataArr, 0, dataArr.length);
            gzipOutputStream.flush();
            gzipOutputStream.finish();
            dataArrCompressed = byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            logger.error("IOException in compressing the kinesis data record - url: "+url, ex);
            throw new RuntimeException("IOException in compressing the kinesis data record");
        } finally {
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }

            if (gzipOutputStream != null) {
                gzipOutputStream.close();
            }
        }

        if (dataArrCompressed == null || dataArrCompressed.length > 1000*1000) {
            logger.error("kinesis record data is greater than 1 MB - url: {}, size: {}", url, dataArr.length);
            throw new RuntimeException("kinesis record data is greater than 1 MB");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(dataArrCompressed);
        return new AbstractMap.SimpleEntry<>(partitionKey, byteBuffer);
    }

    /*public static IndexRecord getIndexRecordFromKinesisRecord(Record record, byte[] byteArrDecompressed) throws Exception {
        ByteBuffer dataBuffer = record.getData();
        IndexRecord indexRecord = null;
        ByteArrayInputStream byteArrayInputStream = null;
        GZIPInputStream gzipInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(dataBuffer.array(), dataBuffer.position(), dataBuffer.limit()-dataBuffer.position());
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            int bytesRead = 0;
            int len = 0;
            while ((bytesRead = gzipInputStream.read(byteArrDecompressed, len, byteArrDecompressed.length-len)) > 0) {
                len+=bytesRead;
            }
            indexRecord = objectMapper.readValue(byteArrDecompressed, 0, len, IndexRecord.class);
        } finally {
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }

            if (gzipInputStream != null) {
                gzipInputStream.close();
            }
        }

        ValidationUtils.validateAssertCondition(indexRecord != null, "indexRecord should not be null");
        return indexRecord;
    }*/

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
        return this.getUrl();
    }

    @Override
    public String getRecordType() {
        return "IndexRecord";
    }

    @Override
    public Map<String, Object> getDocumentMetadata() {
        return new HashMap<>();
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
        try {
            String partitionKey = url.length() > 256 ? AbstractWARCRecord.encodeBase64(AbstractWARCRecord.computeMD5Hash(url)) : url;
            if (partitionKey.length() > 256) {
                logger.error("partitionKey is greater than max allowed length - url: {}, partitionKey: {}", url, partitionKey);
                throw new RuntimeException("partitionKey is greater than max allowed length");
            }
            return partitionKey;
        } catch (Exception ex) {
            throw new RuntimeException("getPartitionKey threw exception", ex);
        }
    }

    @Override
    public boolean isSingleDoc() {
        return false;
    }
}
