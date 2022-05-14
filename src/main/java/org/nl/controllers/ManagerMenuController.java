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
import java.net.URL;

public class ManagerMenuController {
    @FXML
    public void goToStoreCheck(ActionEvent evt){
        StageService.loadPage(evt,"StoreCheck.fxml");
    }
    @FXML
    public void goToSettings(ActionEvent evt){
        StageService.loadPage(evt,"AccountSettings.fxml");
    }
    @FXML
    public void goToStatistics(ActionEvent evt){
        System.out.println("Statistics");
    }

    @FXML
    public void logOut(ActionEvent evt){
        URL root = Main.class.getClassLoader().getResource("register.fxml");
        if(root != null) {
            StageService.loadPage(evt, "register.fxml");
            RegistrationController.loggeduser = null;
        }

    }

    public void goToFeedbacks(ActionEvent evt) {
        StageService.loadPage(evt,"vizFeedback.fxml");
    }
}
