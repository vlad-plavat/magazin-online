package org.nl.exceptions;

public class WrongPasswordException extends Exception {
    private String username;

    public WrongPasswordException(String username) {
        super(String.format("The password for the user %s is incorrect!", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
