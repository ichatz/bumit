package club.bumit;

import club.bumit.model.BumitItem;
import club.bumit.model.User;
import club.bumit.repository.UserRepository;
import club.bumit.service.SocialService;
import club.bumit.util.TwitterSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

public class StreamListener implements StatusListener {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SocialService socialService;

    @PostConstruct
    public void init() {
        TwitterStream twitterStream = TwitterSingleton.getInstance().getTwitterStreamFactory().getInstance();

        twitterStream.addListener(this);

        final ArrayList<String> queryString = new ArrayList<String>();
        queryString.add("#bumit");
        queryString.add("#bumit4");
        final FilterQuery fquery = new FilterQuery();
        fquery.track(queryString.toArray(new String[2]));
        twitterStream.filter(fquery);
    }

    @Override
    public void onStatus(Status status) {
        final String senderHandle = status.getUser().getScreenName();
        System.out.println("@" + senderHandle + " - " + status.getText());
        if (status.getText().contains("#bumit")) {
            BumitItem item = parseStatus(status);
            if (item != null) {
                final User user = userRepository.findByHandle(senderHandle);
                if (user != null) {
                    System.out.println("by " + user);
                } else {
                    socialService.sendReply(status.getUser().getScreenName(), "@" + status.getUser().getScreenName() + " Hello " + status.getUser().getName() + ", come and join Bumit to easily lend and borrow everyday items. http://bumit.sensorflare.com/login", status.getId());
                }
                if (item.getText().contains("#bumit4")) {
                    socialService.makeReplyBumit4(item, status.getUser().getScreenName());
                } else if (item.getText().contains("#bumit")) {
                    socialService.makeReplyBumit(item, status.getUser().getScreenName());
                }
            }
        }
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


    BumitItem parseStatus(final Status status) {
//        final int imagePos = status.getText().indexOf("http://t.co");
//        System.out.println(imagePos + ": " + status.getText());
//        if (imagePos > 0) {
        final BumitItem item = new BumitItem();
        item.setId(status.getId());
        item.setText(status.getText());
        item.setName(status.getUser().getName());
        item.setHandle(status.getUser().getScreenName());
//        item.setImageUrl(status.getText().substring(imagePos));

        final GeoLocation pos = status.getGeoLocation();
        if (pos != null) {
            item.setLatitude(status.getGeoLocation().getLatitude());
            item.setLongitude(status.getGeoLocation().getLongitude());

        } else if (status.getPlace() != null) {
            final GeoLocation[][] posBox = status.getPlace().getGeometryCoordinates();
            if (posBox != null) {
                item.setLatitude(posBox[0][0].getLatitude());
                item.setLongitude(posBox[0][0].getLongitude());
            } else {
                System.out.println(status.getPlace().getName());
            }
        } else if (status.getUser().getLocation() != null) {
            System.out.println(status.getUser().getLocation());
        }

        if (item.getLongitude() == 0) {
            item.setLatitude(37.774929);
            item.setLongitude(-122.419416);
        }

        System.out.println(item.getLatitude() + " " + item.getLongitude());
        System.out.println("---");

        return item;//bumitItemRepository.save(item);
//        }
//        return null;
    }
}
