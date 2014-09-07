package club.bumit.service;

import club.bumit.config.SocialConfigApiKeys;
import club.bumit.model.SocialAccount;
import club.bumit.repository.SocialAccountRepository;
import club.bumit.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Helper functions for social accounts.
 */
@Service
public class SocialService {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(SocialService.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    SocialAccountRepository socialAccountRepository;


    @PostConstruct
    public void init() {
    }


//    public DirectMessage sendTwitterDirectMessage(final String twitterHandle, final String message) {
//        final TwitterTemplate twitter = new TwitterTemplate(
//                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
//                socialAccount.getAccessToken(), socialAccount.getSecret());
//        return twitter.directMessageOperations().sendDirectMessage(twitterHandle, message);
//    }
//
//    public Tweet makeTweet(final SocialAccount socialAccount, final String message) {
//        final TwitterTemplate twitter = new TwitterTemplate(
//                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
//                socialAccount.getAccessToken(), socialAccount.getSecret());
//        return twitter.timelineOperations().updateStatus(message + " via http://www.sensorflare.com");
//    }

    public CursoredList<TwitterProfile> listFriends(final SocialAccount socialAccount) {
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                socialAccount.getAccessToken(), socialAccount.getSecret());
        return twitter.friendOperations().getFriends();
    }

    public TwitterProfile getProfile(SocialAccount socialAccount) {
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                socialAccount.getAccessToken(), socialAccount.getSecret());
        return twitter.userOperations().getUserProfile();
    }
}
