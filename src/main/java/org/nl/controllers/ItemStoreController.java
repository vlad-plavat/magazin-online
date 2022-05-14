package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.nl.model.Product;
import org.nl.services.ProductService;

public class ItemStoreController {
    @FXML
    private TextField priceField;
    @FXML
    private TextField dimensionField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField descriptionField;

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
        descriptionField.setText(""+p.getDescription());
    }

    @FXML
    public void saveChanges(ActionEvent evt){
        Product p = ProductService.getProduct(productId);
        String price = priceField.getText();
        String description = descriptionField.getText();
        String dimensions = dimensionField.getText();
        String stock = stockField.getText();

        if(!(price.isBlank() || description.isBlank() || dimensions.isBlank() || stock.isBlank())){
            ProductService.changeProductData(productId, p.getName(), Float.parseFloat(price),  dimensions, description, Integer.parseInt(stock), p.getImageAddr());
        }
        ((Node)evt.getSource()).setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: grey;");
    }

    @FXML
    public void turnColor(MouseEvent evt){
        ((Node)evt.getSource()).setStyle("-fx-background-color: wheat; -fx-border-width: 1px; -fx-border-color: grey;");
    }



}
