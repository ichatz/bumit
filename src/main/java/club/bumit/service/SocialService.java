package club.bumit.service;

import club.bumit.config.SocialConfigApiKeys;
import club.bumit.model.BumitItem;
import club.bumit.model.SocialAccount;
import club.bumit.model.User;
import club.bumit.repository.SocialAccountRepository;
import club.bumit.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import twitter4j.Status;

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
    SocialAccount bumitSocialAccount;

    @PostConstruct
    public void init() {
        User bumitclubUser = userRepository.findByHandle("BumitClub");
        if (bumitclubUser != null) {
            bumitSocialAccount = socialAccountRepository.findByUserId(bumitclubUser.getId()).iterator().next();
            LOGGER.info("found BumitClub account");
        }
    }


    public DirectMessage sendTwitterDirectMessage(final String screenName, final String message) {
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                bumitSocialAccount.getAccessToken(), bumitSocialAccount.getSecret());
        twitter.friendOperations().follow(screenName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                twitter.directMessageOperations().sendDirectMessage(screenName, message);
            }
        }).start();
        return null;
    }
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

    public void makeReplyBumit(final BumitItem item, final String screenName) {
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                bumitSocialAccount.getAccessToken(), bumitSocialAccount.getSecret());
        LOGGER.info(bumitSocialAccount);
        TweetData tweetData = new TweetData("Looking for a " + item.getText().replaceAll("#bumit", "") + "? borrow one from @" + screenName + " http://bumit.sensorflare.com/bumit/" + item.getId());
        tweetData.inReplyToStatus(item.getId());
        Tweet status = twitter.timelineOperations().updateStatus(tweetData);
        LOGGER.info(status);
    }

    public void makeReplyBumit4(final BumitItem item, final String screenName) {
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                bumitSocialAccount.getAccessToken(), bumitSocialAccount.getSecret());
        LOGGER.info(bumitSocialAccount);
        TweetData tweetData = new TweetData("Offering a " + item.getText().replaceAll("#bumit4", "") + "? lend one to @" + screenName + " http://bumit.sensorflare.com/bumit4/" + item.getId());
        tweetData.inReplyToStatus(item.getId());
        Tweet status = twitter.timelineOperations().updateStatus(tweetData);
        LOGGER.info(status);
    }

    public void sendReply(final String screenName, String replyContents, Long statusId) {
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                bumitSocialAccount.getAccessToken(), bumitSocialAccount.getSecret());
        LOGGER.info(bumitSocialAccount);
        twitter.friendOperations().follow(screenName);
        TweetData tweetData = new TweetData(replyContents);
        tweetData.inReplyToStatus(statusId);
        Tweet status = twitter.timelineOperations().updateStatus(tweetData);
        LOGGER.info(status);
    }

    public void sendReplyBumit(User user, Status status, String time, String duration, String price) {
        final SocialAccount userSocialAccount = socialAccountRepository.findByUserId(user.getId()).iterator().next();
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                userSocialAccount.getAccessToken(), userSocialAccount.getSecret());

        TweetData tweetData = new TweetData("@" + status.getUser().getScreenName() + " Lend it to me on " + time + " (" + duration + ") for " + price + "? http://bumit.sensorflare.com/bumit/" + status.getId() + "/" + twitter.userOperations().getProfileId());
        tweetData.inReplyToStatus(status.getId());
        Tweet status1 = twitter.timelineOperations().updateStatus(tweetData);
        LOGGER.info(status1);
    }

    public void sendReplyBumit4(User user, Status status, String time, String duration, String price) {
        final SocialAccount userSocialAccount = socialAccountRepository.findByUserId(user.getId()).iterator().next();
        final TwitterTemplate twitter = new TwitterTemplate(
                SocialConfigApiKeys.TWITTER_API_KEY, SocialConfigApiKeys.TWITTER_API_SECRET,
                userSocialAccount.getAccessToken(), userSocialAccount.getSecret());
        TweetData tweetData = new TweetData("@" + status.getUser().getScreenName() + " Borrow it from me on " + time + " (" + duration + ") for " + price + "? http://bumit.sensorflare.com/bumit4/" + status.getId() + "/" + twitter.userOperations().getProfileId());
        tweetData.inReplyToStatus(status.getId());
        Tweet status1 = twitter.timelineOperations().updateStatus(tweetData);
        LOGGER.info(status1);
    }
}
