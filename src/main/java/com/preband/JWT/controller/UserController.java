package com.preband.JWT.controller;

import com.preband.JWT.domain.User;
import com.preband.JWT.exception.domain.EmailExistException;
import com.preband.JWT.exception.domain.ExceptionHandling;
import com.preband.JWT.exception.domain.UserNameExistException;
import com.preband.JWT.exception.domain.UserNotFoundException;
import com.preband.JWT.repository.UserRepository;
import com.preband.JWT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/","/user"})
public class UserController extends ExceptionHandling {

    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/")
    public ResponseEntity<User> getUser()  {

        User newuser = userService.findUserByEmail("Jhon@example.com");

        return new ResponseEntity<>(newuser, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws EmailExistException, UserNotFoundException, UserNameExistException {

       User newuser =  userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail());

       return new ResponseEntity<>(newuser, HttpStatus.OK);
    }

}
