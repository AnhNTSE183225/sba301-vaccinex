package com.sba301.vaccinex.exception;

import lombok.Data;

@Data
public class ElementNotFoundException extends RuntimeException {

    private String message;

    public ElementNotFoundException(String message) {
        this.message = message;
    }

}
