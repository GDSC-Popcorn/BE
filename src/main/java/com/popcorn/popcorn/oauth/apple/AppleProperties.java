package com.popcorn.popcorn.oauth.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("oauth")
public class AppleProperties {

    private final OauthSecret apple;

    @ConstructorBinding
    public AppleProperties(OauthSecret apple) {
        this.apple = apple;
    }

    @Getter
    @Setter
    public static class OauthSecret {
        private String baseUrl;
        private String clientId;
    }


    public String getAppleBaseUrl() {
        return apple.getBaseUrl();
    }

    public String getAppleClientId() {
        return apple.getClientId();
    }
}
