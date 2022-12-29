package com.letsdata.commoncrawl.model.filerecords.docs.watmetadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.letsdata.commoncrawl.model.filerecords.docs.http.HtmlHeadMetasDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.http.HtmlMetadataDoc;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.WarcResponseWarcEnvelope;
import com.letsdata.commoncrawl.model.filerecords.docs.watmetadata.envelope.payloadmetadata.WarcResponsePayloadMetadata;
import com.letsdata.commoncrawl.model.filerecords.types.DocumentRecordTypes;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class WatMetadataWarcResponseDoc extends WatMetadataDoc {
    /*
    {
        "Container": {
            "Compressed": true,
            "Filename": "CC-MAIN-20200702045758-20200702075758-00000.warc.gz",
            "Gzip-Metadata": {
                "Deflate-Length": "1877",
                "Footer-Length": "8",
                "Header-Length": "10",
                "Inflated-CRC": "-1535210076",
                "Inflated-Length": "5758"
            },
            "Offset": "52557"
        },
        "WarcEnvelope": {
            "Format": "WARC",
            "Payload-Metadata": {
                "Actual-Content-Length": "5149",
                "Actual-Content-Type": "application/http; msgtype=response",
                "Block-Digest": "sha1:VSQOTNLEMFURZUXHVVRYNYHGDTPZG6PW",
                "HTTP-Response-Metadata": {
                    "Entity-Digest": "sha1:ZA4S2ROKJQBBZUB7HNCIF4B343CJJLNW",
                    "Entity-Length": "4951",
                    "Entity-Trailing-Slop-Length": "0",
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
                                    "url": "https://fonts.googleapis.com/css?family=Crimson+Text|Muli"
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
                    "Headers": {
                        "Accept-Ranges": "bytes",
                        "Connection": "close",
                        "Content-Length": "4951",
                        "Content-Type": "text/html",
                        "Date": "Thu, 02 Jul 2020 05:22:05 GMT",
                        "Server": "Apache",
                        "X-Crawler-Transfer-Encoding": "chunked"
                    },
                    "Headers-Length": "198",
                    "Response-Message": {
                        "Reason": "OK",
                        "Status": "200",
                        "Version": "HTTP/1.1"
                    }
                },
                "Trailing-Slop-Length": "4"
            },
            "WARC-Header-Length": "605",
            "WARC-Header-Metadata": {
                "Content-Length": "5149",
                "Content-Type": "application/http; msgtype=response",
                "WARC-Block-Digest": "sha1:VSQOTNLEMFURZUXHVVRYNYHGDTPZG6PW",
                "WARC-Concurrent-To": "<urn:uuid:0d4689dd-62bc-4a12-95b8-7c79859f389e>",
                "WARC-Date": "2020-07-02T05:22:10Z",
                "WARC-IP-Address": "210.140.45.105",
                "WARC-Identified-Payload-Type": "text/html",
                "WARC-Payload-Digest": "sha1:ZA4S2ROKJQBBZUB7HNCIF4B343CJJLNW",
                "WARC-Record-ID": "<urn:uuid:355cc15b-45ff-4350-a325-4b7e857abccd>",
                "WARC-Target-URI": "http://01.rknt.jp/rank.php?page=0&id=m5130&genre=607835",
                "WARC-Type": "response",
                "WARC-Warcinfo-ID": "<urn:uuid:ca9d6252-f97b-44ea-bd88-3044955099cf>"
            }
        }
    }
    */

    @JsonCreator
    public WatMetadataWarcResponseDoc(@JsonProperty("Container") Container container, @JsonProperty("Envelope") WarcResponseWarcEnvelope envelope) {
        super(DocumentRecordTypes.WAT_METADATA_WARC_RESPONSE_PAYLOAD, container, envelope);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WatMetadataWarcResponseDoc{");
        sb.append("container=").append(container);
        sb.append(", envelope=").append(envelope);
        sb.append('}');
        return sb.toString();
    }

    public String getTitle() {
        if (this.envelope != null && this.envelope.getPayloadMetadata() != null && ((WarcResponsePayloadMetadata)this.envelope.getPayloadMetadata()).getHtmlMetadataDoc() != null ) {
            HtmlMetadataDoc htmlMetadataDoc = ((WarcResponsePayloadMetadata)((WarcResponseWarcEnvelope)this.envelope).getPayloadMetadata()).getHtmlMetadataDoc();
            if (htmlMetadataDoc != null && htmlMetadataDoc.getHtmlHeadDoc() != null) {
                return htmlMetadataDoc.getHtmlHeadDoc().getTitle();
            }
        }
        return null;
    }

    public String getDescription() {
        if (this.envelope != null && this.envelope.getPayloadMetadata() != null && ((WarcResponsePayloadMetadata)this.envelope.getPayloadMetadata()).getHtmlMetadataDoc() != null ) {
            HtmlMetadataDoc htmlMetadataDoc = ((WarcResponsePayloadMetadata)((WarcResponseWarcEnvelope)this.envelope).getPayloadMetadata()).getHtmlMetadataDoc();
            if (htmlMetadataDoc != null && htmlMetadataDoc.getHtmlHeadDoc() != null) {
                return htmlMetadataDoc.getHtmlHeadDoc().getDescription();
            }
        }
        return null;
    }

    public HtmlHeadMetaValues getHtmlHeadMetaValues() {
        if (this.envelope != null && this.envelope.getPayloadMetadata() != null && ((WarcResponsePayloadMetadata)this.envelope.getPayloadMetadata()).getHtmlMetadataDoc() != null ) {
            HtmlMetadataDoc htmlMetadataDoc = ((WarcResponsePayloadMetadata)((WarcResponseWarcEnvelope)this.envelope).getPayloadMetadata()).getHtmlMetadataDoc();
            if (htmlMetadataDoc.getHtmlHeadDoc() != null && htmlMetadataDoc.getHtmlHeadDoc().getHeadMetas() != null) {
                List<HtmlHeadMetasDoc.HtmlHeadMetasKeyValuePair> headMetasList = htmlMetadataDoc.getHtmlHeadDoc().getHeadMetas();
                String keywords = null;
                String icon = null;
                String canonical = null;
                String og_url = null;
                String og_site_name = null;
                String og_type = null;
                String og_image = null;
                String og_title = null;
                String og_description = null;
                String contentType = null;
                if (headMetasList != null) {
                    for (HtmlHeadMetasDoc.HtmlHeadMetasKeyValuePair headMeta : headMetasList) {
                        if (!StringUtils.isBlank(headMeta.getName()) && headMeta.getName().equalsIgnoreCase("keywords") && !StringUtils.isBlank(headMeta.getContent())) {
                            keywords = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getRel()) && headMeta.getRel().equalsIgnoreCase("icon") && !StringUtils.isBlank(headMeta.getContent())) {
                            // <link rel="icon" href="https://theresonancelabs.s3.amazonaws.com/letsresonatewebsite/downloadresonance128x128.png">
                            icon = headMeta.getContent(); // TODO href in content?
                        } else if (!StringUtils.isBlank(headMeta.getRel()) && headMeta.getRel().equalsIgnoreCase("canonical") && !StringUtils.isBlank(headMeta.getContent())) {
                            // <link rel="canonical" href="https://letsresonate.net/index.html">
                            canonical = headMeta.getContent(); // TODO href in content?
                        } else if (!StringUtils.isBlank(headMeta.getProperty()) && headMeta.getProperty().equalsIgnoreCase("og:url") && !StringUtils.isBlank(headMeta.getContent())) {
                            // <meta property="og:url"                 content="https://letsresonate.net/index.html" />
                            og_url = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getProperty()) && headMeta.getProperty().equalsIgnoreCase("og:site_name") && !StringUtils.isBlank(headMeta.getContent())) {
                            og_site_name = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getProperty()) && headMeta.getProperty().equalsIgnoreCase("og:type") && !StringUtils.isBlank(headMeta.getContent())) {
                            og_type = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getProperty()) && headMeta.getProperty().equalsIgnoreCase("og:image") && !StringUtils.isBlank(headMeta.getContent())) {
                            og_image = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getProperty()) && headMeta.getProperty().equalsIgnoreCase("og:title") && !StringUtils.isBlank(headMeta.getContent())) {
                            og_title = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getProperty()) && headMeta.getProperty().equalsIgnoreCase("og:description") && !StringUtils.isBlank(headMeta.getContent())) {
                            og_description = headMeta.getContent();
                        } else if (!StringUtils.isBlank(headMeta.getHttpEquiv()) && headMeta.getHttpEquiv().equalsIgnoreCase("content-type") && !StringUtils.isBlank(headMeta.getContent())) {
                            contentType = headMeta.getContent();
                        }
                    }
                }

                return new HtmlHeadMetaValues(keywords, icon, canonical, og_url, og_site_name, og_type, og_image, og_title, og_description, contentType);
            }
        }
        return null;
    }

    public static class HtmlHeadMetaValues {
        private final String keywords;
        private final String icon;
        private final String canonical;
        private final String og_url;
        private final String og_site_name;
        private final String og_type;
        private final String og_image;
        private final String og_title;
        private final String og_description;
        private final String contentType;

        public HtmlHeadMetaValues(String keywords, String icon, String canonical, String og_url, String og_site_name, String og_type, String og_image, String og_title, String og_description, String contentType) {
            this.keywords = keywords;
            this.icon = icon;
            this.canonical = canonical;
            this.og_url = og_url;
            this.og_site_name = og_site_name;
            this.og_type = og_type;
            this.og_image = og_image;
            this.og_title = og_title;
            this.og_description = og_description;
            this.contentType = contentType;
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
            return contentType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HtmlHeadMetaValues that = (HtmlHeadMetaValues) o;
            return Objects.equals(keywords, that.keywords) &&
                    Objects.equals(icon, that.icon) &&
                    Objects.equals(canonical, that.canonical) &&
                    Objects.equals(og_url, that.og_url) &&
                    Objects.equals(og_site_name, that.og_site_name) &&
                    Objects.equals(og_type, that.og_type) &&
                    Objects.equals(og_image, that.og_image) &&
                    Objects.equals(og_title, that.og_title) &&
                    Objects.equals(og_description, that.og_description) &&
                    Objects.equals(contentType, that.contentType);
        }

        @Override
        public int hashCode() {

            return Objects.hash(keywords, icon, canonical, og_url, og_site_name, og_type, og_image, og_title, og_description, contentType);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("HtmlHeadMetaValues{");
            sb.append("keywords='").append(keywords).append('\'');
            sb.append(", icon='").append(icon).append('\'');
            sb.append(", canonical='").append(canonical).append('\'');
            sb.append(", og_url='").append(og_url).append('\'');
            sb.append(", og_site_name='").append(og_site_name).append('\'');
            sb.append(", og_type='").append(og_type).append('\'');
            sb.append(", og_image='").append(og_image).append('\'');
            sb.append(", og_title='").append(og_title).append('\'');
            sb.append(", og_description='").append(og_description).append('\'');
            sb.append(", contentType='").append(contentType).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}