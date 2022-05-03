package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.exceptions.SimpleTextException;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.exceptions.WrongPasswordException;
import org.nl.exceptions.WrongUsernameException;
import org.nl.model.User;
import org.nl.services.StageService;
import org.nl.services.UserService;

import java.io.IOException;

public class RegistrationController {

    public static User loggeduser;
    @FXML
    public Button toRegButton;
    @FXML
    private Button regButton;
    @FXML
    private Button toLoginButton;
    @FXML
    private Text registrationMessage;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox<String> role;
    @FXML
    private TextField auxField;
    @FXML
    private Button loginButton;


    @FXML
    public void initialize() {
        role.getItems().addAll("Client", "Manager","Worker","Courier");
        role.setValue("Client");
        role.managedProperty().bind(role.visibleProperty());
        auxField.managedProperty().bind(auxField.visibleProperty());
        toLoginButton.managedProperty().bind(toLoginButton.visibleProperty());
        toRegButton.managedProperty().bind(toRegButton.visibleProperty());
        regButton.managedProperty().bind(regButton.visibleProperty());
        role.setVisible(false);
        auxField.setVisible(false);
        toLoginButton.setVisible(false);
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
        if(role.getValue().equals("Client")){
            auxField.setVisible(true);
            auxField.setPromptText("Address");
        }else if(role.getValue().equals("Courier")){
            auxField.setVisible(true);
            auxField.setPromptText("Car license plate");
        }

    }
    @FXML
    public void backToLogin(){
        role.setVisible(false);
        auxField.setVisible(false);
        toLoginButton.setVisible(false);
        toRegButton.setVisible(true);
        regButton.setVisible(false);
        loginButton.setVisible(true);
        registrationMessage.setText("");

    }
    @FXML
    public void goToRegister(){
        role.setVisible(true);
        auxField.setVisible(true);
        toLoginButton.setVisible(true);
        toRegButton.setVisible(false);
        regButton.setVisible(true);
        loginButton.setVisible(false);
        registrationMessage.setText("");

    }
    @FXML
    public void handleLoginAction(ActionEvent evt){
        UserService.readusers();
        String name = usernameField.getText();
        String pass = passwordField.getText();

        try {
            if(name.isBlank() || pass.isBlank())
                throw new SimpleTextException("Please enter a username and password.");
            loggeduser = UserService.checkLoginCredentials(name,pass);
            goToMenu(evt);
            //System.out.println(">>>>>trec la meniu");
        } catch (WrongPasswordException | SimpleTextException | WrongUsernameException e) {
            registrationMessage.setText(e.getMessage());
        }
    }
    @FXML
    public void handleRegisterAction(ActionEvent evt) {
        String name = usernameField.getText();
        String pass = passwordField.getText();
        String aux = auxField.getText();
        String rolestr = role.getValue();

        try {
            if(name.isBlank()) {
                throw new SimpleTextException("Please enter a username.");
            }
            if(pass.length()<4)
                throw new SimpleTextException("The password must be at least 4 characters long.");
            if(rolestr.equals("Client") && aux.length()<5)
                throw new SimpleTextException("Please enter an address.");
            if(rolestr.equals("Courier") && aux.length()<5)
                throw new SimpleTextException("Please enter the license plate.");
            if(!rolestr.equals("Client") && !rolestr.equals("Courier"))
                aux = "";
            loggeduser = UserService.addUser(usernameField.getText(), passwordField.getText(), role.getValue(), aux);
            registrationMessage.setText("Account created successfully!");
            goToMenu(evt);
            //System.out.println(">>>>>>>meniu");

        } catch (UsernameAlreadyExistsException | SimpleTextException e) {
            registrationMessage.setText(e.getMessage());
        }
        UserService.readusers();
    }

    private void goToMenu(ActionEvent evt){
        StageService.loadPage(evt,"Menus/"+loggeduser.getRole()+".fxml");
    }
}
