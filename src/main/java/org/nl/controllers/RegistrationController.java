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
import org.nl.services.UserService;

import java.io.IOException;

public class RegistrationController {

    public static User loggeduser;
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
        if(role.getValue().equals("Client")){
            auxField.setVisible(true);
            auxField.setPromptText("Address");
        }else if(role.getValue().equals("Courier")){
            auxField.setVisible(true);
            auxField.setPromptText("Car license plate");
        }

    }@FXML
    public void backToLogin(){
        role.setVisible(false);
        auxField.setVisible(false);
        backButton.setVisible(false);
        regButton.setVisible(false);
        loginButton.setVisible(true);
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
        } catch (WrongPasswordException | SimpleTextException e) {
            registrationMessage.setText(e.getMessage());
        } catch (WrongUsernameException e) {
            registrationMessage.setText(e.getMessage() + " Create an account for " + name);
            role.setVisible(true);
            auxField.setVisible(true);
            backButton.setVisible(true);
            regButton.setVisible(true);
            loginButton.setVisible(false);
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
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getClassLoader().getResource("Menus/"+loggeduser.getRole()+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
