package dyukusi.com.github;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class MicrosoftTranslateText {
    static String subscriptionKey = "9579a796faf145e98f10385719d46959";
    static String host = "https://api.microsofttranslator.com";
    static String path = "/V2/Http.svc/Translate";

    public static String translate(Language detectedLanguage, String text) throws Exception {
        // ja => en, others => ja
        Language targetLanguage = detectedLanguage.equals(Language.JAPANESE) ? Language.ENGLISH : Language.JAPANESE;

        String encoded_query = URLEncoder.encode (text, "UTF-8");
        String params = "?to=" + targetLanguage.getMicrosoftTranlateTextLanguageCode() + "&text=" + encoded_query;
        URL url = new URL (host + path + params);

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);
        connection.setDoOutput(true);

        StringBuilder response = new StringBuilder ();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        String resultWithXmlTag = response.toString();
        String result = resultWithXmlTag.replaceAll("<.+?>", "");
        return result;
    }
}
