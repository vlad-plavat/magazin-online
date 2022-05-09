package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.services.FeedbackService;
import org.nl.services.UserService;


import java.io.IOException;
import java.net.URL;
import java.util.Date;

import static org.nl.controllers.RegistrationController.loggeduser;

public class FeedbackController {
    @FXML
    private TextField textField;

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
        String text = textField.getText();

        FeedbackService.addFeedback(loggeduser.getUsername(), text, new Date());
        //FeedbackService.printAllFeedback();
        try {
            URL toFxml = Main.class.getClassLoader().getResource("PopupFeedback.fxml");
            if (toFxml == null)
                throw new RuntimeException("Could not load PopupFeedback.fxml");
            Parent root = FXMLLoader.load(toFxml);


            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Feedback sent");
            dialog.setResizable(false);
            dialog.initOwner(((Node) evt.getSource()).getScene().getWindow());
            dialog.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.show();
        }catch (IOException e){
            e.printStackTrace();
        }

        backToMenu(evt);
    }

}
