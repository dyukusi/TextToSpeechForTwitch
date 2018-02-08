package dyukusi.com.github;
import org.jibble.pircbot.IrcException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, IrcException {
        System.out.println("text to speech for twitch process started");

        // let bot join the channel
        TwitchBot bot = new TwitchBot();
    }
}
