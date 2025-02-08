package com.popcorn.popcorn.oauth.kakao.client;

import com.popcorn.popcorn.oauth.kakao.dto.OIDCPublickeyResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoOauthClient",
        url = "https://kauth.kakao.com"
)
public interface KakaoOauthClient{

    @Cacheable(value = "KakaoOauth", cacheManager = "oidcCacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublickeyResponse getOIDCPublickeys();
}
