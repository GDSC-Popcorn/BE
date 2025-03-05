package com.popcorn.popcorn;

import com.popcorn.popcorn.oauth.apple.AppleProperties;
import com.popcorn.popcorn.oauth.kakao.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties(value = {AppleProperties.class, KakaoProperties.class})
@EnableFeignClients
@SpringBootApplication
public class PopcornApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopcornApplication.class, args);
	}

}
