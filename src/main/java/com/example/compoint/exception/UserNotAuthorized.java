package com.example.compoint.exception;

public class UserNotAuthorized extends Exception{
    public UserNotAuthorized(String message) {
        super(message);
    }
}
