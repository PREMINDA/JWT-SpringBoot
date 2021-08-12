package com.preband.JWT.service;

import com.preband.JWT.domain.User;
import com.preband.JWT.exception.domain.EmailExistException;
import com.preband.JWT.exception.domain.UserNameExistException;
import com.preband.JWT.exception.domain.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UserNameExistException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profile) throws UserNotFoundException, EmailExistException, UserNameExistException, IOException;

    User updateUser(String currentName,String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profilePicture) throws UserNotFoundException, EmailExistException, UserNameExistException, IOException;

    void deleteUser(long id);

    User updateProfileImage(String username,MultipartFile profileImage);

}
