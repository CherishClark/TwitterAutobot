import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
            Connection.Response loginForm = Jsoup.connect("https://saltlakecityut.citysourced.com/").
                    method(Connection.Method.GET).execute();
            org.jsoup.nodes.Document doc = Jsoup.connect("https://saltlakecityut.citysourced.com/").
                    data("formhash","97bfbf").data("hdn_refer","https://saltlakecityut.citysourced.com/pages/ajax/callapiendpoint.ashx")
                    .cookies(loginForm.cookies()).header("Accept","application/json, text/javascript, */*; q=0.01").header("X-Requested-With","XMLHttpRequest").post();

                    doc.body();

            System.out.println(doc.data());

        } catch (IOException e) {
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