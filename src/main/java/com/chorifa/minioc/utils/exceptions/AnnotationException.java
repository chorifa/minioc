package com.chorifa.minioc.utils.exceptions;

public class AnnotationException extends RuntimeException {
    public AnnotationException(String message) {
        super(message);
    }

    public AnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationException(Throwable cause) {
        super(cause);
    }
}
