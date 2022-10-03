package org.asvladimirov.exception;

public class SQLTransactionException extends RuntimeException{

    private static final String EXCEPTION_MESSAGE = "Ошибка при работе с базой данных : %s";

    private String cause;

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE,cause);
    }

    public SQLTransactionException(String cause) {
        this.cause = cause;
    }
}
