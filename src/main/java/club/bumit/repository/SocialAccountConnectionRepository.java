package club.bumit.repository;

import club.bumit.model.SocialAccount;
import org.apache.log4j.Logger;
import org.springframework.social.connect.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

public final class SocialAccountConnectionRepository implements ConnectionRepository {
    /**
     * <p>Custom Logger instance.</p>
     */
    private static final Logger LOGGER = Logger.getLogger(SocialAccountConnectionRepository.class);

    /**
     * <p>The id of the User this ConnectionRepository is for.</p>
     */
    private final String userId;

    /**
     * <p>ConnectionFactoryLocator instance passed by SocialAccountUsersConnectionRepository and managed by Spring.</p>
     */
    private final ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * <p>SocialAccountRepository instance passed by SocialAccountUsersConnectionRepository and managed by Spring.</p>
     */
    private final SocialAccountRepository socialAccountRepository;

    /**
     * <p>UserRepository instance passed by SocialAccountUsersConnectionRepository and managed by Spring.</p>
     */
    private final UserRepository userRepository;

    private static final class SocialAccountRankByGraphIdComparator implements Comparator<SocialAccount> {
        @Override
        public int compare(SocialAccount socialAccount1, SocialAccount socialAccount2) {
            if (socialAccount1 == null || socialAccount2 == null)
                throw new NullPointerException("Arguments cannot be null");

            if ((socialAccount1.getUserId().equals(socialAccount2.getId()))) return 0;
            else return 1;
        }
    }

    /**
     * <p>Primary constructor. Each parameter is passed by the SocialAccountUsersConnectionRepository instance.</p>
     *
     * @param userId                   The userId this ConnectionRepository is for.
     * @param connectionFactoryLocator The ConnectionFactoryLocator instance used to get ConnectionFactory instances
     * @param socialAccountRepository  The SocialAccountRepository instance managed by Spring used for persistence
     * @param userRepository           The UserRepository instance managed by Spring used to get User objects
     */
    public SocialAccountConnectionRepository(final String userId,
                                             final ConnectionFactoryLocator connectionFactoryLocator,
                                             final SocialAccountRepository socialAccountRepository,
                                             final UserRepository userRepository) {
        this.userId = userId;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.socialAccountRepository = socialAccountRepository;
        this.userRepository = userRepository;
    }

    /**
     * <p>Given a SocialAccount this method returns a ConnectionData object fully initialized that can be used to create <br />
     * a fully working Connection<?></p>
     *
     * @param socialAccount The SocialAccount
     * @return The ConnectionData
     */
    private ConnectionData createConnectionDataForSocialAccount(final SocialAccount socialAccount) {
        return new ConnectionData(socialAccount.getProviderId(), socialAccount.getProviderUserId(),
                socialAccount.getDisplayName(), socialAccount.getProfileUrl(), socialAccount.getImageUrl(),
                socialAccount.getAccessToken(), socialAccount.getSecret(),
                socialAccount.getRefreshToken(), socialAccount.getExpireTime());
    }

    /**
     * <p>Given a ConnectionData this method returns a SocialAccount containing the data in the ConnectionData. <br />
     * Then each SocialAccount can be persisted in the SocialAccountRepository.</p>
     *
     * @param connectionData The ConnectionData
     * @return The SocialAccount
     */
    private SocialAccount createSocialAccountForConnectionData(final ConnectionData connectionData) {
        final SocialAccount socialAccount = new SocialAccount();

        socialAccount.setUserId(userRepository.findById(userId).getId());
        socialAccount.setProviderId(connectionData.getProviderId());
        socialAccount.setProviderUserId(connectionData.getProviderUserId());
        socialAccount.setDisplayName(connectionData.getDisplayName());
        socialAccount.setProfileUrl(connectionData.getProfileUrl());
        socialAccount.setImageUrl(connectionData.getImageUrl());
        socialAccount.setAccessToken(connectionData.getAccessToken());
        socialAccount.setSecret(connectionData.getSecret());
        socialAccount.setRefreshToken(connectionData.getRefreshToken());
        socialAccount.setExpireTime(connectionData.getExpireTime());

        return socialAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        //multivalue map containing the initial unsorted by providerId elements, but sorted by rank
        final MultiValueMap<String, Connection<?>> unsortedConnections = new LinkedMultiValueMap<>();

        //final multivalue, sorted both by key and rank, containing the user connections
        final MultiValueMap<String, Connection<?>> sortedConnections = new LinkedMultiValueMap<>();

        //the social accounts the user has
        final List<SocialAccount> socialAccounts = socialAccountRepository.findByUserId(userId);

        //sort by rank all social accounts so that each social account type is also sorted
        Collections.sort(socialAccounts, new SocialAccountRankByGraphIdComparator());

        //for each social account, get its factory, its connection data, create the connection and add it to the unsorted
        //by providerId multivaluemap. each list in the multivalue map will be sorted by rank as the list of social accounts
        //has been previously sorted by rank
        for (final SocialAccount socialAccount : socialAccounts) {
            final ConnectionFactory connectionFactory =
                    connectionFactoryLocator.getConnectionFactory(socialAccount.getProviderId());

            final ConnectionData connectionData = createConnectionDataForSocialAccount(socialAccount);
            final Connection connection = connectionFactory.createConnection(connectionData);

            unsortedConnections.add(socialAccount.getProviderId(), connection);
        }

        //get an arraylist of the providerIds from the unsorted multivaluemap and sort it
        final List<String> connectionKeys = new ArrayList<>(unsortedConnections.keySet());
        Collections.sort(connectionKeys);

        //add the sorted by rank connections to the sorted by providerId multivaluemap
        for (final String connectionKey : connectionKeys) {
            final List<Connection<?>> sortedProviderConnections = unsortedConnections.get(connectionKey);

            for (final Connection connection : sortedProviderConnections)
                sortedConnections.add(connectionKey, connection);
        }

        return sortedConnections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Connection<?>> findConnections(final String providerId) {
        //the final list to hold the connections
        final List<Connection<?>> connections = new ArrayList<>();

        //get the connectionfactory for the providerId
        final ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);

        //find the social accounts for the specified providerId for the current user
        final List<SocialAccount> socialAccounts = socialAccountRepository.findByUserIdAndProviderId(userId, providerId);

        //sort the accounts by rank
        Collections.sort(socialAccounts, new SocialAccountRankByGraphIdComparator());

        //for each account create the connectiondata, create the connection and add it to the list.
        //because the socialAccounts list has been previously sorted by rank the final list will also be sorted by rank
        for (final SocialAccount socialAccount : socialAccounts) {
            final ConnectionData connectionData = createConnectionDataForSocialAccount(socialAccount);
            final Connection connection = connectionFactory.createConnection(connectionData);
            connections.add(connection);
        }

        return connections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(final Class<A> connectionClass) {
        final List<?> connections =
                findConnections(connectionFactoryLocator.getConnectionFactory(connectionClass).getProviderId());

        return (List<Connection<A>>) connections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(final MultiValueMap<String, String> providerUsers) {
        final MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<>();

        for (final Map.Entry<String, List<String>> entry : providerUsers.entrySet()) {
            final String providerId = entry.getKey();
            final List<String> providerUserIds = entry.getValue();
            final ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);

            for (final String providerUserId : providerUserIds) {
                final SocialAccount socialAccount =
                        socialAccountRepository.findByUserIdAndProviderIdAndProviderUserId(userId, providerId, providerUserId);

                if (socialAccount != null) {
                    final ConnectionData connectionData = createConnectionDataForSocialAccount(socialAccount);
                    final Connection connection = connectionFactory.createConnection(connectionData);
                    connections.add(providerId, connection);

                } else connections.add(providerId, null);
            }
        }

        return connections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        final SocialAccount socialAccount =
                socialAccountRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());

        if (socialAccount == null)
            throw new NoSuchConnectionException(connectionKey);

        final ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionKey.getProviderId());
        final ConnectionData connectionData = createConnectionDataForSocialAccount(socialAccount);

        return connectionFactory.createConnection(connectionData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(final Class<A> connectionClass, final String providerUserId) {
        final ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionClass);

        final Object connection = getConnection(new ConnectionKey(connectionFactory.getProviderId(), providerUserId));

        return (Connection<A>) connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(final Class<A> connectionClass) {
        final ConnectionFactory connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionClass);
        final List<SocialAccount> socialAccounts =
                socialAccountRepository.findByUserIdAndProviderId(userId, connectionFactory.getProviderId());

        if (socialAccounts.isEmpty()) {
            throw new NotConnectedException(connectionFactory.getProviderId());
        }

        Collections.sort(socialAccounts, new SocialAccountRankByGraphIdComparator());

        final SocialAccount socialAccount = socialAccounts.get(0);
        final ConnectionData connectionData = createConnectionDataForSocialAccount(socialAccount);
        final Object connection = connectionFactory.createConnection(connectionData);

        return (Connection<A>) connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <A> Connection<A> findPrimaryConnection(final Class<A> connectionClass) {
        try {
            return getPrimaryConnection(connectionClass);
        } catch (NotConnectedException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnection(final Connection<?> connection) {
        final ConnectionData connectionData = connection.createData();

        final SocialAccount existingSocialAccount =
                socialAccountRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionData.getProviderId(), connectionData.getProviderUserId());

        if (existingSocialAccount != null)
            throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));

        final SocialAccount socialAccount = createSocialAccountForConnectionData(connectionData);
        socialAccountRepository.save(socialAccount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateConnection(final Connection<?> connection) {
        final ConnectionData connectionData = connection.createData();

        final SocialAccount existingSocialAccount =
                socialAccountRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionData.getProviderId(), connectionData.getProviderUserId());

        final SocialAccount updatedSocialAccount = createSocialAccountForConnectionData(connectionData);
        updatedSocialAccount.setId(existingSocialAccount.getId());

        socialAccountRepository.save(updatedSocialAccount);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnections(final String providerId) {
        final List<SocialAccount> socialAccounts = socialAccountRepository.findByUserIdAndProviderId(userId, providerId);

        socialAccountRepository.delete(socialAccounts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        final SocialAccount socialAccount =
                socialAccountRepository.findByUserIdAndProviderIdAndProviderUserId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());

        if (socialAccount != null)
            socialAccountRepository.delete(socialAccount);
    }
}
