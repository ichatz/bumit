package club.bumit.repository;

import club.bumit.model.SocialAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SocialAccountRepository extends MongoRepository<SocialAccount, String> {

    /**
     * <p>Returns a List of SocialAccounts associated with the User of the given id</p>
     *
     * @param userId The User's Id
     * @return A List of SocialAccounts or an empty List if none is found
     */
    public List<SocialAccount> findByUserId(final String userId);

    /**
     * <p>Returns a List of SocialAccounts for a given provider. (eg. "facebook").</p>
     *
     * @param providerId The provider's id
     * @return A List of SocialAccounts or an empty List if none is found.
     */
    public List<SocialAccount> findByProviderId(final String providerId);

    /**
     * <p>Returns a List of SocialAccounts associated with a user for a given provider id.</p>
     *
     * @param userId     The User's Id
     * @param providerId The provider's id
     * @return A List of Social Accounts or an empty List if none is found.
     */
    public List<SocialAccount> findByUserIdAndProviderId(final String userId,
                                                         final String providerId);

    /**
     * <p>Returns a SocialAccount associated with a User for a given provider id and the provider user's id. <br />
     * This combination is guaranteed to be unique so we will either find one or none.</p>
     *
     * @param userId         The User's Id
     * @param providerId     The provider's id
     * @param providerUserId The provider's userId
     * @return A SocialAccount or null if none is found
     */
    public SocialAccount findByUserIdAndProviderIdAndProviderUserId(final String userId,
                                                                    final String providerId,
                                                                    final String providerUserId);

    /**
     * <p>Returns a List of SocialAccounts for a given providerId and provider user's id. <br />
     * This method returns a List as a single provider account might be associated with multiple local accounts.</p>
     *
     * @param providerId     The provider's id
     * @param providerUserId The provider's userId
     * @return A List of SocialAccounts or an empty List if none is found.
     */
    public List<SocialAccount> findByProviderIdAndProviderUserId(final String providerId,
                                                                 final String providerUserId);

}
