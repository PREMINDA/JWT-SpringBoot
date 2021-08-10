package com.preband.JWT.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPT = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String,Integer> loginAttemptCash;

    public LoginAttemptService(LoadingCache<String, Integer> loginAttemptCash) {
        loginAttemptCash = CacheBuilder.newBuilder().expireAfterAccess(15,MINUTES).maximumSize(100).build(
                new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) {
                        return 0;
                    }
                }
        );
    }
    public void evictUserFromLoginAttemptCache(String username){
        loginAttemptCash.invalidate(username);
    }

    public  void addUserToLoginAttemptCache(String username) throws ExecutionException{
        int attempts = 0;

            attempts = ATTEMPT_INCREMENT+loginAttemptCash.get(username);
            loginAttemptCash.put(username,attempts);

    }

    public boolean hasExceededMaxAttempt(String username) throws ExecutionException {
        return loginAttemptCash.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPT;
    }
}
