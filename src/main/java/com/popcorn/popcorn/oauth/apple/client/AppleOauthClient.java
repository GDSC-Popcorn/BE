package com.popcorn.popcorn.oauth.apple.client;

import com.popcorn.popcorn.oauth.kakao.dto.OIDCPublickeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "AppleOauthClient",
        url = "https://appleid.apple.com"
)
public interface AppleOauthClient {

    @GetMapping("/auth/keys")
    OIDCPublickeyResponse getAppleOIDCOpenKeys();
}
