package org.nl;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nl.controllers.RegistrationController;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.model.Feedback;
import org.nl.model.User;
import org.nl.services.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.nl.services.FileSystemService.getFullPath;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class FeedbackViewTest {

    public static final String USERNAME = "user";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.setAppFolder("database-test");
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        Main.initDatabases();
    }

    @AfterEach
    void tearDown() {
        Main.closeDatabases();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        setUp();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("manager/vizFeedback.fxml")));
        primaryStage.setTitle("Nature Leaf Testing");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        //new Main().start(primaryStage);
        tearDown();
    }

    private void copyImage(int productId) {
        String finalPath = getFullPath("productImages") + "/" + productId + ".png";
        try {
            Files.copy(Path.of(Objects.requireNonNull(Main.class.getClassLoader().getResource("icon.png")).
                    getPath().substring(1)), Path.of(finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFeedbackView(FxRobot robot) {
        robot.clickOn("Back to menu");
        robot.clickOn("View Feedbacks");
        Pane p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(0);

        FeedbackService.addFeedback(USERNAME,"Feedback text 1",new Date());
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        FeedbackService.addFeedback(USERNAME,"Feedback text 2",new Date());
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        FeedbackService.addFeedback(USERNAME+"2","Feedback text 1",new Date());

        List<Feedback> flist = FeedbackService.getAllFeedbacks().toList();
        assertThat(flist.get(0).getText()).isEqualTo("Feedback text 1");
        assertThat(flist.get(1).getText()).isEqualTo("Feedback text 2");
        assertThat(flist.get(2).getText()).isEqualTo("Feedback text 1");

        assertThat(flist.get(0).getUsername()).isEqualTo(USERNAME+"2");
        assertThat(flist.get(1).getUsername()).isEqualTo(USERNAME);
        assertThat(flist.get(2).getUsername()).isEqualTo(USERNAME);

        robot.doubleClickOn("#searchField");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        assertThat(p.getChildren().size()).isEqualTo(3);

    }

    @Test
    void testFeedbackDelete(FxRobot robot) {
        robot.clickOn("Back to menu");
        robot.clickOn("View Feedbacks");
        Pane p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(0);

        FeedbackService.addFeedback(USERNAME,"Feedback text 1",new Date());
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        FeedbackService.addFeedback(USERNAME,"Feedback text 2",new Date());
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        robot.push(KeyCode.SHIFT);
        FeedbackService.addFeedback(USERNAME+"2","Feedback text 1",new Date());

        robot.doubleClickOn("#searchField");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        assertThat(p.getChildren().size()).isEqualTo(3);

        Node b = ((Pane)robot.lookup("Feedback text 2").query().getParent()).getChildren().get(4);
        robot.clickOn(b);
        robot.clickOn("Are you sure you want to delete the feedback?");
        robot.clickOn("No");
        assertThat(p.getChildren().size()).isEqualTo(3);
        robot.push(KeyCode.SHIFT);
        robot.clickOn(b);
        robot.clickOn("Yes");
        robot.push(KeyCode.SHIFT);

        assertThat(p.getChildren().size()).isEqualTo(2);


        assertThat(robot.lookup("Feedback text 2").queryAll().size()).isEqualTo(0);

    }

}
