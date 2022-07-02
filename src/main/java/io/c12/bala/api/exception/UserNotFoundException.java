package io.c12.bala.api.exception;

public class UserNotFoundException extends RuntimeException {

    public final String id;

    public UserNotFoundException(String id) {
        this.id = id;
    }
}
