package com.letsdata.commoncrawl.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.resonance.letsdata.data.util.StringFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LanguageStats implements Comparable<LanguageStats> {
    private static final Logger logger = LoggerFactory.getLogger(LanguageStats.class);

    private final Language language;
    private final String code;
    private final double textCovered;
    private final double score;

    public LanguageStats(Language language, double textCovered, double score) {
        this.language = language;
        this.textCovered = textCovered;
        this.score = score;
        this.code = language.getCode();
    }

    @JsonCreator
    public LanguageStats(@JsonProperty("code") String code, @JsonProperty("code-iso-639-3") String codeISO, @JsonProperty("text-covered") double textCovered, @JsonProperty("score") double score, @JsonProperty("name") String name) {
        this.language = Language.fromCode(code);
        this.code = code;
        this.textCovered = textCovered;
        this.score = score;
        if (language == null) {
            logger.debug("language is null - code: {} ", code, codeISO, name);
        }

        if (language != null) {
            StringFunctions.validateStringsAreEqual(language.getIsoCode(), codeISO, "language iso code");
            StringFunctions.validateStringsAreEqualIgnoreCase(language.getName(), name, "language name");
        }
    }

    public Language getLanguage() {
        return language;
    }

    public double getTextCovered() {
        return textCovered;
    }

    public double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageStats that = (LanguageStats) o;
        return Double.compare(that.textCovered, textCovered) == 0 &&
                Double.compare(that.score, score) == 0 &&
                Objects.equals(that.code, code) &&
                language == that.language;
    }

    @Override
    public int hashCode() {

        return Objects.hash(language, textCovered, score, code);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LanguageStats{");
        sb.append("language=").append(language);
        sb.append(", textCovered=").append(textCovered);
        sb.append(", score=").append(score);
        sb.append(", code=").append(code);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(LanguageStats other) {
        if (other == null) {
            return 1;
        }

        int textCoveredComparison = Double.compare(this.textCovered, other.textCovered);
        if (textCoveredComparison == 0) {
            int scoreComparison = Double.compare(this.score, other.score);
            return scoreComparison;
        } else {
            return textCoveredComparison;
        }
    }
}
