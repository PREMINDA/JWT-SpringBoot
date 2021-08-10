package com.preband.JWT.service;

import com.preband.JWT.domain.User;
import com.preband.JWT.exception.domain.EmailExistException;
import com.preband.JWT.exception.domain.UserNameExistException;
import com.preband.JWT.exception.domain.UserNotFoundException;

import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UserNameExistException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

}
