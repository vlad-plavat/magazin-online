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
import org.nl.services.StageService;
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
        StageService.loadPage(evt,"Menus/"+loggeduser.getRole()+".fxml");
    }

    @FXML
    public void sendFeedback(ActionEvent evt){
        String text = textField.getText();
        if(text.isBlank() || text.isEmpty())
            return;

        FeedbackService.addFeedback(loggeduser.getUsername(), text, new Date());
        //FeedbackService.printAllFeedback();
        StageService.createTextPopup(evt,"Feedback sent","Your feedback has been sent successfully.");

        backToMenu(evt);
    }

}
