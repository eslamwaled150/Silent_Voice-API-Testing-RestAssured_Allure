package Pages;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Voice {

    private String language;

    public Voice() {}

    public Voice(String language) {
        this.language = language;
    }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}