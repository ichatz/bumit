package club.bumit.controllers;

import club.bumit.util.TwitterSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Display items provided.
 *
 * @author ichatz@gmail.com
 */
@Controller
public class ItemController {

    @RequestMapping("/items")
    public String home(final Model model) {
        // The factory instance is re-useable and thread safe.
        final Twitter twitter = TwitterSingleton.getInstance().getTwitterFactory().getInstance();
        final Query query = new Query("#disruptsf2014");
        try {
            final QueryResult result = twitter.search(query);
            final List<Status> tweets = result.getTweets();
            model.addAttribute("tweets", tweets);
            final ArrayList<club.bumit.util.Location> locations = new ArrayList<club.bumit.util.Location>();
            for (final Status status : tweets) {
                final club.bumit.util.Location point = new club.bumit.util.Location();
                final GeoLocation pos = status.getGeoLocation();
                if (pos != null) {
                    point.setLatitude(status.getGeoLocation().getLatitude());
                    point.setLongitude(status.getGeoLocation().getLongitude());
                    locations.add(point);
                }
            }
            model.addAttribute("locations", locations);

        } catch (Exception ex) {
            System.err.print(ex);
            model.addAttribute("tweets", new ArrayList<Status>());
            model.addAttribute("locations", new ArrayList<club.bumit.util.Location>());
        }

        return "items";
    }

}
