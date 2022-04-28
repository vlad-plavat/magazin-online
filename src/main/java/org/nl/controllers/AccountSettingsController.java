package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nl.Main;


import java.io.IOException;

import static org.nl.controllers.RegistrationController.loggeduser;

public class AccountSettingsController {
    @FXML
    private TextField auxField;
    @FXML
    private TextField usernameField;

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
    public void saveChanges(){
        System.out.println("Save");

    }


}
