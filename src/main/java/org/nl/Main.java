package org.nl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.nl.services.FileSystemService;
import org.nl.services.ProductService;
import org.nl.services.UserService;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        initDirectory();
        UserService.initDatabase();
        ProductService.initDatabase();
        URL toFxml = getClass().getClassLoader().getResource("register.fxml");
        if(toFxml != null) {
            Parent root = FXMLLoader.load(toFxml);
            primaryStage.setTitle("NatureLeaf");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.getIcons().add(new Image("icon.png"));
            primaryStage.setResizable(false);
            primaryStage.show();
        }else throw new RuntimeException("FXML file not found!");
    }

    private void initDirectory() {
        Path applicationHomePath = FileSystemService.APPLICATION_HOME_PATH;
        if (!Files.exists(applicationHomePath))
            if(!applicationHomePath.toFile().mkdirs())
                throw new RuntimeException("Could not initialize directories");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
