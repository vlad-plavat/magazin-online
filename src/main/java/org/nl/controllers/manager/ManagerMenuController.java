package org.nl.controllers.manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.nl.Main;
import org.nl.controllers.RegistrationController;
import org.nl.services.StageService;

import java.net.URL;

public class ManagerMenuController {
    @FXML
    public void goToStoreCheck(ActionEvent evt){
        StageService.loadPage(evt, "manager/StoreCheck.fxml");
    }
    @FXML
    public void goToSettings(ActionEvent evt){
        StageService.loadPage(evt,"accountSettings.fxml");
    }
    @FXML
    public void goToStatistics(ActionEvent evt){
        StageService.loadPage(evt, "manager/statistica.fxml");
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
        StageService.loadPage(evt, "manager/vizFeedback.fxml");
    }
}
