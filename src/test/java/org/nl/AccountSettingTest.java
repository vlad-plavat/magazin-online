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
import org.nl.services.FileSystemService;
import org.nl.services.StageService;
import org.nl.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class AccountSettingTest {

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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("accountSettings.fxml")));
        primaryStage.setTitle("Nature Leaf Testing");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        StageService.setMainStage(primaryStage);
        //new Main().start(primaryStage);
    }

    @Test
    void testBackButton(FxRobot robot) {
        robot.clickOn("Back");
        robot.clickOn("Account Settings");
    }

    @Test
    void testChangeFailed(FxRobot robot) {
        robot.doubleClickOn("#usernameField");
        robot.write(USERNAME + "123");
        robot.clickOn("Save Changes");
        assertThat(robot.lookup("#errorField").queryText()).hasText("Please enter a username and password.");
        robot.doubleClickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#oldPasswordField");
        robot.write(PASSWORD + "123");
        robot.clickOn("Save Changes");
        assertThat(robot.lookup("#errorField").queryText()).hasText("The password for the user user is incorrect!");
        robot.doubleClickOn("#oldPasswordField");
        robot.write(PASSWORD);
        robot.doubleClickOn("#auxField");
        robot.write(" ");
        robot.clickOn("Save Changes");
        assertThat(robot.lookup("#errorField").queryText()).hasText("Please enter an address.");
    }

    @Test
    void testChangeSettings(FxRobot robot) {
        robot.doubleClickOn("#usernameField");
        robot.write(USERNAME + "123");
        robot.clickOn("#oldPasswordField");
        robot.write(PASSWORD);
        robot.clickOn("Save Changes");
        robot.clickOn("Account Settings");
        robot.clickOn("#oldPasswordField");
        robot.write(PASSWORD);
        robot.clickOn("#newPasswordField");
        robot.write(PASSWORD + "123");
        robot.clickOn("Save Changes");
        robot.clickOn("Account Settings");
        robot.clickOn("#oldPasswordField");
        robot.write(PASSWORD + "123");
        robot.doubleClickOn("#auxField");
        robot.write("Hateg, 335500");
        robot.clickOn("Save Changes");
        robot.clickOn("Account Settings");
    }

    @Test
    void testDeleteAccount(FxRobot robot) {
        robot.clickOn("#oldPasswordField");
        robot.write(PASSWORD);
        robot.clickOn("Delete account");
        robot.clickOn("No");
        robot.clickOn("Delete account");
        robot.clickOn("Yes");
    }

    @Test
    void testDeleteAccountFailed(FxRobot robot) {
        robot.clickOn("Delete account");
        assertThat(robot.lookup("#errorField").queryText()).hasText("Please enter a username and password.");
        robot.clickOn("#oldPasswordField");
        robot.write(PASSWORD + "123");
        robot.clickOn("Delete account");
        assertThat(robot.lookup("#errorField").queryText()).hasText("The password for the user user is incorrect!");

    }

}