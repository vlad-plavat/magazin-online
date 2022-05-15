package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import org.nl.Main;
import org.nl.model.Product;
import org.nl.services.ProductService;

import javax.validation.constraints.Null;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ItemStoreController {
    @FXML
    private TextField priceField;
    @FXML
    private TextField dimensionField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField nameField;
    @FXML
    private ImageView productImage;

    @FXML
    private Button withdrawButton;

    public Button getWithdrawButton() {
        return withdrawButton;
    }

    private int productId;

    public void setProductId(int productId){
        this.productId = productId;
        setProduct();
    }

    @FXML
    public void initialize() {
        productImage.setOnMouseClicked(this::changePicture);

    }

    @FXML
    private void changePicture(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Change Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Images", "*.jpg","*.png","*.gif","*.bmp")
        );
        File newImage = fileChooser.showOpenDialog(((Node)mouseEvent.getSource()).getScene().getWindow());
        if(newImage!=null) {    //daca am selectat o alta imagine o salvez
            productImage.setImage(new Image("file:/" + newImage.getPath()));
            URL resPath = Main.class.getClassLoader().getResource("");
            if(resPath!=null) {
                //extrag pathul pentru folderul de resurse, poza veche si poza noua
                String finalOldPath = resPath.getPath().substring(1) + "/" +
                        ProductService.getProduct(productId).getImageAddr();
                String finalNewPath = resPath.getPath().substring(1) + "productPictures/" + productId + "." +
                        FilenameUtils.getExtension(newImage.getName());
                try {
                    Files.deleteIfExists(Path.of(finalOldPath));//sterg poza veche
                    Files.copy(Path.of(newImage.getPath()), Path.of(finalNewPath));//adaug poza noua
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setProduct(){
        Product p = ProductService.getProduct(productId);
        priceField.setText(""+p.getPrice());
        dimensionField.setText(""+p.getDimensions());
        stockField.setText(""+p.getStock());
        descriptionField.setText(""+p.getDescription());
        nameField.setText(""+p.getName());
    }

    @FXML
    public void saveChanges(ActionEvent evt){
        Product p = ProductService.getProduct(productId);
        String price = priceField.getText();
        String description = descriptionField.getText();
        String dimensions = dimensionField.getText();
        String stock = stockField.getText();
        String name = nameField.getText();

        if(!(price.isBlank() || description.isBlank() || dimensions.isBlank() || stock.isBlank())){
            try {
                if (priceField.getTooltip()!= null) {
                    priceField.getTooltip().hide();
                }
                priceField.setTooltip(null);
                ProductService.changeProductData(productId, name, Float.parseFloat(price), dimensions, description, Integer.parseInt(stock), p.getImageAddr());
                ((Node)evt.getSource()).setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: grey;");
            }catch (NumberFormatException e){

                priceField.setTooltip(new Tooltip("Check values entered!"));
                priceField.getTooltip().setAutoHide(true);
                priceField.getTooltip().show(((Node) evt.getSource()).getScene().getWindow());
            }
        }

    }

    @FXML
    public void turnColor(MouseEvent evt){
        ((Node)evt.getSource()).setStyle("-fx-background-color: wheat; -fx-border-width: 1px; -fx-border-color: grey;");
    }



}
