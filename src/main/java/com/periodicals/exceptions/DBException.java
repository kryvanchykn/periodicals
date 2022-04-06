package com.periodicals.exceptions;

/**
 * custom checked exception for database errors
 */
public class DBException extends Exception {
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(String message) {
        super(message);
    }
}

