package com.mprodev.ppmtool.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

/* Mirshod created on 2/12/2021 */
@ResponseStatus
public class ProjectIdException extends RuntimeException {
    public ProjectIdException(String message) {
        super(message);
    }
}
