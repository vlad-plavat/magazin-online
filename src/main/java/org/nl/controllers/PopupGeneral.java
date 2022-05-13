package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PopupGeneral {

    private Method onAct;
    private Object target;
    //private int arg;

    public void setOnAct(Method onAct) {
        this.onAct = onAct;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    /*public void setArg(int arg) {
        this.arg = arg;
    }*/

    @FXML
    public void confirm(ActionEvent evt){
        try {
            onAct.invoke(target);
            goBack(evt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void goBack(ActionEvent evt){
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
}
