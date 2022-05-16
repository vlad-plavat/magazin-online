package org.nl.controllers.courier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.dizitart.no2.objects.Cursor;
import org.nl.Main;
import org.nl.model.Order;
import org.nl.model.Product;
import org.nl.services.OrderService;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.net.URL;

import static org.nl.controllers.RegistrationController.loggeduser;
import static org.nl.services.FileSystemService.imgFromPrd;

public class DeliveryController {
    @FXML
    private AnchorPane pane;
    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/"+loggeduser.getRole()+".fxml");
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

        Cursor<Order> all = OrderService.getAllProcessedOrders();

        int i = 0;
        for(Order o : all){
            addOrderOnScreen(o, i);
            i++;
        }
        pane.setPrefHeight(i*125);
    }


    private void addOrderOnScreen(Order o, int i){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("courier/deliveryList.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load fxml file deliveryList.fxml");
            FXMLLoader loader = new FXMLLoader(toFxml);
            Pane newPane = loader.load();
            ((DeliveryItem)loader.getController()).setOrderDate(o.getDate());
            ((DeliveryItem)loader.getController()).setUserOrd(o.getUsername());
            ((DeliveryItem)loader.getController()).setDc(this);

            pane.getChildren().add(newPane);
            Product p = ProductService.getProduct(o.getIdProduct());

            ((Text)newPane.getChildren().get(1)).setText(p.getName());
            ((Text)newPane.getChildren().get(2)).setText(String.format("Price: $%.2f",p.getPrice()));
            ((Text)newPane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());
            ((Text)newPane.getChildren().get(5)).setText("Address: " + o.getAddress());
            ((Text)newPane.getChildren().get(6)).setText("Name: " + o.getUsername());

            newPane.setLayoutY(i*125);
            ((ImageView)newPane.getChildren().get(0)).setImage(new Image(imgFromPrd(p)));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
