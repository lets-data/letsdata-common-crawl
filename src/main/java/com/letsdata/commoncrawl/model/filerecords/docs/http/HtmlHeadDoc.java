package com.letsdata.commoncrawl.model.filerecords.docs.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/*
"Head": {
    "Link": [
        {
            "path": "LINK@/href",
            "rel": "shortcut icon",
            "url": "/favicon.ico"
        },
        {
            "path": "LINK@/href",
            "rel": "stylesheet",
            "type": "text/css",
            "url": "http://fonts.googleapis.com/css?family=Sofia"
        }
    ],
    "Metas": [
        {
            "content": "text/html; charset=shift_JIS",
            "http-equiv": "content-type"
        },
        {
            "content": "no-cache",
            "http-equiv": "Cache-control"
        },
        {
            "content": "application/xhtml+xml; charset=Shift_JIS",
            "http-equiv": "Content-Type"
        },
        {
            "content": "no-cache",
            "http-equiv": "Pragma"
        },
        {
            "content": "no-cache",
            "http-equiv": "Cache-Control"
        },
        {
            "content": "noindex, follow",
            "name": "robots"
        },
        {
            "content": "none,noindex,nofollow,noarchive",
            "name": "robots"
        },
        {
            "content": "noindex,nofollow,noarchive",
            "name": "robots"
        },
        {
            "content": "none",
            "name": "robots"
        },
        {
            "content": "NODIFF,NOINDEX",
            "name": "HATENA"
        },
        {
            "content": "NOINDEX,NOFOLLOW",
            "name": "GOOGLEBOT"
        },
        {
            "content": "NOARCHIVE",
            "name": "GOOGLEBOT"
        },
        {
            "content": "NOSNIPPET",
            "name": "GOOGLEBOT"
        },
        {
            "content": "no-cache",
            "http-equiv": "Pragma"
        },
        {
            "content": "no-cache",
            "http-equiv": "Cache-Control"
        }
    ],
    "Scripts": [
        {
            "path": "SCRIPT@/src",
            "type": "text/javascript",
            "url": "http://adm.shinobi.jp/st/na.js"
        }
    ],
    "Title": "ranking(No.1 \u2192 No.10)"
},
* */
public class HtmlHeadDoc extends HtmlDoc {
    private final List<HtmlLinkDoc> links;
    // private final Map<HtmlHeadMetasDoc.HtmlHeadMetasKey, HtmlHeadMetasDoc> headMetas;
    private final List<HtmlHeadMetasDoc.HtmlHeadMetasKeyValuePair> headMetas;
    private final List<HtmlScriptDoc> scripts;
    private final String title;
    private final String description;
    private final String base;

    public HtmlHeadDoc(@JsonProperty("Link") List<HtmlLinkDoc> links, @JsonProperty("Metas") List<HtmlHeadMetasDoc.HtmlHeadMetasKeyValuePair> headMetasKeyValuePairs, @JsonProperty("Scripts") List<HtmlScriptDoc> scripts, @JsonProperty("Title") String title, @JsonProperty("Description") String description, @JsonProperty("Base") String base) {
        this.links = links;
        //this.headMetas = new HashMap<>();
        this.headMetas = headMetasKeyValuePairs;
        this.scripts = scripts;
        this.title = title;
        this.description = description;
        this.base = base;
        /*for (HtmlHeadMetasDoc.HtmlHeadMetasKeyValuePair keyValuePair : headMetasKeyValuePairs) {
            if (!headMetas.containsKey(keyValuePair.getHttpMetasKey())) {
                headMetas.put(keyValuePair.getHttpMetasKey(), new HtmlHeadMetasDoc(keyValuePair.getHttpMetasKey(), new ArrayList<>()));
            }
            headMetas.get(keyValuePair.getHttpMetasKey()).appendValue(keyValuePair.getHttpMetasKey(), keyValuePair.getContent());
        }*/
    }

    public List<HtmlLinkDoc> getLinks() {
        return links;
    }

    /*public Map<HtmlHeadMetasDoc.HtmlHeadMetasKey, HtmlHeadMetasDoc> getHeadMetas() {
        return headMetas;
    }*/

    public List<HtmlHeadMetasDoc.HtmlHeadMetasKeyValuePair> getHeadMetas() {
        return headMetas;
    }

    public List<HtmlScriptDoc> getScripts() {
        return scripts;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlHeadDoc that = (HtmlHeadDoc) o;
        return Objects.equals(links, that.links) &&
                Objects.equals(headMetas, that.headMetas) &&
                Objects.equals(scripts, that.scripts) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(base, that.base);
    }

    @Override
    public int hashCode() {

        return Objects.hash(links, headMetas, scripts, title, description, base);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HtmlHeadDoc{");
        sb.append("links=").append(links);
        sb.append(", headMetas=").append(headMetas);
        sb.append(", scripts=").append(scripts);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", base='").append(base).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
