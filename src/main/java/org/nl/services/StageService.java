package org.nl.services;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.nl.Main;

import java.io.IOException;
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
}
