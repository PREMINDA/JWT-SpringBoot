package com.preband.JWT.controller;

import com.preband.JWT.constant.SecurityConstant;
import com.preband.JWT.domain.HttpResponse;
import com.preband.JWT.domain.User;
import com.preband.JWT.domain.UserPrinciple;
import com.preband.JWT.exception.domain.EmailExistException;
import com.preband.JWT.exception.domain.ExceptionHandling;
import com.preband.JWT.exception.domain.UserNameExistException;
import com.preband.JWT.exception.domain.UserNotFoundException;

import com.preband.JWT.service.UserService;
import com.preband.JWT.utility.JWTTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import static com.preband.JWT.constant.FileConstant.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/","/user"})
public class UserController extends ExceptionHandling {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;


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

       User newUser =  userService.register(user.getFirstName(),user.getLastName(),user.getUsername(),user.getPassword(),user.getEmail());

       return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("userName") String userName,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked

                                           ) throws UserNotFoundException, EmailExistException, UserNameExistException, IOException {
        User newUser = userService.addNewUser(firstName,lastName,userName,email,password,role,Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive));
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }


    @PostMapping("/update")
    public ResponseEntity<User> update(    @RequestParam("currentName") String currentName,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("userName") String userName,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isActive") String isActive,
                                           @RequestParam("isNonLocked") String isNonLocked

    ) throws UserNotFoundException, EmailExistException, UserNameExistException, IOException {
        User updateUser = userService.updateUser(currentName,firstName,lastName,userName,email,role,Boolean.parseBoolean(isNonLocked),Boolean.parseBoolean(isActive));
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") long id){
        userService.deleteUser(id);
        return response(HttpStatus.NO_CONTENT,"User Deleted successfully");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase(Locale.ROOT)),httpStatus);
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
