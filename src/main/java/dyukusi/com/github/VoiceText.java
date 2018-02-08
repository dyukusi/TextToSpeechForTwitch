package dyukusi.com.github;

import am.ik.voicetext4j.EmotionalSpeaker;

import java.util.concurrent.ExecutionException;

public class VoiceText extends Speaker {
    private Language language;
    private EmotionalSpeaker speaker;

    VoiceText(EmotionalSpeaker speaker) {
        this.speaker = speaker;

        // VoiceTextApi only supports japanese atm
        this.language = Language.JAPANESE;
    }

    @Override
    public void speak(String text) {
        try {
            this.speaker.ready()
                    .pitch(100)
                    .speed(100)
                    .speak(text)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Language getLanguage() {
        return this.language;
    }

    @Override
    public String getName() {
        return this.speaker.name();
    }
}
