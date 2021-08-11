package com.preband.JWT.repository;

import com.preband.JWT.domain.User;
import com.preband.JWT.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class UserRepositoryTest {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void getuser(){
        User newuser =userRepository.findUserByEmail("Jhon@example.com");
        System.out.println(newuser);
    }

    @Test
    public void wr(){

    }
}