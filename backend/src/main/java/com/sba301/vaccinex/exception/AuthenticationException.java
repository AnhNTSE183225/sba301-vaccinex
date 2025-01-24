package com.sba301.vaccinex.exception;

import lombok.Data;

@Data
public class AuthenticationException extends RuntimeException {

    private String message;

    public AuthenticationException(String message) {
        this.message = message;
    }

}
