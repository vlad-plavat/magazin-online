package org.nl.controllers.client;


import javafx.fxml.FXML;

import javafx.event.ActionEvent;
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
import org.nl.controllers.AccountSettingsController;
import org.nl.controllers.RegistrationController;
import org.nl.exceptions.OutOfStockException;
import org.nl.model.Product;
import org.nl.services.ProductService;
import org.nl.services.StageService;
import org.nl.services.UserService;

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
        ((ImageView)((HBox)pane.getChildren().get(0)).getChildren().get(0)).setImage(new Image(p.getImageAddr()));
    }

    @FXML
    public void order(ActionEvent evt){
        try {
            Product p = ProductService.getProduct(Integer.parseInt(idText.getText()));
            if(p.getStock() == 0){
                throw new OutOfStockException();
            }

            //send here

            URL toFxml = Main.class.getClassLoader().getResource("OrderMenu.fxml");
            if (toFxml == null)
                throw new RuntimeException("Could not load OrderMenu.fxml");
            FXMLLoader newFXML = new FXMLLoader(toFxml);
            Pane root = newFXML.load();
            ((OrderMenu)newFXML.getController()).setPID(p.getIdProdct());
            ((OrderMenu)newFXML.getController()).setMainStage((Stage) ((Node) evt.getSource()).getScene().getWindow());

            final Stage popup = new Stage();
            popup.initModality(Modality.WINDOW_MODAL);
            popup.setResizable(false);
            popup.initOwner(((Node) evt.getSource()).getScene().getWindow());
            popup.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            popup.setScene(scene);
            popup.setTitle("Confirm order?");
            setOrderProduct(root);
            popup.show();
        }catch(OutOfStockException e){
            StageService.createTextPopup(evt,"Product out of stock","The selected product is out of stock.");
        }
        catch (IOException e){
            System.out.println("IO error");
        }
    }

    private void setOrderProduct(Pane pane){
        //numele
        Product p = ProductService.getProduct(Integer.parseInt(idText.getText()));
        ((Text)pane.getChildren().get(1)).setText("Confirm ordering " + p.getName()+"?");
        ((Text)pane.getChildren().get(2)).setText(String.format("Price: $%.2f",p.getPrice()));
        ((Text)pane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());

        ((TextField)pane.getChildren().get(4)).setText(RegistrationController.loggeduser.getAux());
        ((Text)pane.getChildren().get(7)).setText(RegistrationController.loggeduser.getUsername());
        ((ImageView)((HBox)pane.getChildren().get(0)).getChildren().get(0)).setImage(new Image(p.getImageAddr()));
    }

}
