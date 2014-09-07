package club.bumit.controllers;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * Home page controller.
 * @author ichatz@gmail.com
 */
@Controller
public class HomeController {

    private Twitter twitter;

    private ConnectionRepository connectionRepository;

    @Inject
    public HomeController(final Twitter twitter, final ConnectionRepository connectionRepository) {
        this.twitter = twitter;
        this.connectionRepository = connectionRepository;
    }

    @RequestMapping("/")
    public String home(final Model model) {
        //model.addAttribute("name", name);
        return "home";
    }

}
