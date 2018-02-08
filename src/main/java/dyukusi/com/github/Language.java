package dyukusi.com.github;

public enum Language {
    ENGLISH("en"),
    JAPANESE("ja"),
    OTHERS("null")
    ;

    private final String microsoftTranlateTextLanguageCode;

    Language(final String msLangCode) {
        this.microsoftTranlateTextLanguageCode = msLangCode;
    }

    public String getMicrosoftTranlateTextLanguageCode() {
        return microsoftTranlateTextLanguageCode;
    }
}
