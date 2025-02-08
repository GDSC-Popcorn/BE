package com.popcorn.popcorn.oauth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@ConfigurationProperties("oauth")
public class KakaoProperties {

    private final OauthSecret kakao;  //변수명으로 자동 매핑됨 apple추가하면 됟듯

    @Getter
    @Setter
    public static class OauthSecret {
        private String baseUrl;
        private String appKey;
    }

    public String getKakaoBaseUrl() {
        return kakao.getBaseUrl();
    }
    public String getKakaoAppKey() {
        return kakao.getAppKey();
    }
}
