package com.jwt.tutorial.jwt;

public interface JwtProperties {
    String SECRET = "cos";
    int EXPIRATION_TIME = 60000*2;                         //10분
    String TOKEN_PROFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    String SECRET_REFRESH = "cosrefresh";
    int EXPIRATION_TIME_REFRESH = 60000*60*24*14;                 //1분 * 60 -> 1시간 * 24 -> 하루 * 14 -> 2주
    String TOKEN_PROFIX_REFRESH = "REFRESH ";
    String HEADER_STRING_REFRESH = "RefreshToken";

}
