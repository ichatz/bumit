package club.bumit.util;

import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Keeps the configuration of twitter4j.
 *
 * @author ichatz@gmail.com
 */
public class TwitterSingleton {

    private static TwitterSingleton singleton;

    private final TwitterStreamFactory tsf;

    private final TwitterFactory tf;

    private TwitterSingleton() {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);

        cb.setOAuthConsumerKey("t4O0CbznYyunAvW56gnzRkFzA")
                .setOAuthConsumerSecret("v9lxN6kX5ni6JY5jTt9Mf5xzPpAjCuBwPOHlydCd190yF8872N")
                .setOAuthAccessToken("2794851559-pII8isWrGktMeBmKSlPdXikKPSQYMOfSF3KuONI")
                .setOAuthAccessTokenSecret("UQ8GklktP0JRjO1JGqOd7bFWtaLLfubcK0ToYYCaQpMXj");

        final Configuration conf = cb.build();
        tsf = new TwitterStreamFactory(conf);
        tf = new TwitterFactory(conf);
    }

    public static TwitterSingleton getInstance() {
        synchronized (TwitterSingleton.class) {
            if (singleton == null) {
                singleton = new TwitterSingleton();
            }
        }

        return singleton;
    }

    public TwitterStreamFactory getTwitterStreamFactory() {
        return tsf;
    }

    public TwitterFactory getTwitterFactory() {
        return tf;
    }
}
