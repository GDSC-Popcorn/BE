package com.popcorn.popcorn;

import com.popcorn.popcorn.oauth.kakao.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableConfigurationProperties(value = KakaoProperties.class)
@EnableFeignClients
@SpringBootApplication
public class PopcornApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopcornApplication.class, args);
	}

}
