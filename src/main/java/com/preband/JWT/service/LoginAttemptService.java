package com.preband.JWT.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPT = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String,Integer> loginAttemptCash;



    public LoginAttemptService() {
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

    public  void addUserToLoginAttemptCache(String username){
        int attempts = 0;

        try {
            attempts = ATTEMPT_INCREMENT+loginAttemptCash.get(username);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        loginAttemptCash.put(username,attempts);

    }

    public boolean hasExceededMaxAttempt(String username) {
        try {
            return loginAttemptCash.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPT;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
