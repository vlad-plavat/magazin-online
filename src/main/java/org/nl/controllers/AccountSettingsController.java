package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.exceptions.SimpleTextException;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.exceptions.WrongPasswordException;
import org.nl.exceptions.WrongUsernameException;
import org.nl.services.UserService;


import java.io.IOException;

import static org.nl.controllers.RegistrationController.loggeduser;
import static org.nl.services.UserService.encodePassword;

public class AccountSettingsController {
    @FXML
    private TextField auxField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField oldPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private Text errorField;

    @FXML
    public void initialize() {
        auxField.setVisible(false);
        usernameField.setText(loggeduser.getUsername());
        if(loggeduser.getRole().equals("Client")){
            auxField.setVisible(true);
            auxField.setPromptText("Address");
            auxField.setText(loggeduser.getAux());
        }else if(loggeduser.getRole().equals("Courier")){
            auxField.setVisible(true);
            auxField.setPromptText("Car license plate");
            auxField.setText(loggeduser.getAux());
        }

    }

    @FXML
    public void backToMenu(ActionEvent evt){
        Parent root;
        try {

            root = FXMLLoader.load(Main.class.getClassLoader().getResource("Menus/"+loggeduser.getRole()+".fxml"));
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void saveChanges(ActionEvent evt){
        UserService.readusers();
        String name = usernameField.getText();
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String aux = auxField.getText();
        errorField.setText("");

        try {
            if(name.isBlank() || oldPass.isBlank())
                throw new SimpleTextException("Please enter a username and password.");
            if(!loggeduser.getPassword().equals(encodePassword(loggeduser.getUsername(), oldPass))){
                throw new WrongPasswordException(loggeduser.getUsername());
            }
            if(!newPass.isBlank() && newPass.length()<4)
                throw new SimpleTextException("The password must be at least 4 characters long.");
            if(loggeduser.getRole().equals("Client") && (aux.length()<5 || aux.isBlank()))
                throw new SimpleTextException("Please enter an address.");
            if(loggeduser.getRole().equals("Courier") && (aux.length()<5 || aux.isBlank()))
                throw new SimpleTextException("Please enter the license plate.");
            if(newPass.isBlank())
                newPass = oldPass;
            UserService.changeUserData(name, newPass, aux);

            backToMenu(evt);
            //System.out.println(">>>>>trec la meniu");
        } catch (WrongPasswordException | SimpleTextException | UsernameAlreadyExistsException e) {
            errorField.setText(e.getMessage());
        }

    }


}
