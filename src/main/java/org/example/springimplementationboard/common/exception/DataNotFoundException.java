package org.example.springimplementationboard.common.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {
    private final Object data;

    public DataNotFoundException(Object data, String message) {
        super(message);
        this.data = data;
    }
}
