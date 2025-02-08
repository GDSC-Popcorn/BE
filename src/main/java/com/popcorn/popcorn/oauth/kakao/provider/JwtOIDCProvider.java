package com.popcorn.popcorn.oauth.kakao.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.popcorn.common.exception.ExpiredTokenException;
import com.popcorn.popcorn.common.exception.InvalidTokenException;
import com.popcorn.popcorn.oauth.kakao.dto.OIDCDecodePayload;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtOIDCProvider implements OauthOIDCProvider{

    private final String KID = "kid";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
        try {
            String unsignedTokenHeader = getUnsignedToken(token);
            String headerJson = new String(Base64.getUrlDecoder().decode(unsignedTokenHeader));
            Map<String, Object> headerMap = objectMapper.readValue(headerJson, new TypeReference<Map<String, Object>>() {});
            return (String) headerMap.get(KID);
        } catch (Exception e) {
            log.error("Error extracting kid from token header: {}", e.toString());
            throw InvalidTokenException.EXCEPTION;
        }

    }



    /**
     * Token의 signature를 제거하는 메서드 (header, payload만 분리) 세 부분에서 머리, 몸통만 가져옴
     *
     * -> 헤더만 가져오는 걸로 변경
     */
    private String getUnsignedToken(String token){
        String[] splitToken = token.split("\\.");
        if(splitToken.length != 3)throw InvalidTokenException.EXCEPTION;
        return splitToken[0];
    }



    //2,3,4,5번과정 토큰에서 서명 부분을 제거한 후(즉, header와 payload만 있는 상태) 클레임을 파싱   -> 서명 부분만 제거한 토큰을 그대로 parseSignedClaims에 넣으면 JJWT 라이브러리에서 서명이 없다고 오류 발생
    /*private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) {
        try {
            return Jwts.parser()
                    .requireAudience(aud)  //aud(팝콘 카카오톡 어플리케이션 아이디) 가 같은지 확인
                    .requireIssuer(iss)    //이슈어가 카카오인지 확인
                    .build()
                    .parseSignedClaims(getUnsignedToken(token))
        } catch (ExpiredJwtException e){
            throw ExpiredTokenException.EXCEPTION;
        } catch (Exception e){
            log.error(e.toString());
            throw InvalidTokenException.EXCEPTION;
        }
    }*/

    /**
     * 공개키로 서명을 검증하는 메서드  -> && payload도 검증
     */
    private Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent, String iss, String aud) {
        try {
            return Jwts.parser()
                    .verifyWith(getRSAPublickey(modulus, exponent))
                    .requireIssuer(iss)
                    .requireAudience(aud)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e){
            throw ExpiredTokenException.EXCEPTION;
        } catch (Exception e){
            log.error(e.toString());
            throw InvalidTokenException.EXCEPTION;
        }
    }

    /**
     * n, e 조합으로 공개키를 생성하는 메서드
     */
    private PublicKey getRSAPublickey(String modulns, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulns);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);

    }


    @Override
    public OIDCDecodePayload getOIDCTokenBody(String token, String modulus, String exponent, String iss, String aud) {
        Claims payload = getOIDCTokenJws(token, modulus, exponent, iss, aud).getPayload();
        return new OIDCDecodePayload(
                payload.getIssuer(),
                payload.getAudience().toString(),
                payload.getSubject(),
                payload.get("email", String.class));
    }
}
