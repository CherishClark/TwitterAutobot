import twitter4j.*;

import java.io.*;
import java.nio.charset.Charset;

public class TwitterAutoBot {

    public static void main(String[] args) {
        tweetLines();
    }

    private static void tweetLines() {
        String line;
        try {
            try (
                    InputStream fis = new FileInputStream("/Users/cherishclark/projects/TwitterAutoBot/src/main/resources/tweets.txt");
                    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("Cp1252"));
                    BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {
                    // Deal with the line
                    sendDirectMessage();
                    System.out.println("Tweeting: " + line + "...");

                    try {
                        System.out.println("Sleeping for 30 minutes...");
                        Thread.sleep(180000); // every 30 minutes
                        // Thread.sleep(10000); // every 10 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendDirectMessage() {
        Twitter twitter = TwitterFactory.getSingleton();

        try {
            User wanderingdave = twitter.showUser("wanderingdave");
            twitter.sendDirectMessage(wanderingdave.getId(), "I am a bot and I love you");
        } catch (TwitterException e) {
            System.out.println("couldn't find this user" + e);
        }

    }

    private static void sendTweet(String line) {
        Twitter twitter = TwitterFactory.getSingleton();
        Status status;

        try {
            status = twitter.updateStatus(line);
            System.out.println(status);
        } catch (TwitterException e) {;
            e.printStackTrace();
        }
    }

}