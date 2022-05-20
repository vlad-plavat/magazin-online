package org.nl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nl.exceptions.*;
import org.nl.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionTest {
    @Test
    @DisplayName("OutOfStockException works")
    void testOutOfStockException() {
        OutOfStockException e = new OutOfStockException();
        assertThat(e).isNotNull();
    }

    @Test
    @DisplayName("ProductIDAlreadyExistsException works")
    void testProductIDAlreadyExistsException() {
        ProductIDAlreadyExistsException e = new ProductIDAlreadyExistsException(123);
        assertThat(e.getID()).isEqualTo(123);
        assertThat(e.getMessage()).isEqualTo("A product with the ID 123 already exists!");
    }

    @Test
    @DisplayName("SimpleTextException works")
    void testSimpleTextException() {
        SimpleTextException e = new SimpleTextException("sample");
        assertThat(e.getText()).isEqualTo("sample");
        assertThat(e.getMessage()).isEqualTo("sample");

    }

    @Test
    @DisplayName("WrongPasswordException works")
    void testWrongPasswordException() {
        WrongPasswordException e = new WrongPasswordException("sample");
        assertThat(e.getUsername()).isEqualTo("sample");
        assertThat(e.getMessage()).isEqualTo("The password for the user sample is incorrect!");

    }

    @Test
    @DisplayName("WrongUsernameException works")
    void testWrongUsernameException() {
        WrongUsernameException e = new WrongUsernameException("sample");
        assertThat(e.getUsername()).isEqualTo("sample");
        assertThat(e.getMessage()).isEqualTo("The user sample does not exist!");

    }

    @Test
    @DisplayName("UsernameAlreadyExistsException works")
    void testUsernameAlreadyExistsException() {
        UsernameAlreadyExistsException e = new UsernameAlreadyExistsException("sample");
        assertThat(e.getUsername()).isEqualTo("sample");
        assertThat(e.getMessage()).isEqualTo("An account with the username sample already exists!");

    }
}
