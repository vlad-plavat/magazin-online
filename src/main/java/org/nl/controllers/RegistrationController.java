package org.nl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.services.UserService;

public class RegistrationController {

    @FXML
    private Button regButton;
    @FXML
    private Button backButton;
    @FXML
    private Text registrationMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;
    @FXML
    private TextField auxField;
    @FXML
    private Button loginButton;


    @FXML
    public void initialize() {
        role.getItems().addAll("Client", "Manager","Lucrator","Curier");
        role.setValue("Client");
        role.managedProperty().bind(role.visibleProperty());
        auxField.managedProperty().bind(auxField.visibleProperty());
        backButton.managedProperty().bind(backButton.visibleProperty());
        regButton.managedProperty().bind(regButton.visibleProperty());
        role.setVisible(false);
        auxField.setVisible(false);
        backButton.setVisible(false);
        regButton.setVisible(false);
    }

    /*
    Client - adresa
    Manager -
    Lucrator -
    Curier - nr inmatriculare
     */
    @FXML
    public void modificaCampuri(){
        auxField.setVisible(false);
        if(role.getValue().toString().equals("Client")){
            auxField.setVisible(true);
            auxField.setPromptText("Adresa");
        }else if(role.getValue().toString().equals("Curier")){
            auxField.setVisible(true);
            auxField.setPromptText("Nr. inmatriculare");
        }

    }@FXML
    public void backToLogin(){
        role.setVisible(false);
        auxField.setVisible(false);
        backButton.setVisible(false);
        regButton.setVisible(false);
        loginButton.setVisible(true);

    }
    @FXML
    public void handleLoginAction(){
        UserService.readusers();

        if (!usernameField.getText().isEmpty() && usernameField.getText().charAt(0) == 'r') {
            role.setVisible(true);
            auxField.setVisible(true);
            backButton.setVisible(true);
            regButton.setVisible(true);
            loginButton.setVisible(false);
        }
        System.out.println("am incercat sa ma loghez");
    }
    @FXML
    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue());
            registrationMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        }
        UserService.readusers();
    }
}
