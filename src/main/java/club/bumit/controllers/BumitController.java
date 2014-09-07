package club.bumit.controllers;

import club.bumit.model.BumitItem;
import club.bumit.service.SocialService;
import club.bumit.util.TwitterSingleton;
import com.simplify.payments.PaymentsApi;
import com.simplify.payments.PaymentsMap;
import com.simplify.payments.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author ichatz@gmail.com
 */
@Controller
public class BumitController extends BaseController {

    twitter4j.Twitter twitter;

    @Autowired
    SocialService socialService;


    @PostConstruct
    public void init() {
        twitter = TwitterSingleton.getInstance().getTwitterFactory().getInstance();
    }

    @RequestMapping(value = "/bumit/{tweetId}", method = RequestMethod.GET)
    public String bumitGet(final Map<String, Object> model, @PathVariable("tweetId") Long tweetId) {
        if (isUnknownUser(getUser())) {
            return "redirect:/login";
        }
        try {
            Status status = twitter.showStatus(tweetId);
            if (status == null) { //
                // don't know if needed - T4J docs are VERY BAD
            } else {
                System.out.println("@" + status.getUser().getScreenName()
                        + " - " + status.getText());
            }
            parseStatus(status);
            model.put("status", status);
            return "bumit";
        } catch (TwitterException e) {
            System.err.print("Failed to search tweets: " + e.getMessage());
            // e.printStackTrace();
            // DON'T KNOW IF THIS IS THROWN WHEN ID IS INVALID
            return "redirect:/";
        }

    }

    @RequestMapping(value = "/bumit", method = RequestMethod.POST)
    public String bumitPost(final Map<String, Object> model,
                            @RequestParam final String simplifyToken,
                            @RequestParam("statusId") Long statusId,
                            @RequestParam("time") String time,
                            @RequestParam("duration") String duration,
                            @RequestParam("price") String price) throws Exception {
        try {
            Status status = twitter.showStatus(statusId);
            if (status == null) { //
                // don't know if needed - T4J docs are VERY BAD
            } else {
                System.out.println("@" + status.getUser().getScreenName()
                        + " - " + status.getText());
            }
            socialService.sendReplyBumit(getUser(), status, time, duration, price);
            parseStatus(status);
            model.put("status", status);

            PaymentsApi.PRIVATE_KEY = "h6nIxIuGUx0LH1GtETYSn1+vWhnf01p5E7Oywitxfql5YFFQL0ODSXAOkNtXTToq";
            PaymentsApi.PUBLIC_KEY = "sbpb_OGY5OGZiYjEtMjMyZC00MGVlLWI0OGQtZThiNjM1OGMwYmZk";

            System.out.println(simplifyToken);

            final Payment payment = Payment.create(new PaymentsMap()
                    .set("currency", "USD")
                    .set("token", simplifyToken) // returned by JavaScript call
                    .set("amount", 1000) // In cents e.g. $10.00
                    .set("description", "BumIt Club"));
            if ("APPROVED".equals(payment.get("paymentStatus"))) {
                System.out.println("Payment approved");
            }

            model.put("paymentStatus", payment.get("paymentStatus"));
            return "payment-success";


        } catch (TwitterException e) {
            System.err.print("Failed to search tweets: " + e.getMessage());
            // e.printStackTrace();
            // DON'T KNOW IF THIS IS THROWN WHEN ID IS INVALID
        }
        return "redirect:/";

    }

    @RequestMapping(value = "/bumit4/{tweetId}", method = RequestMethod.GET)
    public String bumit4Get(final Map<String, Object> model, @PathVariable("tweetId") Long tweetId) {
        if (isUnknownUser(getUser())) {
            return "redirect:/login";
        }
        try {
            Status status = twitter.showStatus(tweetId);
            if (status == null) { //
                // don't know if needed - T4J docs are VERY BAD
            } else {
                System.out.println("@" + status.getUser().getScreenName()
                        + " - " + status.getText());
            }
            parseStatus(status);
            model.put("status", status);
            return "bumit4";
        } catch (TwitterException e) {
            System.err.print("Failed to search tweets: " + e.getMessage());
            // e.printStackTrace();
            // DON'T KNOW IF THIS IS THROWN WHEN ID IS INVALID
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/bumit4", method = RequestMethod.POST)
    public String bumit4Post(final Map<String, Object> model,
                             @RequestParam("statusId") Long statusId,
                             @RequestParam("time") String time,
                             @RequestParam("duration") String duration,
                             @RequestParam("price") String price) {
        try {
            Status status = twitter.showStatus(statusId);
            if (status == null) { //
                // don't know if needed - T4J docs are VERY BAD
            } else {
                System.out.println("@" + status.getUser().getScreenName()
                        + " - " + status.getText());
            }
            socialService.sendReplyBumit4(getUser(), status, time, duration, price);
            parseStatus(status);
            model.put("status", status);
        } catch (TwitterException e) {
            System.err.print("Failed to search tweets: " + e.getMessage());
            // e.printStackTrace();
            // DON'T KNOW IF THIS IS THROWN WHEN ID IS INVALID
        }
        return "redirect:/";
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
