package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.nl.services.UserService;

public class PopupController {
    @FXML
    public void confirmDelete(ActionEvent evt){
        UserService.deleteUser(evt);
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
    @FXML
    public void goBack(ActionEvent evt){
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
}
