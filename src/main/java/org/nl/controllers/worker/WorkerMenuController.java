package org.nl.controllers.worker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.nl.Main;
import org.nl.controllers.RegistrationController;
import org.nl.services.StageService;

import java.net.URL;

public class WorkerMenuController {
    @FXML
    public void goToOrderProcessing(ActionEvent evt){
        StageService.loadPage(evt, "worker/Worker.fxml");
    }
    @FXML
    public void goToSettings(ActionEvent evt){
        StageService.loadPage(evt,"AccountSettings.fxml");
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
