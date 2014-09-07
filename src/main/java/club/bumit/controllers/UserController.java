package club.bumit.controllers;

import club.bumit.model.User;
import club.bumit.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String home(final Model model) {
        return "login";
    }


    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
