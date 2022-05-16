package org.nl.controllers.manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.dizitart.no2.objects.Cursor;
import org.nl.Main;
import org.nl.model.Feedback;
import org.nl.services.FeedbackService;
import org.nl.services.StageService;

import java.net.URL;
import java.text.SimpleDateFormat;

public class FeedbackViewController {
    @FXML
    public TextField searchField;
    @FXML
    private AnchorPane pane;
    @FXML
    public void initialize() {
        reloadFeedback();
    }
    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/Manager.fxml");
    }

    @FXML
    public void reloadFeedback() {
        for(int chind=pane.getChildren().size();chind>0;chind--) {
            pane.getChildren().remove(0);
        }

        Cursor<Feedback> all = FeedbackService.getAllFeedbacks();

        int i = 0;

        for(Feedback f : all) {
            if(FeedbackService.checkText(f,searchField)) {
                addFeedbackOnScreen(f, i);
                i++;
            }
        }
        pane.setPrefHeight(i*250);
    }

    private void addFeedbackOnScreen(Feedback f, int i){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("manager/feedList.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load fxml file feedList.fxml");
            FXMLLoader loader = new FXMLLoader(toFxml);
            Pane newPane = loader.load();
            pane.getChildren().add(newPane);

            ((TextArea)newPane.getChildren().get(1)).setText(f.getText());
            ((Text)newPane.getChildren().get(2)).setText("User:\n"+f.getUsername());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy\nHH:mm:ss");
            ((Text)newPane.getChildren().get(3)).setText("Date:\n" + formatter.format(f.getDate()));
            ((Button)newPane.getChildren().get(4)).setOnAction(
                    (evt)-> {
                        try {
                            StageService.createYesNoPopup(pane.getScene(),"Delete feedback?",
                                    "Are you sure you want ti delete the feedback?",
                                    null,
                                    FeedbackService.class.getMethod("deleteFeedback", Feedback.class, FeedbackViewController.class),
                                    f,this);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
            );

            newPane.setLayoutY(i*250);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}
