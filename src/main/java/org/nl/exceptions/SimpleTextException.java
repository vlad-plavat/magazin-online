package org.nl.exceptions;

public class SimpleTextException extends Exception{
    private final String s;

    public SimpleTextException(String s) {
        super(s);
        this.s = s;
    }

    public String getText() {
        return s;
    }
}
