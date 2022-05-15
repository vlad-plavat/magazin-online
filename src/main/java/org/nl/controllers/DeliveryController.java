package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.nl.services.StageService;

import static org.nl.controllers.RegistrationController.loggeduser;

public class DeliveryController {
    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/"+loggeduser.getRole()+".fxml");
    }
}
