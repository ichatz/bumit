package club.bumit;

import club.bumit.util.TwitterSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

/**
 * Main Application file.
 *
 * @author ichatz@gmail.com
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public void run(String... args) throws Exception {

    }

    public static void main(String[] args)
            throws Exception {
        SpringApplication.run(Application.class, args);

        // The factory instance is re-useable and thread safe.
        final Twitter twitter = TwitterSingleton.getInstance().getTwitterFactory().getInstance();
        Query query = new Query("#bumit");
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }

        /*TwitterStream twitterStream = TwitterSingleton.getInstance().getTwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }
            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }
            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }
            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }
            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);

        final ArrayList<String> queryString = new ArrayList<String>();
        queryString.add("#disruptsf2014");
        queryString.add("#bumitclub");
        final FilterQuery fquery = new FilterQuery();
        fquery.track(queryString.toArray(new String[2]));
        twitterStream.filter(fquery);*/
    }

}
