package com.letsdata.commoncrawl.model.filerecords.docs.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class HtmlMetadataDoc extends HtmlDoc {
    private final HtmlHeadDoc htmlHeadDoc;
    private final List<HtmlLinkDoc> links;

    /*
    "HTML-Metadata": {
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
        "Links": [
            {
                "path": "A@/href",
                "text": "\u6d77\u8cca\u5922RANK",
                "title": "\u6d77\u8cca\u5922RANK",
                "url": "http://01.rknt.jp/link.php?id=opdreamrank&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "\u8d64\u9aea\u6d77\u8cca\u56e3Rank!",
                "title": "\u8d64\u9aea\u6d77\u8cca\u56e3Rank!",
                "url": "http://01.rknt.jp/link.php?id=redredred&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "\u71b1\u5e2f\u9b5a",
                "title": "\u71b1\u5e2f\u9b5a",
                "url": "http://01.rknt.jp/link.php?id=48c100506&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "\u4e0d\u6b7b\u9ce5\u306e\u80cc\u4e2d",
                "title": "\u4e0d\u6b7b\u9ce5\u306e\u80cc\u4e2d",
                "url": "http://01.rknt.jp/link.php?id=marco05&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "\u6d77\u8cca\u65d7\u3092\u63b2\u3052\u3066",
                "title": "\u6d77\u8cca\u65d7\u3092\u63b2\u3052\u3066",
                "url": "http://01.rknt.jp/link.php?id=kaizokud&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "vase",
                "title": "vase",
                "url": "http://01.rknt.jp/link.php?id=vase0509&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "RED RANK",
                "title": "RED RANK",
                "url": "http://01.rknt.jp/link.php?id=flame0red&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "\u4fe1\u53f7\u6a5f",
                "title": "\u4fe1\u53f7\u6a5f",
                "url": "http://01.rknt.jp/link.php?id=jdtp&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "OP*BLD\uff97\uff9d\uff77\uff9d\uff78\uff9e",
                "title": "OP*BLD\uff97\uff9d\uff77\uff9d\uff78\uff9e",
                "url": "http://01.rknt.jp/link.php?id=opbldr&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "\u72c2\u3063\u305f\u304a\u8336\u4f1a",
                "title": "\u72c2\u3063\u305f\u304a\u8336\u4f1a",
                "url": "http://01.rknt.jp/link.php?id=amtp&table=m5130"
            },
            {
                "path": "A@/href",
                "text": "No.11 \u2192 No.14",
                "title": "ranking(No.11 \u2192 No.14)",
                "url": "./rank.php?page=1&id=m5130&genre=607835"
            },
            {
                "path": "A@/href",
                "text": "back",
                "url": "m5130/"
            }
        ]
    },
    * */
    public HtmlMetadataDoc(@JsonProperty("Head") HtmlHeadDoc htmlHeadDoc, @JsonProperty("Links") List<HtmlLinkDoc> links) {
        this.htmlHeadDoc = htmlHeadDoc;
        this.links = links;
    }

    public HtmlHeadDoc getHtmlHeadDoc() {
        return htmlHeadDoc;
    }

    public List<HtmlLinkDoc> getLinks() {
        return links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlMetadataDoc that = (HtmlMetadataDoc) o;
        return Objects.equals(htmlHeadDoc, that.htmlHeadDoc) &&
                Objects.equals(links, that.links);
    }

    @Override
    public int hashCode() {

        return Objects.hash(htmlHeadDoc, links);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HtmlMetadataDoc{");
        sb.append("htmlHeadDoc=").append(htmlHeadDoc);
        sb.append(", links=").append(links);
        sb.append('}');
        return sb.toString();
    }
}
