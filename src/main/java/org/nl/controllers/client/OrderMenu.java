package org.nl.controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class OrderMenu {
    @FXML
    public void goBack(ActionEvent evt){
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
    @FXML
    public void confirmOrder(ActionEvent evt){

    }
}
