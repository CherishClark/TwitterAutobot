import com.jauntium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import twitter4j.*;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


            List<Element> ticketsIcareAbout = new ArrayList<>();
            for (Element e : items) {

                boolean isTypeICareAbout = isTicketOfType(e, Issue.IssueType.BIKING.getIssueName())
                        || isTicketOfType(e, Issue.IssueType.CROSSWALKS.getIssueName())
                        || isTicketOfType(e, Issue.IssueType.SIDEWALKS__RAMPS.getIssueName())
                        || isTicketOfType(e, Issue.IssueType.STREET_CURB.getIssueName());

                if (isTypeICareAbout && isTicketInStatusOf(e, "Received")) {
                    ticketsIcareAbout.add(e);
                }
            }

            Element abandonedcart = ticketsIcareAbout.get(0);
            String href = getHrefLinkFromListElement(abandonedcart);

            Document listItemPageDoc = browser.visit(href);

            listItemPageDoc.findEvery("<span id=updatedLng class='Hdn");
            String s = listItemPageDoc.findFirst("<span id=updatedLng class='Hdn").getTextContent();
            // case PATTERN_DAY_OF_MONTH:         // 'd'
            // case PATTERN_HOUR_OF_DAY0:         // 'H' 0-based.  eg, 23:59 + 1 hour =>> 00:59
            // case PATTERN_MINUTE:               // 'm'
            // case PATTERN_SECOND:               // 's'
            // case PATTERN_MILLISECOND:          // 'S'
            // case PATTERN_DAY_OF_YEAR:          // 'D'
            // case PATTERN_DAY_OF_WEEK_IN_MONTH: // 'F'
            // case PATTERN_WEEK_OF_YEAR:         // 'w'
            // case PATTERN_WEEK_OF_MONTH:        // 'W'
            // case PATTERN_HOUR0:                // 'K' 0-based.  eg, 11PM + 1 hour =>> 0 AM
            // case PATTERN_ISO_DAY_OF_WEEK:      // 'u' (pseudo field);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy @ KK:mm aa");

            try {
                Date date = formatter.parse(s);
            } catch (Exception e) {
                System.out.println(e + "date no work");
            }
//
//            LocalDate localDate = new LocalDate(year, month, dayOfMonth);
//            LocalTime localTime = new LocalTime(hour, minute, second);
//                    new LocalDateTime()
//                   <span id="updatedLng" class="Hdn" style="display: none;">Jan 25, 2020 @ 05:25 PM</span>

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