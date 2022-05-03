package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.nl.Main;

import java.io.IOException;

import static org.nl.controllers.RegistrationController.loggeduser;

public class FeedbackController {
    @FXML
    public void backToMenu(ActionEvent evt){
        Parent root;
        try {

            root = FXMLLoader.load(Main.class.getClassLoader().getResource("Menus/"+loggeduser.getRole()+".fxml"));
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void sendFeedback(ActionEvent evt){
        System.out.println("Send Feedback");

    }

}
