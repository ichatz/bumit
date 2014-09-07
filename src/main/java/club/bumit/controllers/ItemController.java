package club.bumit.controllers;

import club.bumit.model.BumitItem;
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

    private List<BumitItem> getItems(final String queryStr, final boolean onlyImages) {
        // The factory instance is re-useable and thread safe.
        final Twitter twitter = TwitterSingleton.getInstance().getTwitterFactory().getInstance();
        final Query query = new Query(queryStr);
        final ArrayList<BumitItem> bumitItems = new ArrayList<BumitItem>();

        try {
            final QueryResult result = twitter.search(query);
            for (final Status status : result.getTweets()) {
                final int imagePos = status.getText().indexOf("http://t.co");
                System.out.println(imagePos + ": " + status.getText());

                // skip tweets without images
                if (onlyImages && imagePos < 0) {
                    continue;
                }

                final BumitItem item = new BumitItem();
                item.setId(status.getId());
                item.setName(status.getUser().getName());
                item.setHandle(status.getUser().getScreenName());
                item.setCreatedOn(status.getCreatedAt());

                if (imagePos > 0) {
                    item.setText(status.getText().substring(0, imagePos - 1));
                    item.setImageUrl(status.getText().substring(imagePos));
                } else {
                    item.setText(status.getText());
                    item.setImageUrl("");
                }

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

                if (status.getUser().getScreenName().toLowerCase().equals("myrinafrancis")) {
                    item.setLatitude(37.759020);
                    item.setLongitude(-122.421334);

                } else if (status.getUser().getScreenName().toLowerCase().equals("sensorflare")) {
                    item.setLatitude(37.755132);
                    item.setLongitude(-122.423772);

                } else if (status.getUser().getScreenName().toLowerCase().equals("axiodim")) {
                    item.setLatitude(37.751206);
                    item.setLongitude(-122.418355);
                }

                if (item.getLongitude() == 0) {
                    item.setLatitude(37.774929);
                    item.setLongitude(-122.419416);
                }

                System.out.println(item.getLatitude() + " " + item.getLongitude());
                System.out.println("---");
                bumitItems.add(item);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bumitItems;
    }

    @RequestMapping("/items.rss")
    public String rss(final Model model) {
        model.addAttribute("items", getItems("#bumit", true));
        return "itemsrss";
    }

    @RequestMapping("/items4.rss")
    public String rss4(final Model model) {
        model.addAttribute("items", getItems("#bumit4", false));
        return "itemsrss";
    }

    @RequestMapping("/items")
    public String home(final Model model) {
        model.addAttribute("items", getItems("#bumit", true));
        model.addAttribute("searchers", getItems("#bumit4", false));
        return "items";
    }

    @RequestMapping("/main")
    public String main(final Model model) {
        model.addAttribute("items", getItems("#bumit", true));
        model.addAttribute("searchers", getItems("#bumit4", false));
        return "main";
    }

}
