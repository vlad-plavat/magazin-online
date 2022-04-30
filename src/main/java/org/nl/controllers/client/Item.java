package org.nl.controllers.client;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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

public class Item {

    @FXML
    private Text idText;

    @FXML
    public void detailsPage(ActionEvent evt){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("productInfo.fxml");
            if (toFxml == null)
                throw new RuntimeException("Could not load productInfo.fxml");
            Pane root = FXMLLoader.load(toFxml);

            final Stage infoPage = new Stage();
            infoPage.initModality(Modality.NONE);
            infoPage.setResizable(false);
            infoPage.initOwner(((Node) evt.getSource()).getScene().getWindow());
            infoPage.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            infoPage.setScene(scene);
            setProduct(root,infoPage);
            infoPage.show();
        }catch (IOException e){
            System.out.println("IO error");
        }
    }

    private void setProduct(Pane pane, Stage infoPage){
        //numele
        Product p = ProductService.getProduct(Integer.parseInt(idText.getText()));
        ((ImageView)((HBox)pane.getChildren().get(0)).getChildren().get(0)).setImage(new Image(p.getImageAddr()));
        ((Text)pane.getChildren().get(1)).setText(p.getName());
        infoPage.setTitle(p.getName());
        ((Text)pane.getChildren().get(2)).setText(String.format("Price: $%.2f",p.getPrice()));
        ((Text)pane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());
        if(p.getStock()>0){
            ((Text)pane.getChildren().get(4)).setText("In stock");
            ((Text)pane.getChildren().get(4)).setFill(Color.GREEN);
        }else{
            ((Text)pane.getChildren().get(4)).setText("Out of stock");
            ((Text)pane.getChildren().get(4)).setFill(Color.RED);
        }
        ((Text)pane.getChildren().get(6)).setText(p.getDescription());
    }

}
