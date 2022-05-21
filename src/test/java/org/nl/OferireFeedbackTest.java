package org.nl;



import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nl.controllers.RegistrationController;
import org.nl.model.User;
import org.nl.services.FeedbackService;
import org.nl.services.FileSystemService;
import org.nl.services.StageService;
import org.nl.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class OferireFeedbackTest {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";
    public static final String[] ROLES = new String[]{"Client", "Worker", "Courier", "Manager"};

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.setAppFolder(".test-registration-example");
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        Main.initDatabases();
        UserService.addUser(USERNAME, PASSWORD, ROLES[0], "Timisoara");

    }

    @AfterEach
    void tearDown(){
        Main.closeDatabases();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        RegistrationController.loggeduser = new User(USERNAME, UserService.encodePassword(USERNAME, PASSWORD), ROLES[0], "Timisoara");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("client/Feedback.fxml")));
        primaryStage.setTitle("Nature Leaf Testing");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        StageService.setMainStage(primaryStage);
        //new Main().start(primaryStage);
    }

    @Test
    void testBackButton(FxRobot robot) {
        robot.clickOn("Back");
        robot.clickOn("Feedback");
    }

    @Test
    void testSendFeedback(FxRobot robot){
        robot.clickOn("Enter your feedback here");
        robot.write("Test feedback");
        assertThat(FeedbackService.getAllFeedbacks().toList()).size().isEqualTo(0);
        robot.clickOn("Send Feedback");
        robot.clickOn("Ok");
        assertThat(FeedbackService.getAllFeedbacks().toList()).size().isEqualTo(1);
        assertThat(FeedbackService.getAllFeedbacks().toList().get(0).getText()).isEqualTo("Test feedback");
    }

    @Test
    void testSendFeedbackFailed(FxRobot robot){
        assertThat(FeedbackService.getAllFeedbacks().toList()).size().isEqualTo(0);
        robot.clickOn("Enter your feedback here");
        robot.clickOn("Send Feedback");
        assertThat(FeedbackService.getAllFeedbacks().toList()).size().isEqualTo(0);
    }



}