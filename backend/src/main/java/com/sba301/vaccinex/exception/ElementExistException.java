package com.sba301.vaccinex.exception;

import lombok.Data;

@Data
public class ElementExistException extends RuntimeException {

    private String message;

    public ElementExistException(String message) {
        this.message = message;
    }

}
