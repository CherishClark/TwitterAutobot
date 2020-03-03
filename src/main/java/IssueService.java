import com.jauntium.*;
import org.omg.CORBA.TIMEOUT;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueService {


    public void scrapeIssues() {

        //Add system property for chromedriver
        try {
            Browser browser = setupScraper();

            Elements items = findAllIssueElements(browser);


            List<Element> filteredElements = getFilteredIssueElements(items);

            Element getOneElement = filteredElements.get(0);
            Document listItemPageDoc = findIssueDocument(browser, getOneElement);
            Date date = getDateLastUpdatedFromIssueDoc(listItemPageDoc);

//            this.id = id;
//            this.created = created;
//            this.dateLastUpdated = dateLastUpdated;
//            this.issueType = issueType;
//            this.title = title;

            Issue issue = new Issue();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Date getDateLastUpdatedFromIssueDoc(Document listItemPageDoc) throws NotFound {
        String dateString = listItemPageDoc.findFirst("<span id=updatedLng class='Hdn").getTextContent();
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
        Date date = new Date();
        try {
            date = formatter.parse(dateString);
        } catch (Exception e) {
            System.out.println(e + "date no work");
        }

        return date;
    }

    private Document findIssueDocument(Browser browser, Element getOneElement) throws NotFound {
        String href = getHrefLinkFromListElement(getOneElement);
        return browser.visit(href);
    }

    private List<Element> getFilteredIssueElements(Elements items) throws InterruptedException{
        Thread.sleep(1600);
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
        return ticketsIcareAbout;
    }

    private Elements findAllIssueElements(Browser browser) throws InterruptedException {

         browser.doc.findEvery("<a class=link href=/servicerequests/>");
         Thread.sleep(1600);
        return browser.doc.findEvery("<div class=item-title>");
    }

    private Browser setupScraper() throws NotFound {
        ChromeOptions options = new ChromeOptions();                //create chrome options object
        options.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver", "/Users/cherishclark/projects/TwitterAutoBot/chromedriver");   //setup (edit the path)

        Browser browser = new Browser(new ChromeDriver(options));   //create new browser window
        browser.visit("https://saltlakecityut.citysourced.com/servicerequests/nearby");//visit a url

        return browser;
    }

    private static boolean isTicketInStatusOf(Element e, String status) {
        return e.nextElementSibling().innerHTML().contains(status);
    }

    private static boolean isTicketOfType(Element e, String type) {
        return e.getTextContent().toLowerCase().contains(type.toLowerCase());
    }

    private static String getHrefLinkFromListElement(Element abandonedCart) throws NotFound {
        return abandonedCart.getParent().getParent().getParent().getAt("href");
    }
}


