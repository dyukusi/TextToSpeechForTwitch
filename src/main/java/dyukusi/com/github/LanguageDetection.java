package dyukusi.com.github;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;

import java.util.List;

public class LanguageDetection {
    static Language detect(String text) {
        DetectLanguage.apiKey = "46219074c47d52732e1fadb6da9cf3aa";
        List<Result> results = null;
        try {
            results = DetectLanguage.detect(text);
        } catch (APIError apiError) {
            apiError.printStackTrace();
        }

        // if couldnt detect any language, regard as English
        if (results.size() <= 0) { return Language.ENGLISH; }

        Result result = results.get(0);

        // regarding languages without japanese as English
        return result.language.toString().equalsIgnoreCase("ja") ? Language.JAPANESE : Language.ENGLISH;
    }
}
