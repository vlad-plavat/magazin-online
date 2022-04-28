package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.nl.Main;

import java.io.IOException;

public class WorkerMenuController {
    @FXML
    public void goToOrderProcessing(){
        System.out.println("OrderProcessing");

    }
    @FXML
    public void goToSettings(ActionEvent evt){
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getClassLoader().getResource("accountSettings.fxml"));
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @FXML
    public void logOut(ActionEvent evt){
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getClassLoader().getResource("register.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        RegistrationController.loggeduser = null;

    }
}
