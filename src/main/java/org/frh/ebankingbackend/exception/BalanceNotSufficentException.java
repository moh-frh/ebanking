package org.frh.ebankingbackend.exception;
// Exception : exception surveillé
public class BalanceNotSufficentException extends Exception {
    public BalanceNotSufficentException(String message) {
        super(message);
    }
}
