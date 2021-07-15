package com.mprodev.ppmtool.exceptions;

public class UserNameAlreadyExistExceptionResponse {
    private String username;

    public UserNameAlreadyExistExceptionResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
