package com.blog.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class TokenUtils {

    private static final long EXPIRE_TIME = 12*60*60*1000;
    private static final String TOKEN_SECRET = "jiangbfy";

    public static String sign(String password) {
        String token= "";
        try {
            Date expireAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    //发行人
                    .withIssuer("jiangbfy")
                    //存放数据
                    .withClaim("password", password)
                    //过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static String verify(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("jiangbfy").build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
//            log.info("password:" + decodedJWT.getClaim("password").asString());
//            log.info("过期时间:" + decodedJWT.getExpiresAt());
            return decodedJWT.getClaim("password").asString();
        }catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
