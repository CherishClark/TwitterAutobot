import com.jaunt.Document;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;
import twitter4j.*;

import java.io.*;
import java.nio.charset.Charset;

public class TwitterAutoBot {

    public static void main(String[] args) {
//        tweetLines();


//        Connection.Response loginForm=Jsoup.connect("http://www.a5.cn").
//                method(Connection.Method.GET).execute();
//
//        Document document=Jsoup.connect("http://www.a5.cn/login.html").
//                data("formhash","97bfbf").data("hdn_refer","http://www.a5.cn/")
//        data("account","userID").data("autoLogin","1").data("password","your password").
//                cookies(loginForm.cookies()).header("Accept","application/json, text/javascript, */*; q=0.01").header("X-Requested-With","XMLHttpRequest").post();
//
//        System.out.println(document.body().text());
        try {
            UserAgent agent = new UserAgent();
//            https://saltlakecityut.citysourced.com/servicerequests/nearby

             Document doc = agent.visit("https://saltlakecityut.citysourced.com/servicerequests/nearby");

            System.out.println("Response:\n" + agent.response);

//            <a class="link" href="/servicerequests/840073">
//            <i class="far fa-chevron-right"></i>
//            <div class="item-content">
//                <div class="item-media">
//                    <img src="https://d2p5liwq1c5kwh.cloudfront.net/FileStorage/2020-01/eeeef94e81d84f6e88bff8b6bbbb64d6_md.jpg">
//                </div>
//                <div class="item-inner">
//                    <div class="item-title">
//                    Abandoned Shopping Cart
//                    </div>
//                    <div class="item-after">
//                        <div>
//                            <div>Updated: <span class="item-updated">12h ago</span></div>
//                            <span class="Received item-status">Received</span>
//                        </div>
//                    </div>
//                </div>
//            </div>
//        </a>

//            System.out.println(doc.innerHTML());

            Element body = agent.doc.findEach("<body>");
            System.out.println("This is the body " + body + " | body child text " + body.getChildText());

            Elements divs = body.findEach("<div>");
            for (Element div: divs) {
//                System.out.println("This is a div " + div);
//                System.out.println("this is div child text " + div.getChildText());
            }

            Element appDiv = body.findFirst("div id=app");
//            System.out.println("is this the app div? " + appDiv + appDiv.getChildText());
//            System.out.println(appDiv.innerHTML());

            Element divList = appDiv.findEach("div id=divList");


            Element ul = divList.findFirst("<ul>");
            System.out.println(ul.innerHTML());




//            Elements tables = agent.doc.findEach("<a class=\"link\" href>");
//            //find non-nested tables
//            System.out.println("Found " + tables.size() + " tables:");
//            for(Element table : tables){                               //iterate through search results
//                System.out.println(table.outerHTML() + "\n----\n");
//                System.out.println(table.getTextContent());//print each element and its contents
//                System.out.println(tables.getAttributeNames());
//            }

//
//            Connection.Response loginForm = Jsoup.connect("https://saltlakecityut.citysourced.com/").
//                    method(Connection.Method.GET).execute();
//            org.jsoup.nodes.Document doc = Jsoup.connect("https://saltlakecityut.citysourced.com/").
//                    data("formhash","97bfbf").data("hdn_refer","https://saltlakecityut.citysourced.com/pages/ajax/callapiendpoint.ashx")
//                    .cookies(loginForm.cookies()).header("Accept","application/json, text/javascript, */*; q=0.01").header("X-Requested-With","XMLHttpRequest").post();
//
//            doc.body();
//

        } catch ( Exception  e) {
            System.out.println("tried getting the url with ajax and failed " + e);
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