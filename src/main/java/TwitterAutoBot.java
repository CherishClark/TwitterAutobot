import com.jauntium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import twitter4j.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TwitterAutoBot {

    public static void main(String[] args) {

        try {

            //Add system property for chromedriver
            System.setProperty("webdriver.chrome.driver", "/Users/cherishclark/projects/TwitterAutoBot/chromedriver");   //setup (edit the path)

            Browser browser = new Browser(new ChromeDriver());   //create new browser window
            browser.visit("https://saltlakecityut.citysourced.com/servicerequests/nearby");                  //visit a url
            Node bodyNode = browser.doc.findFirst("<body>").toNode();   //g// et body node

            Elements ticketElements = browser.doc.findEvery("<a class=link>");

            Elements items = browser.doc.findEvery("<div class=item-title>");
            List<Element> cart = new ArrayList<>();
            for (Element e : items ) {
                if (e.getTextContent().contains("Cart")){
                cart.add(e);
            }
            }

            String href = cart.get(0).getParent().getParent().getParent().getAt("href");
//            List<String> ticketUrls = new ArrayList<>();
//            for (Element e : ticketElements) {
//                String href = null;
//                try {
//                     href = e.getAt("href");
//                } catch (NotFound ex) {
//                    System.out.println(" that didn't work " + e);
//                }
//
//                    ticketUrls.add(href);
//            }

           sendDirectMessage(href);

        } catch (NotFound e) {
            System.out.println(e);
        }


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
                    sendDirectMessage("");
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

    private static void sendDirectMessage(String s) {
        Twitter twitter = TwitterFactory.getSingleton();

        try {
            User wanderingdave = twitter.showUser("wanderingdave");
            twitter.sendDirectMessage(wanderingdave.getId(), "Help me, I'm abandoned " + s);
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