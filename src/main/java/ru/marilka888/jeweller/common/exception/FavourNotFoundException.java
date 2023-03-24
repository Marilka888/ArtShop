package ru.marilka888.jeweller.common.exception;

public class FavourNotFoundException extends RuntimeException{
    public FavourNotFoundException() {
        super("Услуга не была найдена");
    }
    public FavourNotFoundException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }
}