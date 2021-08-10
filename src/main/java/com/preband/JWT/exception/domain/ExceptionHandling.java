package com.preband.JWT.exception.domain;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.preband.JWT.domain.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

//import java.net.http.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandling.class);
    private static final String ACCOUNT_LOCKED = "Your Account has been locked";
    private static final String METHODE_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint";
    private static final String INTERNAL_SERVER_ERROR = "An Error occurred while processing";
    private static final String INCORRECT_CREDENTIAL = "Username or Password incorrect.Try again";
    private static final String ACCOUNT_DISABLE = "Your Account has been disable";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing files";
    private static final String NOT_ENOUGH_PERMISSION= "You Don't Have enough permission";

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST,INCORRECT_CREDENTIAL);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisable(){
        return createHttpResponse(HttpStatus.BAD_REQUEST,ACCOUNT_DISABLE);
    }


    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST,ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException e){
        return createHttpResponse(HttpStatus.UNAUTHORIZED,e.getMessage().toUpperCase());
    }


    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException e){
        return createHttpResponse(HttpStatus.UNAUTHORIZED,e.getMessage().toUpperCase());
    }

    @ExceptionHandler(UserNameExistException.class)
    public ResponseEntity<HttpResponse> userNameExistException(UserNameExistException e){
        return createHttpResponse(HttpStatus.UNAUTHORIZED,e.getMessage().toUpperCase());
    }


    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException e){
        return createHttpResponse(HttpStatus.BAD_REQUEST,e.getMessage().toUpperCase());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException e){
        return createHttpResponse(HttpStatus.BAD_REQUEST,e.getMessage().toUpperCase());
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        HttpMethod supportedMethode = Objects.requireNonNull(e.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED,String.format(METHODE_IS_NOT_ALLOWED,supportedMethode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception e){
        LOGGER.error(e.getMessage().toUpperCase());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> noResultException(IOException e){
        LOGGER.error(e.getMessage().toUpperCase());
        return createHttpResponse(HttpStatus.NOT_FOUND,e.getMessage().toUpperCase());
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException e){
        LOGGER.error(e.getMessage().toUpperCase());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,ERROR_PROCESSING_FILE);
    }


    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus,String message){
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(),httpStatus,httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase());
        return new ResponseEntity<>(httpResponse,httpStatus);
    }
}
