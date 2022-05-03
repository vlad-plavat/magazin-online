package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.services.FeedbackService;
import org.nl.services.UserService;


import java.io.IOException;
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
        backToMenu(evt);
    }

}
