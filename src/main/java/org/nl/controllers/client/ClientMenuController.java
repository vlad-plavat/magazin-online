package org.nl.controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.nl.Main;
import org.nl.controllers.RegistrationController;
import org.nl.services.StageService;

import java.net.URL;


public class ClientMenuController {
    @FXML
    public void goToStore(ActionEvent evt){
        StageService.loadPage(evt, "client/shop.fxml");
    }
    @FXML
    public void goToSettings(ActionEvent evt){
        StageService.loadPage(evt,"accountSettings.fxml");
    }
    @FXML
    public void goToOrders(ActionEvent evt){
        StageService.loadPage(evt, "client/orderHistory.fxml");

    }
    @FXML
    public void goToFeedback(ActionEvent evt){
        StageService.loadPage(evt, "client/Feedback.fxml");

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
