package io.c12.bala.api.exception;

import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException implements Supplier<UserNotFoundException> {

    public UserNotFoundException(String id) {
        super("User not found for id: " + id);
    }

    @Override
    public UserNotFoundException get() {
        return this;
    }
}
