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
            List<Element> abandonedCarts = new ArrayList<>();
            for (Element e : items) {
                if (isTicketOfType(e, "Cart") && isTicketInStatusOf(e, "In Process")) {
                    abandonedCarts.add(e);
                }
            }

            Element abandonedcart = abandonedCarts.get(0);
            String href = getHrefLinkFromListElement(abandonedcart);


//            sendDirectMessage(href);

        } catch (NotFound e) {
            System.out.println(e);
        }


    }

    private static boolean isTicketInStatusOf(Element e, String status) {
        return e.nextElementSibling().innerHTML().contains(status);
    }

    private static boolean isTicketOfType(Element e, String type) {
        return e.getTextContent().contains(type);
    }
    private static String getHrefLinkFromListElement(Element abandonedCart) throws NotFound {
        return abandonedCart.getParent().getParent().getParent().getAt("href");
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
        } catch (TwitterException e) {
            ;
            e.printStackTrace();
        }
    }

}