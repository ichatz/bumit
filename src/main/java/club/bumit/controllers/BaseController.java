package club.bumit.controllers;

import club.bumit.model.User;
import club.bumit.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Basic functionality used by all controllers.
 *
 * @author ichatz@gmail.com
 */
@Controller
public class BaseController {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(BaseController.class);

    protected final User anonymous;

    @Autowired
    protected UserRepository userRepository;

    /**
     * Default constructor.
     */
    public BaseController() {
        anonymous = new User();
        anonymous.setId("");
        anonymous.setName("stranger");
        anonymous.setUsername("unknown");
        anonymous.setPassword("");
    }

    /**
     * Retrieve current user from security context holder.
     *
     * @return the User object.
     */
    protected final User getUser() {
        final Object userObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userObject instanceof User)
            return (User) userObject;

        else return anonymous;
    }

    /**
     * Check if this is an unknown user.
     *
     * @param user the user object
     * @return true, if the user is unknown / not logged in.
     */
    protected boolean isUnknownUser(final User user) {
        if ("unknown".equals(user.getUsername())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieve the response from a specific URL endpoint.
     *
     * @param url the URL endpoint.
     * @return the response.
     * @throws java.io.IOException in case an error occurred from the net.
     */
    public final String retrieveFromUrl(final URL url)
            throws IOException {
        final URLConnection connection = url.openConnection();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
        final StringBuilder inputLine = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            inputLine.append(str);
        }
        reader.close();
        return inputLine.toString();
    }
}
