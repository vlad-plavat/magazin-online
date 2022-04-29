package org.nl.controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.dizitart.no2.objects.Cursor;
import org.nl.Main;
import org.nl.model.Product;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

public class Shop {

    //private ArrayList<Pane> produseAfisate = new ArrayList<>();

    @FXML
    private AnchorPane pane;
    @FXML
    public void initialize() {
        //i.setImage(new Image("file:/E:/ceva.png"));
        //i.imageProperty().set(new Image("file:/E:/ceva.png"));
        loadAllProducts();
    }

    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/Client.fxml");
    }

    private void loadAllProducts(){
        for(int chind=pane.getChildren().size();chind>0;chind--) {
            pane.getChildren().remove(0);
        }
        Cursor<Product> all = ProductService.getAllProducts();
        int i = 0;
        for(Product p : all){
            addProductOnScreen(p,i);
            i++;
        }
        pane.setPrefHeight(i*125);
    }

    private void addProductOnScreen(Product p, int i){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("item.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load fxml file item.fxml");
            Pane newPane = FXMLLoader.load(toFxml);
            pane.getChildren().add(newPane);
            ((Text)newPane.getChildren().get(1)).setText(p.getName());
            ((ImageView)newPane.getChildren().get(0)).setImage(new Image(p.getImageAddr()));
            ((Text)newPane.getChildren().get(2)).setText(String.format("Price: %.2f",p.getPrice()));
            ((Text)newPane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());
            if(p.getStock()>0){
                ((Text)newPane.getChildren().get(4)).setText("In stock");
                ((Text)newPane.getChildren().get(4)).setFill(Color.GREEN);
            }else{
                ((Text)newPane.getChildren().get(4)).setText("Out of stock");
                ((Text)newPane.getChildren().get(4)).setFill(Color.RED);
            }
            newPane.setLayoutY(i*125);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}
