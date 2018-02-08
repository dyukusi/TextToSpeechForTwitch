package dyukusi.com.github;

import com.amazonaws.services.polly.model.VoiceId;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TwitchBot extends PircBot {
    private static String channel;
    SpeakerManager speakManager;

    public TwitchBot() throws IOException, IrcException {
        // set keys for VoiceText api
        System.setProperty("voicetext.apikey", "uy95dtztaidafapb");

        // set keys for aws api
        System.setProperty("aws.accessKeyId", "AKIAIFMXLMYYHI27JWQQ");
        System.setProperty("aws.secretKey", "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");

        // init bot
        this.setVerbose(true);
        this.connect("irc.twitch.tv", 6667, "oauth:ozhduilmnfmgt19bf6si0mhuxk6ptd");
        this.joinChannel("#dyukusi");

        // for Text2Speech
        this.speakManager = new SpeakerManager();
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        String speakMessage = message;
        boolean shouldTranslate = true;

        // if ng word included, replace alternative word
        String alternativeWord = this.getAlternativeWordIfNgWord(message);
        if (alternativeWord != null) {
            speakMessage = alternativeWord;
            shouldTranslate = false;
        }

        // language detection
        Language detectedLanguage = LanguageDetection.detect(speakMessage);

        // get name speaker
        Speaker nameSpeaker = speakManager.getSpeaker(VoiceId.Joanna.name());

        // get comment speaker
        List<Speaker> speakerCandidates = speakManager.getSpeakers(detectedLanguage);

        Speaker commentSpeaker = speakerCandidates.get(Math.abs(sender.hashCode() % speakerCandidates.size()));

        try {
            // speak sender name in English
            nameSpeaker.speak(sender);

            // speak comment
            commentSpeaker.speak(speakMessage);

            // translation
            if (shouldTranslate) {
                String translatedMessage = MicrosoftTranslateText.translate(detectedLanguage, speakMessage);
                this.sendMessage(channel, "[Translation] " + translatedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getAlternativeWordIfNgWord(String text) {
        for (Map.Entry<String, String> entry : Conf.SPEAK_NG_WORD_MAP.entrySet()) {
            String ngWord = entry.getKey();
            String alternativeWord = entry.getValue();

            if (text.matches("(?i).*" + ngWord + ".*")) {
                System.out.println("NG word detected. the text will be replaced to specified alternative word");
                return alternativeWord;
            }
        }
        return null;
    }
}
