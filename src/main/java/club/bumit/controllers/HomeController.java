package club.bumit.controllers;

import club.bumit.model.SocialAccount;
import club.bumit.repository.SocialAccountRepository;
import club.bumit.repository.UserRepository;
import club.bumit.service.SocialService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Home page controller.
 *
 * @author ichatz@gmail.com
 */
@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(HomeController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Autowired
    SocialService socialService;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Map<String, Object> model) {
        if (isUnknownUser(getUser())) {

        } else {
            List<SocialAccount> accounts = socialAccountRepository.findByUserId(getUser().getId());
            if (accounts.isEmpty()) {
                return "redirect:/connect/twitter";
            } else {
                model.put("twitterProfile", socialService.getProfile(accounts.iterator().next()));
                model.put("friends", socialService.listFriends(accounts.iterator().next()));
            }
        }
        return "home";
    }

}
