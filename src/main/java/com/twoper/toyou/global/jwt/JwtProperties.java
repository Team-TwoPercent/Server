package com.twoper.toyou.global.jwt;

public interface JwtProperties {
    // 서버만 알고 있는 시크릿 키
    String SECRET = "sosow0212";
    int EXPIRATION_TIME = 60000 * 24; // 10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

}
