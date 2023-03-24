package ru.marilka888.jeweller.common.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("Пользователь не был найден");
    }
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }
}