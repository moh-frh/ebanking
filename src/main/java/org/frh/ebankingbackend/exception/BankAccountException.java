package org.frh.ebankingbackend.exception;

// Exception : exception surveillé
public class BankAccountException extends Exception {
    public BankAccountException(String message) {
        super(message);
    }
}
