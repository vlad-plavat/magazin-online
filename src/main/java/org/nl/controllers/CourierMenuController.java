package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.services.StageService;

import java.io.IOException;

public class CourierMenuController {
    @FXML
    public void goToDelivery(ActionEvent evt){
        StageService.loadPage(evt,"delivery.fxml");

    }

    @FXML
    public void goToSettings(ActionEvent evt){
        StageService.loadPage(evt,"accountSettings.fxml");
    }

    @FXML
    public void logOut(ActionEvent evt){
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getClassLoader().getResource("register.fxml"));
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            RegistrationController.loggeduser = null;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
