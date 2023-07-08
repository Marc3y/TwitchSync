package de.kenjih.twitchsync.utils.objects;

public class TokenSetting {

    private String id;
    private String accessToken;
    private String refreshToken;

    public TokenSetting(String id, String accessToken, String refreshToken){
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
