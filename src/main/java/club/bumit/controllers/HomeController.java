package club.bumit.controllers;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

/**
 * Home page controller.
 * @author ichatz@gmail.com
 */
@Controller
@RequestMapping("/")
public class HomeController {

    private Twitter twitter;

    private ConnectionRepository connectionRepository;

    @Inject
    public HomeController(final Twitter twitter, final ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String home(final Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        model.addAttribute(twitter.userOperations().getUserProfile());
        final CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
        model.addAttribute("friends", friends);
        return "home";
    }

}
