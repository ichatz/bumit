package club.bumit.config;

import club.bumit.model.User;
import club.bumit.repository.SocialAccountRepository;
import club.bumit.repository.SocialAccountUsersConnectionRepository;
import club.bumit.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    private static final Logger LOGGER = Logger.getLogger(SocialConfig.class);

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * <p>Provides the controller bean responsible for initiating connections to Social Providers (eg. Twitter, Facebook)</p>
     *
     * @param connectionFactoryLocator The connection factory locator
     * @param connectionRepository     The connection repository
     * @return The controller
     */
    @Bean
    public ConnectController connectController(final ConnectionFactoryLocator connectionFactoryLocator,
                                               final ConnectionRepository connectionRepository) {
        final ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);

        connectController.addInterceptor(new ConnectInterceptor<Object>() {
            @Override
            public void preConnect(ConnectionFactory<Object> objectConnectionFactory, MultiValueMap<String, String> stringStringMultiValueMap, WebRequest webRequest) {

            }

            @Override
            public void postConnect(Connection<Object> twitterconnection, WebRequest webRequest) {
            }
        });

        return connectController;
    }

    /**
     * <p>Registers providers with the ConnectionFactoryConfigurer we want to support in this application.</p>
     *
     * @param connectionFactoryConfigurer The connection factory configurer instance
     * @param environment                 Object containing environment variables
     */
    @Override
    public void addConnectionFactories(final ConnectionFactoryConfigurer connectionFactoryConfigurer,
                                       final Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(
                new TwitterConnectionFactory(SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET));
    }

    /**
     * <p>Returns the UserIdSource to be used in this project. The UserIdSource returned returns the User's Id <br />
     * in String representation.</p>
     *
     * @return The UserIdSource
     */
    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication == null)
                    throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");

                final String userId = ((User) authentication.getPrincipal()).getId();
                return userId;
            }
        };
    }

    /**
     * <p>Returns the instance of the UsersConnectionRepository to be used in this project. We currently use an <br/>
     * InMemoryUsersConnectionRepository</p>
     *
     * @param connectionFactoryLocator The connection factory locator
     * @return THe UsersConnectionRepository
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(final ConnectionFactoryLocator connectionFactoryLocator) {
        return new SocialAccountUsersConnectionRepository(connectionFactoryLocator, socialAccountRepository, userRepository);
    }
}
