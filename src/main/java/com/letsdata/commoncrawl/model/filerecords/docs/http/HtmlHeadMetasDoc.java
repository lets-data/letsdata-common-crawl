package com.letsdata.commoncrawl.model.filerecords.docs.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resonance.letsdata.data.util.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HtmlHeadMetasDoc {
    private final HtmlHeadMetasKey metaKey;
    private final List<String> values;

    public HtmlHeadMetasDoc(HtmlHeadMetasKey metaKey, List<String> values) {
        this.metaKey = metaKey;
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HtmlHeadMetasDoc that = (HtmlHeadMetasDoc) o;
        return Objects.equals(metaKey, that.metaKey) &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {

        return Objects.hash(metaKey, values);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HtmlHeadMetasDoc{");
        sb.append("metaKey='").append(metaKey).append('\'');
        sb.append(", values=").append(values);
        sb.append('}');
        return sb.toString();
    }

    public HtmlHeadMetasKey getMetaKey() {
        return metaKey;
    }

    public List<String> getValues() {
        return values;
    }

    public void appendValue(HtmlHeadMetasKey metaKey, String value) {
        ValidationUtils.validateAssertCondition(this.metaKey.equals(metaKey), "metaKey for append should be the same");
        values.add(value);
    }

    public static enum HtmlHeadMetasKeyType {
        NAME("name"),
        HTTP_EQUIV("http-equiv");

        private final String value;

        HtmlHeadMetasKeyType(String name) {
            this.value = name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return  value;
        }

        private static final Map<String, HtmlHeadMetasKeyType> valueToEnumMap = new HashMap<>();
        static {
            initMap();
        }

        private static void initMap() {
            if (valueToEnumMap.size() == 0) {
                for (HtmlHeadMetasKeyType htmlHeadMetasKeyType : HtmlHeadMetasKeyType.values()) {
                    if (valueToEnumMap.containsKey(htmlHeadMetasKeyType.value)) {
                        throw new RuntimeException("duplicate enum value");
                    }
                    valueToEnumMap.put(htmlHeadMetasKeyType.value, htmlHeadMetasKeyType);
                }
            }
        }

        public static HtmlHeadMetasKeyType fromValue(String value) {
            if (valueToEnumMap.size() == 0) {
                initMap();
            }

            HtmlHeadMetasKeyType keyType = valueToEnumMap.get(value);
            ValidationUtils.validateAssertCondition(keyType != null, "keyType should not be null");
            return keyType;
        }
    }

    public static class HtmlHeadMetasKey {
        private final String metasKey;
        private final HtmlHeadMetasKeyType metasKeyType;

        public HtmlHeadMetasKey(String metasKey, HtmlHeadMetasKeyType metasKeyType) {
            this.metasKey = metasKey;
            this.metasKeyType = metasKeyType;
        }

        public String getMetasKey() {
            return metasKey;
        }

        public HtmlHeadMetasKeyType getMetasKeyType() {
            return metasKeyType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HtmlHeadMetasKey that = (HtmlHeadMetasKey) o;
            return Objects.equals(metasKey, that.metasKey) &&
                    metasKeyType == that.metasKeyType;
        }

        @Override
        public int hashCode() {

            return Objects.hash(metasKey, metasKeyType);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("HtmlHeadMetasKey{");
            sb.append("metasKey='").append(metasKey).append('\'');
            sb.append(", metasKeyType=").append(metasKeyType);
            sb.append('}');
            return sb.toString();
        }
    }
    public static class HtmlHeadMetasKeyValuePair {
        // private final HtmlHeadMetasKey metasKey;
        private final String name;
        private final String property;
        private final String httpEquiv;
        private final String content;
        private final String rel;

        @JsonCreator
        public HtmlHeadMetasKeyValuePair(@JsonProperty("name") String name, @JsonProperty("property") String property, @JsonProperty("http-equiv") String httpEquiv, @JsonProperty("content") String content, @JsonProperty("rel") String rel) {
            String metasKeyName = name == null ? httpEquiv : name;
            // HtmlHeadMetasKeyType metasKeyType = name == null ? HtmlHeadMetasKeyType.HTTP_EQUIV : HtmlHeadMetasKeyType.NAME;
            // this.metasKey = new HtmlHeadMetasKey(metasKeyName, metasKeyType);
            this.name = name;
            this.property = property;
            this.httpEquiv = httpEquiv;
            this.content = content;
            this.rel = rel;
        }

        /*public HtmlHeadMetasKey getHttpMetasKey() {
            return metasKey;
        }*/

        public String getHttpEquiv() {
            return httpEquiv;
        }

        public String getName() {
            return name;
        }

        public String getProperty() {
            return property;
        }

        public String getContent() {
            return content;
        }

        public String getRel() {
            return rel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HtmlHeadMetasKeyValuePair that = (HtmlHeadMetasKeyValuePair) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(httpEquiv, that.httpEquiv) &&
                    Objects.equals(property, that.property) &&
                    Objects.equals(content, that.content) &&
                    Objects.equals(rel, that.rel);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name, property, httpEquiv, content, rel);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("HtmlHeadMetasKeyValuePair{");
            sb.append("name=").append(name);
            sb.append("property=").append(property);
            sb.append("httpEquiv=").append(httpEquiv);
            sb.append(", content='").append(content).append('\'');
            sb.append(", rel='").append(rel).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
