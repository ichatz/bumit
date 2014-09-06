package club.bumit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page controller.
 * @author ichatz@gmail.com
 */
@Controller
public class Home {

    @RequestMapping("/")
    public String home(final Model model) {
        //model.addAttribute("name", name);
        return "home";
    }

}
