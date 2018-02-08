package dyukusi.com.github;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.services.polly.model.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.IOException;
import java.io.InputStream;

public class AmazonPolly extends Speaker {
    private final AmazonPollyClient polly;
    private final Voice voice;
    private final Language language;

    AmazonPolly(Voice voice) {
        // create an Amazon Polly client in a specific region
        polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(),
                new ClientConfiguration());
        polly.setRegion(Region.getRegion(Regions.US_WEST_1));

        this.voice = voice;

        // TODO : fix dirty implementation
        if (voice.getLanguageName().contains("Japanese")) {
            this.language = Language.JAPANESE;
        } else if (voice.getLanguageName().contains("English")) {
            this.language = Language.ENGLISH;
        } else {
            this.language = Language.OTHERS;
        }
    }

    @Override
    public void speak(String text) {
        try {
            //get the audio stream
            InputStream speechStream = this.synthesize(text, this.voice, OutputFormat.Mp3);

            //create an MP3 player
            AdvancedPlayer player = new AdvancedPlayer(speechStream,
                    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackStarted(PlaybackEvent evt) {
                    //System.out.println("Playback started");
                }

                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    //System.out.println("Playback finished");
                }
            });

            player.play();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Language getLanguage() {
        return this.language;
    }

    @Override
    public String getName() {
        return voice.getName();
    }

    public InputStream synthesize(String text, Voice voice, OutputFormat format) throws IOException {
        SynthesizeSpeechRequest synthReq =
                new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
                        .withOutputFormat(format);
        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

        return synthRes.getAudioStream();
    }
}
