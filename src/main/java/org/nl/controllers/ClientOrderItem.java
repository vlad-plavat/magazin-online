package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.model.Order;
import org.nl.model.Product;
import org.nl.services.OrderService;
import org.nl.services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import static org.nl.controllers.RegistrationController.loggeduser;
import static org.nl.services.FileSystemService.imgFromPrd;

public class ClientOrderItem {

    private Date orderDate;

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @FXML
    public void details(ActionEvent evt){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("orderDetails.fxml");
            if (toFxml == null)
                throw new RuntimeException("Could not load orderDetails.fxml");
            Pane root = FXMLLoader.load(toFxml);

            final Stage infoPage = new Stage();
            infoPage.initModality(Modality.NONE);
            infoPage.setResizable(false);
            infoPage.initOwner(((Node) evt.getSource()).getScene().getWindow());
            infoPage.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            infoPage.setScene(scene);
            setOrder(root,infoPage);
            infoPage.show();
        }catch (IOException e){
            System.out.println("IO error");
        }
    }

    private void setOrder(Pane pane, Stage infoPage){
        //numele
        Order o = OrderService.getOrder(orderDate,loggeduser.getUsername());
        Product p = ProductService.getProduct(o.getIdProduct());
        ((ImageView)((HBox)pane.getChildren().get(0)).getChildren().get(0)).setImage(new Image(imgFromPrd(p)));
        ((Text)pane.getChildren().get(1)).setText(p.getName());
        infoPage.setTitle(p.getName());
        ((Text)pane.getChildren().get(2)).setText(String.format("Price: $%.2f",p.getPrice()));
        ((Text)pane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());
        ((Text)pane.getChildren().get(4)).setText("Status: " + o.getStatus());
        ((Text)pane.getChildren().get(6)).setText(p.getDescription());
        ((Text)pane.getChildren().get(7)).setText("Address: " + o.getAddress());
    }
}
