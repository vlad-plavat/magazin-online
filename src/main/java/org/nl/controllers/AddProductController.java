package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class AddProductController {
    @FXML
    public ImageView productImage;
    @FXML
    public TextField nameField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField stockField;
    @FXML
    public TextField dimensionsButton;
    @FXML
    public TextArea descriptionBox;

    private File imageFile;


    public void saveProduct(ActionEvent evt) {
    }

    public void openFileChooser(ActionEvent evt) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Supported Images", "*.jpg","*.png","*.gif","*.bmp")
        );
        imageFile = fileChooser.showOpenDialog(((Node)evt.getSource()).getScene().getWindow());
        if(imageFile!=null)//  file:/E:/sofa.jpg
            productImage.setImage(new Image("file:/"+imageFile.getPath()));
    }
}
