package club.bumit.controllers;

import club.bumit.model.User;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * User controller.
 *
 * @author ichatz@gmail.com
 */
@Controller
public class UserController extends BaseController {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(UserController.class);
    private BCryptPasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String home(final Model model) {
        if (isUnknownUser(getUser())) {
            return "login";
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
