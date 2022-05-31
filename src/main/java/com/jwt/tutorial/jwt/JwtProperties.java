package com.jwt.tutorial.jwt;

public interface JwtProperties {
    String SECRET = "cos";
    int EXPIRATION_TIME = 60000*10;
    String TOKEN_PROFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
