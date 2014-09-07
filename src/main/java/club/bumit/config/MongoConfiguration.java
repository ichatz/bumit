package club.bumit.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@EnableMongoRepositories
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(MongoConfiguration.class);

    public static String MONGO_URL;
    public static String MONGO_DB;
    public static String MONGO_USER;
    public static String MONGO_PASSWORD;


    /**
     * <p>Static initializer block that loads the api key and api secret from "social.properties".</p>
     */
    static {
        //Load the social.properties file
        final InputStream socialPropertiesFile =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("mongo.properties");

        //If the file exists continue
        if (socialPropertiesFile != null) {
            //Create a Properties instance
            final Properties properties = new Properties();

            //Try and load social.properties in the Properties instance
            try {
                properties.load(socialPropertiesFile);
            } catch (IOException | IllegalArgumentException e) {
                LOGGER.error("Error loading mongo.properties file.", e);
            }

            if (properties.containsKey("mongo.url")) {
                //If the file was loaded successfully and is well formed it will contain the following keys.
                //We load those properties and finish
                MONGO_URL = properties.getProperty("mongo.url", "");
            }
            if (properties.containsKey("mongo.user")) {
                //If the file was loaded successfully and is well formed it will contain the following keys.
                //We load those properties and finish
                MONGO_USER = properties.getProperty("mongo.user", "");
            }
            if (properties.containsKey("mongo.pass")) {
                //If the file was loaded successfully and is well formed it will contain the following keys.
                //We load those properties and finish
                MONGO_PASSWORD = properties.getProperty("mongo.pass", "");
            }

            if (properties.containsKey("mongo.db")) {
                //If the file was loaded successfully and is well formed it will contain the following keys.
                //We load those properties and finish
                MONGO_DB = properties.getProperty("mongo.db", "");
            }

        } else {
            //If the file doesn't exist or couldn't be loaded give empty default values
            MONGO_URL = "";
            MONGO_USER = "";
            MONGO_PASSWORD = "";
            MONGO_DB = "";
        }
    }

    public Mongo mongo() throws Exception {
        return new MongoClient(MONGO_URL);
    }

    @Override
    protected String getDatabaseName() {
        return MONGO_DB;
    }

    @Override
    protected UserCredentials getUserCredentials() {
        return new UserCredentials(MONGO_USER, MONGO_PASSWORD);
    }
}