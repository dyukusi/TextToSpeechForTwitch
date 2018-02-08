package dyukusi.com.github;

import am.ik.voicetext4j.EmotionalSpeaker;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.Voice;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpeakManager {
    ArrayList<SpeakerBase> speakers;

    SpeakManager() {
        this.speakers = new ArrayList<SpeakerBase>();

        AmazonPollyClient polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(), new ClientConfiguration());
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
        List<Voice> amazonPollyVoices = describeVoicesResult.getVoices();
        EmotionalSpeaker[] voiceTextApiSpeakers = EmotionalSpeaker.values();

        // make instances for AmazonPolly
        for (Voice voice : amazonPollyVoices) {
            this.speakers.add(new AmazonPolly(voice));
        }
        
        // make instances for VoiceText
        // NOTE: Although its forbidden to use output of free VoiceTextAPI for secondary use atm,
        //       you can test by just removing following commented out.
        // for (EmotionalSpeaker speaker : voiceTextApiSpeakers) {
        //     this.speakers.add(new VoiceText(speaker));
        // }
    }

    List<SpeakerBase> getSpeakers(Language targetLanguage) {
        List<SpeakerBase> targetSpeakers = this.speakers.stream().filter(x -> {
            return x.getLanguage() == targetLanguage;
        }).collect(Collectors.toList());

        return targetSpeakers;
    }

    SpeakerBase getSpeaker(String name) {
        SpeakerBase speaker = this.speakers.stream().filter(x -> {
            return x.getName().equals(name);
        }).findFirst().orElse(null);

        return speaker;
    }
}
