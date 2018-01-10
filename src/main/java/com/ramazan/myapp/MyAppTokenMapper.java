package com.ramazan.myapp;

public interface MyAppTokenMapper {
    void addToken(String token);
    int countTokens(String token);
    void deleteToken(String token);
}
