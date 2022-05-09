package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.nl.Main;
import org.nl.exceptions.SimpleTextException;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.exceptions.WrongPasswordException;
import org.nl.exceptions.WrongUsernameException;
import org.nl.services.StageService;
import org.nl.services.UserService;


import java.io.IOException;
import java.net.URL;

import static org.nl.controllers.RegistrationController.loggeduser;
import static org.nl.services.UserService.encodePassword;

public class AccountSettingsController {
    @FXML
    private TextField auxField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField oldPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private Text errorField;

    private static Stage crStage;
    public static Stage getCrStage() {
        return crStage;
    }



    @FXML
    public void initialize() {
        auxField.setVisible(false);
        usernameField.setText(loggeduser.getUsername());
        if(loggeduser.getRole().equals("Client")){
            auxField.setVisible(true);
            auxField.setPromptText("Address");
            auxField.setText(loggeduser.getAux());
        }else if(loggeduser.getRole().equals("Courier")){
            auxField.setVisible(true);
            auxField.setPromptText("Car license plate");
            auxField.setText(loggeduser.getAux());
        }

    }

    @FXML
    public void backToMenu(ActionEvent evt){
        StageService.loadPage(evt,"Menus/"+loggeduser.getRole()+".fxml");

    }
    @FXML
    public void saveChanges(ActionEvent evt){
        UserService.readusers();
        String name = usernameField.getText();
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String aux = auxField.getText();
        errorField.setText("");

        try {
            if(name.isBlank() || oldPass.isBlank())
                throw new SimpleTextException("Please enter a username and password.");
            if(!loggeduser.getPassword().equals(encodePassword(loggeduser.getUsername(), oldPass))){
                throw new WrongPasswordException(loggeduser.getUsername());
            }
            if(!newPass.isBlank() && newPass.length()<4)
                throw new SimpleTextException("The password must be at least 4 characters long.");
            if(loggeduser.getRole().equals("Client") && (aux.length()<5 || aux.isBlank()))
                throw new SimpleTextException("Please enter an address.");
            if(loggeduser.getRole().equals("Courier") && (aux.length()<5 || aux.isBlank()))
                throw new SimpleTextException("Please enter the license plate.");
            if(newPass.isBlank())
                newPass = oldPass;
            UserService.changeUserData(name, newPass, aux);

            backToMenu(evt);
            //System.out.println(">>>>>trec la meniu");
        } catch (WrongPasswordException | SimpleTextException | UsernameAlreadyExistsException e) {
            errorField.setText(e.getMessage());
        }

    }

    @FXML
    public void deleteAccount(ActionEvent evt) {
        UserService.readusers();
        String name = usernameField.getText();
        String oldPass = oldPasswordField.getText();
        String newPass = newPasswordField.getText();
        String aux = auxField.getText();
        errorField.setText("");

        try {
            if(name.isBlank() || oldPass.isBlank())
                throw new SimpleTextException("Please enter a username and password.");
            if(!loggeduser.getPassword().equals(encodePassword(loggeduser.getUsername(), oldPass))){
                throw new WrongPasswordException(loggeduser.getUsername());
            }

            URL toFxml = Main.class.getClassLoader().getResource("Popup.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load Popup.fxml");
            Parent root = FXMLLoader.load(toFxml);

            crStage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Confirmation");
            dialog.setResizable(false);
            dialog.initOwner(((Node) evt.getSource()).getScene().getWindow());
            dialog.getIcons().add(new Image("icon.png"));
            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.show();

        } catch (WrongPasswordException | SimpleTextException e) {
            errorField.setText(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
