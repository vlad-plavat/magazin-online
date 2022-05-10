package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.nl.services.OrderService;
import org.nl.services.StageService;

import java.util.Date;

public class OrderItem {

    public Date orderDate;
    private String userOrd;
    private WorkerController wk;

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setUserOrd(String userOrd) {
        this.userOrd = userOrd;
    }

    public void setWk(WorkerController wk) {
        this.wk = wk;
    }

    public void confirmOrder(){
        OrderService.processOrder(orderDate,userOrd);
        wk.reloadOrders();
    }

    @FXML
    public void processOrder(ActionEvent evt) {
        try {
            StageService.createYesNoPopup(evt,"Process order?","Process selected order?",
                    this,getClass().getMethod("confirmOrder"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        wk.reloadOrders();
    }
}
