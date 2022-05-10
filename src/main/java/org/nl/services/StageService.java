package org.nl.services;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.controllers.PopupYesNo;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

public class StageService {
    public static void loadPage(ActionEvent evt, String s){
        try {
            URL toFxml = Main.class.getClassLoader().getResource(s);
            if(toFxml == null)
                throw new RuntimeException("Could not load FXML file "+s);
            Parent root = FXMLLoader.load(toFxml);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadPage(Stage stg, String s){
        try {
            URL toFxml = Main.class.getClassLoader().getResource(s);
            if(toFxml == null)
                throw new RuntimeException("Could not load FXML file "+s);
            Parent root = FXMLLoader.load(toFxml);
            Scene scene = new Scene(root);
            stg.setScene(scene);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTextPopup(ActionEvent evt, String title,String text){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("PopupFeedback.fxml");
            if (toFxml == null)
                throw new RuntimeException("Could not load PopupFeedback.fxml");
            Pane root = FXMLLoader.load(toFxml);

            ((Text)((HBox)root.getChildren().get(0)).getChildren().get(0)).setText(text);
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(title);
            dialog.setResizable(false);
            dialog.initOwner(((Node) evt.getSource()).getScene().getWindow());
            dialog.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void createYesNoPopup(ActionEvent evt, String title,String text,
                                        Object target, Method onAct){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("PopupYesNo.fxml");
            if (toFxml == null)
                throw new RuntimeException("Could not load PopupYesNo.fxml");
            FXMLLoader loader = new FXMLLoader(toFxml);
            Pane root = loader.load();
            ((PopupYesNo)loader.getController()).setTarget(target);
            ((PopupYesNo)loader.getController()).setOnAct(onAct);
            //((PopupYesNo)loader.getController()).setArg(arg);

            ((Text)((HBox)root.getChildren().get(0)).getChildren().get(0)).setText(text);
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(title);
            dialog.setResizable(false);
            dialog.initOwner(((Node) evt.getSource()).getScene().getWindow());
            dialog.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
