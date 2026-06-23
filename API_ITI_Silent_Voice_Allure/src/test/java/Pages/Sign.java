package Pages;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sign {

    private String sentenceEn;

    public Sign() {}

    public Sign(String sentenceEn) {
        this.sentenceEn = sentenceEn;
    }

    public String getSentenceEn() { return sentenceEn; }
    public void setSentenceEn(String sentenceEn) { this.sentenceEn = sentenceEn; }
}