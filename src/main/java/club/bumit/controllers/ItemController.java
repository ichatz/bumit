package club.bumit.controllers;

import club.bumit.util.BumitItem;
import club.bumit.util.TwitterSingleton;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter4j.*;

import java.util.ArrayList;

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
        final Query query = new Query("#bumit");
        try {
            final QueryResult result = twitter.search(query);
            final ArrayList<BumitItem> bumitItems = new ArrayList<BumitItem>();

            for (final Status status : result.getTweets()) {
                final int imagePos = status.getText().indexOf("co");
                if (imagePos > 0) {
                    final BumitItem item = new BumitItem();
                    item.setText(status.getText().substring(0, imagePos));
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
                        }
                    }

                    bumitItems.add(item);
               }
            }

            model.addAttribute("items", bumitItems);

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("items", new ArrayList<BumitItem>());
        }

        return "items";
    }

}
