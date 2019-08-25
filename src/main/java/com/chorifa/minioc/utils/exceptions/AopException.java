package com.chorifa.minioc.utils.exceptions;

public class AopException extends RuntimeException{
    public AopException() {
        super();
    }

    public AopException(String message) {
        super(message);
    }

    public AopException(String message, Throwable cause) {
        super(message, cause);
    }

    public AopException(Throwable cause) {
        super(cause);
    }
}
