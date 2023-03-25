package ru.marilka888.jeweller.common.exception;

public class UserHaveNotOrder extends RuntimeException{
    public UserHaveNotOrder() {
        super("Заказ не принадлежит пользователю, который его запрашивает");
    }
    public UserHaveNotOrder(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }
}