package club.bumit.config;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SocialConfigApiKeys {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(SocialConfigApiKeys.class);

    /**
     * <p>The consumer API key (username) for Twitter.</p>
     */
    public static final String TWITTER_API_KEY;

    /**
     * <p>The consumer API secret (password) for Twitter.</p>
     */
    public static final String TWITTER_API_SECRET;

    /**
     * <p>Static initializer block that loads the api key and api secret from "social.properties".</p>
     */
    static {
        //Load the social.properties file
        final InputStream socialPropertiesFile =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("social.properties");

        //If the file exists continue
        if (socialPropertiesFile != null) {
            //Create a Properties instance
            final Properties socialProperties = new Properties();

            //Try and load social.properties in the Properties instance
            try {
                socialProperties.load(socialPropertiesFile);
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("Error loading social.properties file.", e);
            }

            if (socialProperties.containsKey("twitter_api_key") && socialProperties.containsKey("twitter_api_secret")) {
                //If the file was loaded successfully and is well formed it will contain the following keys.
                //We load those properties and finish
                TWITTER_API_KEY = socialProperties.getProperty("twitter_api_key");
                TWITTER_API_SECRET = socialProperties.getProperty("twitter_api_secret");

            } else {
                //If the file is malformed give empty default values
                TWITTER_API_KEY = "";
                TWITTER_API_SECRET = "";
            }

        } else {
            //If the file doesn't exist or couldn't be loaded give empty default values
            TWITTER_API_KEY = "";
            TWITTER_API_SECRET = "";
        }
    }
}
