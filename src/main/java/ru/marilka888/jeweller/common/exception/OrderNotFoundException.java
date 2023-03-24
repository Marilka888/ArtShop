package ru.marilka888.jeweller.common.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {
        super("Заказ не был найден");
    }
    public OrderNotFoundException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }

}
