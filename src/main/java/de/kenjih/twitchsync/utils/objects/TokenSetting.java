package de.kenjih.twitchsync.utils.objects;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;

import java.util.Optional;

public class TokenSetting {

    private String id;
    private String accessToken;
    private String refreshToken;

    public TokenSetting(String id, String accessToken, String refreshToken){
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenSetting refresh(String clientId, String clientSecret, String redirectUri){
        TwitchIdentityProvider tip = new TwitchIdentityProvider(clientId, clientSecret, redirectUri);
        OAuth2Credential credential = new OAuth2Credential("twitch", getAccessToken());
        credential.setRefreshToken(getRefreshToken());
        Optional<OAuth2Credential> newCredential = tip.refreshCredential(credential);
        if(!newCredential.isPresent()){
            System.out.println("Token refresh of id " + getId() + " failed");
            return this;
        }
        setAccessToken(newCredential.get().getAccessToken());
        setRefreshToken(newCredential.get().getRefreshToken());
        return this;
    }

    public String getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
