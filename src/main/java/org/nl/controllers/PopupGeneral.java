package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class PopupGeneral {

    private Method onAct;
    private Object target;
    //private int arg;
    private Object[] args;

    public void setOnAct(Method onAct) {
        this.onAct = onAct;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


    @FXML
    public void confirm(ActionEvent evt){
        try {
            onAct.invoke(target,args);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            goBack(evt);
        }
    }
    @FXML
    public void goBack(ActionEvent evt){
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
}
