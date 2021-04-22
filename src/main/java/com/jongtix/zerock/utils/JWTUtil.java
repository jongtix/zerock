package com.jongtix.zerock.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 스프링 환경이 아닌 곳에서 사용할 수 있도록 간단한 유틸리티 클래스로 설계
 */
@Log4j2
public class JWTUtil {

    private String secretKey = "secretKeyForZerockProject"; //Signature 생성을 위한 key

    //1 month
    private long expire = 60 * 24 * 30; //보안을 위한 만료일 설정
//    private long expire = 1; //보안을 위한 만료일 설정

    /**
     * JWT 토큰을 생성하는 역할
     * @param content 사용자의 이메일
     * @return
     * @throws Exception
     */
    public String generateToken(String content) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * 인코딩된 문자열에서 원하는 값을 추출하는 용도
     * @param tokenStr
     * @return
     * @throws Exception
     */
    public String validateAndExtract(String tokenStr) throws Exception {
        String contentValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            contentValue = claims.getSubject();
        } catch (Exception e) { //만료일이 지난 JWT
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return contentValue;
    }
}
