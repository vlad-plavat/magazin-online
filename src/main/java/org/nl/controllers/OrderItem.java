package org.nl.controllers;

import javafx.event.ActionEvent;
import org.nl.services.StageService;

public class OrderItem {
    public void processOrder(ActionEvent evt) {
        StageService.createYesNoPopup(evt,"Process order?","Process selected order?");
    }
}
