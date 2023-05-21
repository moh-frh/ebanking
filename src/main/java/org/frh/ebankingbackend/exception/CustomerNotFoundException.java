package org.frh.ebankingbackend.exception;

// exception surveillé : (Exception) stopped in try/catch et exception no-surveillé : (RuntimeException)
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
