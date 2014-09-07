package club.bumit;

import club.bumit.service.SocialService;
import club.bumit.util.BumitAuthenticationProvider;
import com.simplify.payments.PaymentsApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Application file.
 *
 * @author ichatz@gmail.com
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    @Bean
    StreamListener streamListener() {
        return new StreamListener();
    }

    @Bean
    SocialService socialService() {
        return new SocialService();
    }

    @Bean
    BumitAuthenticationProvider bumitAuthenticationProvider() {
        return new BumitAuthenticationProvider();
    }

    public void run(String... args) throws Exception {

    }

    public static void main(String[] args)
            throws Exception {
        SpringApplication.run(Application.class, args);

        // The factory instance is re-useable and thread safe.
//        final Twitter twitter = TwitterSingleton.getInstance().getTwitterFactory().getInstance();
//        Query query = new Query("#bumit");
//        QueryResult result = twitter.search(query);
//        for (Status status : result.getTweets()) {
//            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
//        }

    }

}
