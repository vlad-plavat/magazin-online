package org.nl.exceptions;

public class WrongUsernameException extends Exception {
    private String username;

    public WrongUsernameException(String username) {
        super(String.format("The user %s does not exist!", username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
