package club.bumit.util;

import club.bumit.model.User;
import club.bumit.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * The authentication provider looks up the user data from the neo4j repository.
 */
@Component
public class BumitAuthenticationProvider
        implements AuthenticationProvider {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(BumitAuthenticationProvider.class);
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public BumitAuthenticationProvider() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Performs authentication with the same contract as {@link
     * org.springframework.security.authentication.AuthenticationManager#authenticate(org.springframework.security.core.Authentication)}.
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return <code>null</code> if the
     * <code>AuthenticationProvider</code> is unable to support authentication of the passed
     * <code>Authentication</code> object. In such a case, the next <code>AuthenticationProvider</code> that
     * supports the presented <code>Authentication</code> class will be tried.
     * @throws org.springframework.security.core.AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        final String username = String.valueOf(auth.getPrincipal());
        final String password = String.valueOf(auth.getCredentials());

        // 1. Use the username to load the data for the user, including authorities and password.
        final User user = userRepository.findByName(username);

        // Check if we are connected to Neo4J
        if (userRepository == null) {
            throw new BadCredentialsException("Internal Server Error");
        }

        // Check that user exists in our database
        if (user == null) {
            throw new BadCredentialsException("Bad Credentials");
        }

        // 2. Check the passwords match.

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Bad Credentials");
        }

        // 3. Preferably clear the password in the user object before storing in authentication object
        //user.clearPassword();

        // 5. Return an authenticated token, containing user data and authorities
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<GrantedAuthority>());
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the indicated
     * <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an <code>AuthenticationProvider</code> will be able to
     * authenticate the presented instance of the <code>Authentication</code> class. It simply indicates it can support
     * closer evaluation of it. An <code>AuthenticationProvider</code> can still return <code>null</code> from the
     * {@link #authenticate(org.springframework.security.core.Authentication)} method to indicate another <code>AuthenticationProvider</code> should be
     * tried.
     * </p>
     * <p>Selection of an <code>AuthenticationProvider</code> capable of performing authentication is
     * conducted at runtime the <code>ProviderManager</code>.</p>
     *
     * @param authentication
     * @return <code>true</code> if the implementation can more closely evaluate the <code>Authentication</code> class
     * presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
