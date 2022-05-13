package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.dizitart.no2.objects.Cursor;
import org.nl.Main;
import org.nl.model.Order;
import org.nl.model.Product;
import org.nl.services.OrderService;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.net.URL;
import java.text.SimpleDateFormat;

public class WorkerController {

    @FXML
    private AnchorPane pane;
    @FXML
    public TextField searchField;

    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/Worker.fxml");
    }

    @FXML
    public void initialize() {
        reloadOrders();
    }

    @FXML
    public void reloadOrders() {
        for(int chind=pane.getChildren().size();chind>0;chind--) {
            pane.getChildren().remove(0);
        }

        Cursor<Order> all = OrderService.getAllPlacedOrders();

        int i = 0;
        for(Order o : all){
            Product p = ProductService.getProduct(o.getIdProduct());
            if(OrderService.checkOrderName(p.getName(),searchField.getText())) {
                addOrderOnScreen(o, i);
                i++;
            }
        }
        pane.setPrefHeight(i*125);
    }


    private void addOrderOnScreen(Order o, int i){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("order.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load fxml file item.fxml");
            FXMLLoader loader = new FXMLLoader(toFxml);
            Pane newPane = loader.load();
            ((OrderItem)loader.getController()).setOrderDate(o.getDate());
            ((OrderItem)loader.getController()).setUserOrd(o.getUsername());
            ((OrderItem)loader.getController()).setWk(this);

            pane.getChildren().add(newPane);
            Product p = ProductService.getProduct(o.getIdProduct());

            ((ImageView)newPane.getChildren().get(0)).setImage(new Image(p.getImageAddr()));
            ((Text)newPane.getChildren().get(1)).setText(p.getName());
            ((Text)newPane.getChildren().get(2)).setText(String.format("Price: $%.2f",p.getPrice()));
            ((Text)newPane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            ((Text)newPane.getChildren().get(5)).setText("Order date: " + formatter.format(o.getDate()));



            newPane.setLayoutY(i*125);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
