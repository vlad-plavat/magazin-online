package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.nl.services.OrderService;
import org.nl.services.StageService;

import java.util.Date;

public class DeliveryItem {
    private Date orderDate;
    private String userOrd;
    private DeliveryController dc;

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setUserOrd(String userOrd) {
        this.userOrd = userOrd;
    }

    public void setDc(DeliveryController dc) {
        this.dc = dc;
    }

    public void confirmDelivery(){
        OrderService.deliverOrder(orderDate,userOrd);
        dc.reloadOrders();
    }
    @FXML
    public void deliverOrder(ActionEvent evt) {
        try {
            StageService.createYesNoPopup(evt,"Deliver order?","Deliver selected order?",
                    this,getClass().getMethod("confirmDelivery"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
