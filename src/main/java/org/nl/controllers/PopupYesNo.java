package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PopupYesNo {
    @FXML
    private Text question;
    private Object target;
    @FXML
    public void confirm(ActionEvent evt){

    }
    @FXML
    public void goBack(ActionEvent evt){
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
}
