package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.services.StageService;
import org.nl.services.UserService;

import java.io.IOException;
import java.net.URL;


public class ClientMenuController {
    @FXML
    public void goToStore(ActionEvent evt){
        StageService.loadPage(evt,"shop.fxml");
    }
    @FXML
    public void goToSettings(ActionEvent evt){
        StageService.loadPage(evt,"AccountSettings.fxml");
    }
    @FXML
    public void goToOrders(ActionEvent evt){
        System.out.println("Orders");

    }
    @FXML
    public void goToFeedback(ActionEvent evt){
        StageService.loadPage(evt,"Feedback.fxml");

    }
    @FXML
    public void logOut(ActionEvent evt){
        URL root = Main.class.getClassLoader().getResource("register.fxml");
        if(root != null) {
            StageService.loadPage(evt, "register.fxml");
            RegistrationController.loggeduser = null;
        }
    }
}
