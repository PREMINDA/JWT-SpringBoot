package com.preband.JWT.ServiceImplimenta;

import com.preband.JWT.domain.User;
import com.preband.JWT.domain.UserPrinciple;
import com.preband.JWT.enumeration.Role;
import com.preband.JWT.exception.domain.EmailExistException;
import com.preband.JWT.exception.domain.UserNameExistException;
import com.preband.JWT.exception.domain.UserNotFoundException;
import com.preband.JWT.repository.UserRepository;
import com.preband.JWT.service.EmailService;
import com.preband.JWT.service.LoginAttemptService;
import com.preband.JWT.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginAttemptService loginAttemptService;
    private EmailService emailService;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,LoginAttemptService loginAttemptService,EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if(user == null){
            LOGGER.error("User Not Found by Username" + username);
            throw new UsernameNotFoundException("User Not Found by Username" + username);
        }else{
             validateLoginAttempt(user);
             user.setLastLogindateDisplay(user.getLastLoginDate());
             user.setLastLoginDate(new Date());
             userRepository.save(user);
             UserPrinciple userPrinciple = new UserPrinciple(user);
             LOGGER.info("Returning found user by username : "+ username);

            return userPrinciple;
        }

    }

    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()){
            if(loginAttemptService.hasExceededMaxAttempt(user.getUsername())){
                user.setNotLocked(false);
            }else{
                 user.setNotLocked(true);
            }
        }else{
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UserNameExistException {
        validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodePassword = encoddePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTempfileImageUrl());
        return user;
    }

    private String getTempfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
    }

    private String encoddePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomNumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername,String newUsername,String email) throws UserNotFoundException, UserNameExistException, EmailExistException {

        if(StringUtils.isNotBlank(currentUsername)){
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser==null){
                throw new UserNotFoundException("No user found by username "+currentUsername);
            }
            User userByNewUsername = findUserByUsername(newUsername);
            if(userByNewUsername != null && !currentUser.getUserId().equals(userByNewUsername.getUserId())){
                throw new UserNameExistException("Username already exists");
            }
            User userByNewEmail = findUserByEmail(email);
            if(userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getUserId())){
                throw new EmailExistException("Email already Taken");
            }

            return currentUser;
        }else{
            User userByUsername = findUserByUsername(newUsername);
            if(userByUsername != null){
                throw new UserNameExistException("Username already exists");
            }
            User userByEmail = findUserByEmail(email);
            if(userByEmail != null){
                throw new EmailExistException("Email already Taken");
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profile) {
        return null;
    }

    @Override
    public User UpdateUser(String currentName, String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profile) {
        return null;
    }

    @Override
    public User deleteUser(long id) {
        return null;
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) {
        return null;
    }


}
