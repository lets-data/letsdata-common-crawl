package com.letsdata.commoncrawl.model.filerecords.docs.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HtmlLinkDoc extends HtmlDoc {
    public static enum HttpLinkDocRel {
        SHORTCUT_ICON(1, "shortcut_icon"),
        STYLESHEET_ICON(2, "stylesheet");

        private final int httpLinkDocRelId;
        private final String relText;

        HttpLinkDocRel(int httpLinkDocRelId, String relText) {
            this.httpLinkDocRelId = httpLinkDocRelId;
            this.relText = relText;
        }

        public int getHttpLinkDocRelId() {
            return httpLinkDocRelId;
        }

        public String getRelText() {
            return relText;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("HttpLinkDocRel{");
            sb.append("httpLinkDocRelId=").append(httpLinkDocRelId);
            sb.append(", relText='").append(relText).append('\'');
            sb.append('}');
            return sb.toString();
        }

        private static final Map<String, HttpLinkDocRel> textHttpLinkDocRelMap = new HashMap();

        public static HttpLinkDocRel fromText(String text) {
            if (textHttpLinkDocRelMap.size() == 0) {
                for (HttpLinkDocRel currHttpLinkDocRel : HttpLinkDocRel.values()) {
                    if (textHttpLinkDocRelMap.containsKey(currHttpLinkDocRel.relText)) {
                        throw new RuntimeException("Duplicate text in textHttpLinkDocRelMap");
                    }
                    textHttpLinkDocRelMap.put(currHttpLinkDocRel.relText, currHttpLinkDocRel);
                }
            }

            return textHttpLinkDocRelMap.get(text);
        }
    }

    private final String path;
    private final HttpLinkDocRel rel;
    private final String type;
    private final String url;
    private final String text;
    private final String title;
    private final String href;
    private final String hreflang;
    private final String target;
    private final String alt;
    private final String method;

    public HtmlLinkDoc(@JsonProperty("path") String path, @JsonProperty("rel") String rel, @JsonProperty("type") String type, @JsonProperty("url") String url, @JsonProperty("text") String text, @JsonProperty("title") String title, @JsonProperty("href") String href, @JsonProperty("target") String target, @JsonProperty("alt") String alt, @JsonProperty("method") String method, @JsonProperty("hreflang") String hreflang) {
        this.path = path;
        this.rel = HttpLinkDocRel.fromText(rel);
        this.type = type;
        this.url = url;
        this.text = text;
        this.title = title;
        this.href = href;
        this.target = target;
        this.alt = alt;
        this.method = method;
        this.hreflang = hreflang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlLinkDoc that = (HtmlLinkDoc) o;
        return Objects.equals(path, that.path) &&
                rel == that.rel &&
                Objects.equals(type, that.type) &&
                Objects.equals(url, that.url) &&
                Objects.equals(text, that.text) &&
                Objects.equals(title, that.title) &&
                Objects.equals(href, that.href) &&
                Objects.equals(target, that.target) &&
                Objects.equals(alt, that.alt) &&
                Objects.equals(method, that.method) &&
                Objects.equals(hreflang, that.hreflang) ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(path, rel, type, url, text, title, href, target, alt, method, hreflang);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HtmlLinkDoc{");
        sb.append("path='").append(path).append('\'');
        sb.append(", rel=").append(rel);
        sb.append(", type='").append(type).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", href='").append(href).append('\'');
        sb.append(", target='").append(target).append('\'');
        sb.append(", alt='").append(alt).append('\'');
        sb.append(", method='").append(method).append('\'');
        sb.append(", method='").append(hreflang).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getPath() {
        return path;
    }

    public HttpLinkDocRel getRel() {
        return rel;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getTarget() {
        return target;
    }

    public String getAlt() {
        return alt;
    }

    public String getMethod() {
        return method;
    }

    public String getHreflang() {
        return hreflang;
    }
}
