package com.periodicals.exceptions;

/**
 * custom not checked exception
 * used for displaying error page on website
 */
public class AppException extends RuntimeException {
    public AppException(Throwable cause) {
        super(cause);
    }
}