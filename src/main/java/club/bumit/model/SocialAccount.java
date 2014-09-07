package club.bumit.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "socialaccounts")
public class SocialAccount
        implements Serializable {

    /**
     * Mongodb id.
     */
    @Id
    private String id;

    private String userId;

    /**
     * <p>The providerId of the Connection<?></p>
     */
    private String providerId;

    /**
     * <p>The providerUserId of the Connection<?></p>
     */
    private String providerUserId;

    /**
     * <p>The displayName of the Connection<?></p>
     */
    private String displayName;

    /**
     * <p>The profileUrl of the Connection<?></p>
     */
    private String profileUrl;

    /**
     * <p>The imageUrl of the Connection<?></p>
     */
    private String imageUrl;

    /**
     * <p>The accessToken of the Connection<?></p>
     */
    private String accessToken;

    /**
     * <p>The secret of the Connection<?></p>
     */
    private String secret;

    /**
     * <p>The refreshToken of the Connection<?></p>
     */
    private String refreshToken;

    /**
     * <p>The expireTime of the Connection<?></p>
     */
    private Long expireTime;

    /**
     * <p>Sets the id of this SocialAccount</p>
     *
     * @param id The id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * <p>Returns the id of this SocialAccount</p>
     *
     * @return The id
     */
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * <p>Sets the providerId of this SocialAccount</p>
     *
     * @param providerId The providerId
     */
    public void setProviderId(final String providerId) {
        this.providerId = providerId;
    }

    /**
     * <p>Returns the providerId of this SocialAccount</p>
     *
     * @return The providerId
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * <p>Sets the providerUserId of this SocialAccount</p>
     *
     * @param providerUserId The providerUserId
     */
    public void setProviderUserId(final String providerUserId) {
        this.providerUserId = providerUserId;
    }

    /**
     * <p>Returns the providerUserId of this SocialAccount</p>
     *
     * @return The providerUserId
     */
    public String getProviderUserId() {
        return providerUserId;
    }

    /**
     * <p>Sets the displayName of this SocialAccount</p>
     *
     * @param displayName The displayName
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * <p>Returns the displayName of this SocialAccount</p>
     *
     * @return The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * <p>Sets the profileUrl of this SocialAccount</p>
     *
     * @param profileUrl The profileUrl
     */
    public void setProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }

    /**
     * <p>Returns the profileUrl of this SocialAccount</p>
     *
     * @return The profileUrl
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * <p>Sets the imageUrl of this SocialAccount</p>
     *
     * @param imageUrl The imageUrl
     */
    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * <p>Returns the imageUrl of this SocialAccount</p>
     *
     * @return The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * <p>Sets the accessToken of this SocialAccount</p>
     *
     * @param accessToken The accessToken
     */
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * <p>Returns the accessToken of this SocialAccount</p>
     *
     * @return The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * <p>Sets the secret of this SocialAccount</p>
     *
     * @param secret The secret
     */
    public void setSecret(final String secret) {
        this.secret = secret;
    }

    /**
     * <p>Returns the secret of this SocialAccount</p>
     *
     * @return The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * <p>Sets the refreshToken of this SocialAccount</p>
     *
     * @param refreshToken The refreshToken
     */
    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * <p>Returns the refreshToken of this SocialAccount</p>
     *
     * @return The refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * <p>Sets the expireTime of this SocialAccount</p>
     *
     * @param expireTime The expireTime
     */
    public void setExpireTime(final Long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * <p>Returns the expireTime of this SocialAccount</p>
     *
     * @return The expireTime
     */
    public Long getExpireTime() {
        return expireTime;
    }
}
