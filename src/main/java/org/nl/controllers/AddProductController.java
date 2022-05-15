package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.nl.Main;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.nl.services.FileSystemService.getFullPath;

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
    public TextField dimensionsField;
    @FXML
    public TextArea descriptionBox;

    private File imageFile;

    private StoreCheckController scc;

    public void setScc(StoreCheckController scc) {
        this.scc = scc;
    }

    public void saveProduct(ActionEvent evt) {
        if(nameField.getText().isBlank()){
            StageService.createTextPopup(evt,"Incorrect input","Enter the product's name.");
            return;
        }
        float price;
        try{
            price = Float.parseFloat(priceField.getText());
        }catch (NumberFormatException e){
            StageService.createTextPopup(evt,"Incorrect input","Check price entered!");
            return;
        }
        int stock;
        try{
            stock = Integer.parseInt(stockField.getText());
        }catch (NumberFormatException e){
            StageService.createTextPopup(evt,"Incorrect input","Check stock entered!");
            return;
        }
        if(dimensionsField.getText().isBlank()){
            StageService.createTextPopup(evt,"Incorrect input","Enter the product's dimensions.");
            return;
        }
        if(imageFile == null || !imageFile.exists()){
            StageService.createTextPopup(evt,"Incorrect input","Select a valid image!");
            return;
        }
        Random random = new Random();
        int productId;
        do {
            productId = random.nextInt();
        }while(productId==0 || ProductService.doesIdExist(productId));

        try {

            String finalPath = getFullPath("productImages") + "/" + productId + "." +
                    FilenameUtils.getExtension(imageFile.getName());
            Files.copy(Path.of(imageFile.getPath()), Path.of(finalPath));

            ProductService.addProduct(productId,nameField.getText(),price,dimensionsField.getText(),
                    descriptionBox.getText(),stock,productId + "." +
                            FilenameUtils.getExtension(imageFile.getName()));

            ((Stage) ((Node) evt.getSource()).getScene().getWindow()).close();
            scc.reloadProducts(evt);
        } catch (IOException | ProductIDAlreadyExistsException e) {
            e.printStackTrace();
        }
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
