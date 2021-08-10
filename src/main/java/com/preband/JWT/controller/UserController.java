package com.preband.JWT.controller;

import com.preband.JWT.constant.SecurityConstant;
import com.preband.JWT.domain.User;
import com.preband.JWT.domain.UserPrinciple;
import com.preband.JWT.exception.domain.EmailExistException;
import com.preband.JWT.exception.domain.ExceptionHandling;
import com.preband.JWT.exception.domain.UserNameExistException;
import com.preband.JWT.exception.domain.UserNotFoundException;
import com.preband.JWT.repository.UserRepository;
import com.preband.JWT.service.UserService;
import com.preband.JWT.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/","/user"})
public class UserController extends ExceptionHandling {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;


    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws EmailExistException, UserNotFoundException, UserNameExistException {

        authenticate(user.getUsername(),user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrinciple userPrinciple = new UserPrinciple(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrinciple);
        return new ResponseEntity<>(loginUser,jwtHeader, HttpStatus.OK);
    }



    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws EmailExistException, UserNotFoundException, UserNameExistException {

       User newuser =  userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getEmail());

       return new ResponseEntity<>(newuser, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(UserPrinciple user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKE_HEADER,jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

}
