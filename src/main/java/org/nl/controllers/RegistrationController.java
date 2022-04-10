package org.nl.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.services.UserService;

public class RegistrationController {

    @FXML
    private Text registrationMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;
    @FXML
    private Label adresaText,nrText;
    @FXML
    private TextField adresaField,nrField;


    @FXML
    public void initialize() {
        role.getItems().addAll("Client", "Manager","Lucrator","Curier");
        role.setValue("Client");
        adresaText.managedProperty().bind(adresaText.visibleProperty());
        adresaField.managedProperty().bind(adresaField.visibleProperty());
        nrText.managedProperty().bind(nrText.visibleProperty());
        nrField.managedProperty().bind(nrField.visibleProperty());
    }

    /*
    Client - adresa
    Manager -
    Lucrator -
    Curier - nr inmatriculare
     */
    @FXML
    public void modificaCampuri(){
        adresaText.setVisible(false);
        adresaField.setVisible(false);
        nrText.setVisible(false);
        nrField.setVisible(false);
        if(role.getValue().toString().equals("Client")){
            adresaText.setVisible(true);
            adresaField.setVisible(true);
        }else if(role.getValue().toString().equals("Curier")){
            nrText.setVisible(true);
            nrField.setVisible(true);
        }

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
