package org.nl.controllers.client;

import com.sun.prism.ReadbackGraphics;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nl.controllers.RegistrationController;
import org.nl.services.OrderService;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.util.Date;

public class OrderMenu {

    @FXML
    private TextField addressField;

    private int productID;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private Stage mainStage;

    public void setPID(int id){
        productID = id;
    }

    @FXML
    public void goBack(ActionEvent evt){
        ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
    }
    @FXML
    public void confirmOrder(ActionEvent evt){
        ProductService.orderProduct(productID);
        OrderService.addOrder(RegistrationController.loggeduser.getUsername(),
                productID,new Date(),addressField.getText());
        StageService.loadPage(mainStage,"shop.fxml");
        goBack(evt);
    }
}
