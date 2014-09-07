package club.bumit.repository;

import club.bumit.model.SocialAccount;
import org.apache.log4j.Logger;
import org.springframework.social.connect.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SocialAccountUsersConnectionRepository implements UsersConnectionRepository {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(SocialAccountUsersConnectionRepository.class);

    /**
     * <p>ConnectionFactoryLocator instance.</p>
     */
    private final ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * <p>SocialAccountRepository instance. Bean managed by Spring.</p>
     */
    private final SocialAccountRepository socialAccountRepository;

    /**
     * <p>UserRepository instance. Bean managed by Spring.</p>
     */
    private final UserRepository userRepository;

    /**
     * <p>Default constructor.</p>
     *
     * @param connectionFactoryLocator The ConnectionFactoryLocator instance.
     * @param socialAccountRepository  The SocialAccountRepository instance.
     */
    public SocialAccountUsersConnectionRepository(final ConnectionFactoryLocator connectionFactoryLocator,
                                                  final SocialAccountRepository socialAccountRepository,
                                                  final UserRepository userRepository) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.socialAccountRepository = socialAccountRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findUserIdsWithConnection(final Connection<?> connection) {
        final List<String> userIds = new ArrayList<>();
        final ConnectionKey connectionKey = connection.getKey();
        final List<SocialAccount> socialAccounts = socialAccountRepository.findByProviderIdAndProviderUserId(
                connectionKey.getProviderId(), connectionKey.getProviderUserId());

        for (final SocialAccount socialAccount : socialAccounts)
            userIds.add(socialAccount.getUserId().toString());

        return userIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
        final Set<String> userIds = new HashSet<>();
        final List<SocialAccount> socialAccounts = socialAccountRepository.findByProviderId(providerId);

        for (final SocialAccount socialAccount : socialAccounts) {
            if (providerUserIds.contains(socialAccount.getProviderUserId()))
                userIds.add(socialAccount.getUserId().toString());
        }

        return userIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConnectionRepository createConnectionRepository(final String userId) {
        return new SocialAccountConnectionRepository(userId, connectionFactoryLocator, socialAccountRepository, userRepository);
    }
}
