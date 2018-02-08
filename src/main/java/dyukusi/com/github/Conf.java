package dyukusi.com.github;

import java.util.HashMap;

public class Conf {
    public static final String[] TEXT_TO_SPEECH_NG_WORDS = {
            "http",
    };

    // NG word, alternative word
    public static final HashMap<String, String> SPEAK_NG_WORD_MAP = new HashMap<String, String>() {
        {put("http", "URL");}
    };

    public static final HashMap<String, Language> LANGUAGE_CODE_TO_LANGUAGE_MAP = new HashMap<String, Language>() {
        {put("en", Language.ENGLISH);}
        {put("ja", Language.JAPANESE);}
    };
}
