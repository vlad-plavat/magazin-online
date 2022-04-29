package org.nl.exceptions;

public class ProductIDAlreadyExistsException extends Exception {

    private final int ID;

    public ProductIDAlreadyExistsException(int ID) {
        super(String.format("A product with the ID %s already exists!", ID));
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
