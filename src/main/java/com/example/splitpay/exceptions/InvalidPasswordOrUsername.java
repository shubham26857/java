package com.example.splitpay.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidPasswordOrUsername extends RuntimeException{
    public InvalidPasswordOrUsername(String message){
        super(message);
    }
}
