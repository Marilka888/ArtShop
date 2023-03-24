package ru.marilka888.jeweller.common.exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException() {
        super("Пользователь уже является клиентом Jeweller Shop");
    }
    public UserAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }

}