package com.sba301.vaccinex.exception;

import lombok.Data;

@Data
public class UnchangedStateException extends RuntimeException {
    private String message;

    public UnchangedStateException(String message) {
        this.message = message;
    }
}
