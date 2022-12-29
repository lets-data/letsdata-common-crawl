package com.letsdata.commoncrawl.model.filerecords.docs.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class HtmlScriptDoc {

    private final String path;
    private final String type;
    private final String url;

    /*
    {
        "path": "SCRIPT@/src",
        "type": "text/javascript",
        "url": "http://adm.shinobi.jp/st/na.js"
    }
    * */
    public HtmlScriptDoc(@JsonProperty("path") String path, @JsonProperty("type") String type, @JsonProperty("url") String url) {
        this.path = path;
        this.type = type;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlScriptDoc that = (HtmlScriptDoc) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(type, that.type) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(path, type, url);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HtmlScriptDoc{");
        sb.append("path='").append(path).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
