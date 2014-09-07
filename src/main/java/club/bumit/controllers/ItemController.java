package club.bumit.controllers;

import club.bumit.util.BumitItem;
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

    private List<BumitItem> getItems() {
        // The factory instance is re-useable and thread safe.
        final Twitter twitter = TwitterSingleton.getInstance().getTwitterFactory().getInstance();
        final Query query = new Query("#bumit");
        final ArrayList<BumitItem> bumitItems = new ArrayList<BumitItem>();

        try {
            final QueryResult result = twitter.search(query);
            for (final Status status : result.getTweets()) {
                final int imagePos = status.getText().indexOf("http://t.co");
                System.out.println(imagePos + ": " + status.getText());
                if (imagePos > 0) {
                    final BumitItem item = new BumitItem();
                    item.setText(status.getText().substring(0, imagePos - 1));
                    item.setName(status.getUser().getName());
                    item.setHandle(status.getUser().getScreenName());
                    item.setImageUrl(status.getText().substring(imagePos));

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
                    bumitItems.add(item);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bumitItems;
    }

    @RequestMapping("/items.rss")
    public String rss(final Model model) {
        model.addAttribute("items", getItems());
        return "itemsrss";
    }

    @RequestMapping("/items")
    public String home(final Model model) {
        model.addAttribute("items", getItems());
        return "items";
    }

}
