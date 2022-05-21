package org.nl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.nl.services.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    public static void closeDatabases() {
        UserService.closeDatabase();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initDatabases();
        URL toFxml = getClass().getClassLoader().getResource("register.fxml");
        if(toFxml != null) {
            Parent root = FXMLLoader.load(toFxml);
            primaryStage.setTitle("NatureLeaf");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.getIcons().add(new Image("icon.png"));
            primaryStage.setResizable(false);
            StageService.setMainStage(primaryStage);
            primaryStage.show();
        }else throw new RuntimeException("FXML file not found!");
    }

    public static void initDatabases(){
        FileSystemService.initDirectory();
        UserService.initDatabase();
        FeedbackService.initDatabase();
        ProductService.initDatabase();
        OrderService.initDatabase();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
