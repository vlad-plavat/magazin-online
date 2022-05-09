package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.model.Product;
import org.nl.services.ProductService;

import java.io.IOException;
import java.net.URL;

import static org.nl.controllers.RegistrationController.loggeduser;

public class ItemStoreController {
    @FXML
    private TextField priceField;
    @FXML
    private TextField dimensionField;
    @FXML
    private TextField stockField;

    private int productId;

    public void setProductId(int productId){
        this.productId = productId;
        setProduct();
    }

    @FXML
    public void initialize() {


    }

    private void setProduct(){
        Product p = ProductService.getProduct(productId);
        priceField.setText(""+p.getPrice());
        dimensionField.setText(""+p.getDimensions());
        stockField.setText(""+p.getStock());
    }


}
