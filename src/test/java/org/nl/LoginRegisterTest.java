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
import org.nl.model.User;
import org.nl.services.FileSystemService;
import org.nl.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class LoginRegisterTest {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";
    public static final String[] ROLES = new String[]{"Client", "Worker", "Courier", "Manager"};

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.setAppFolder(".test-registration-example");
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        Main.initDatabases();

    }

    @AfterEach
    void tearDown(){
        Main.closeDatabases();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("register.fxml")));
        primaryStage.setTitle("Registration Example");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        //new Main().start(primaryStage);
    }

    @Test
    void testLoginFailedWrongUsername(FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("The user "+USERNAME+" does not exist!");
    }
    @Test
    void testLoginFailedNoUsernameOrPassword(FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.clickOn("#passwordField");
        robot.clickOn("#loginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please enter a username and password.");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please enter a username and password.");
        robot.doubleClickOn("#passwordField");
        robot.press(KeyCode.BACK_SPACE);
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#loginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please enter a username and password.");
    }

    @Test
    void testLoginFailedWrongPassword(FxRobot robot) {
        robot.clickOn("#toRegButton");
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.clickOn(ROLES[1]);
        robot.clickOn("#regButton");
        robot.clickOn("Log Out");
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD+"nopass");
        robot.clickOn("#loginButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("The password for the user "+ USERNAME +" is incorrect!");
    }

    @Test
    void testLoginSuccess(FxRobot robot) {
        robot.clickOn("#toRegButton");
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.clickOn(ROLES[1]);
        robot.clickOn("#regButton");
        robot.clickOn("Log Out");
    }

    @Test
    void testRegisterSuccess(FxRobot robot) {
        for(int i=0; i<4; i++) {
            robot.clickOn("#toRegButton");
            robot.clickOn("#usernameField");
            robot.write(USERNAME+i);
            robot.clickOn("#passwordField");
            robot.write(PASSWORD);
            robot.clickOn("#role");
            robot.clickOn(ROLES[i]);
            if(i==0||i==2) {
                robot.clickOn("#auxField");
                robot.write("aux info");
            }
            robot.clickOn("#regButton");
            robot.clickOn("Log Out");
        }
    }

    @Test
    void testRegisterFailUserAlreadyExists(FxRobot robot) {
        for(int i=0; i<2; i++) {
            robot.clickOn("#toRegButton");
            robot.clickOn("#usernameField");
            robot.write(USERNAME);
            robot.clickOn("#passwordField");
            robot.write(PASSWORD+i);
            robot.clickOn("#role");
            robot.clickOn(ROLES[i]);
            if(i==0) {
                robot.clickOn("#auxField");
                robot.write("aux info");
            }
            robot.clickOn("#regButton");
            if(i==0)
                robot.clickOn("Log Out");
            else{
                assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with the username "+USERNAME+" already exists!");
            }
        }
    }

    @Test
    void testRegisterFailNoUser(FxRobot robot) {
        robot.clickOn("#toRegButton");
        robot.clickOn("#usernameField");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.clickOn(ROLES[1]);
        robot.clickOn("#regButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please enter a username.");
    }

    @Test
    void testRegisterFailNoPass(FxRobot robot) {
        robot.clickOn("#toRegButton");
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.clickOn("#role");
        robot.clickOn(ROLES[1]);
        robot.clickOn("#regButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("The password must be at least 4 characters long.");
    }

    @Test
    void testRegisterFailNoAux(FxRobot robot) {
        robot.clickOn("#toRegButton");
        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.clickOn(ROLES[0]);
        robot.clickOn("#regButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please enter an address.");
        robot.clickOn("#role");
        robot.clickOn(ROLES[2]);
        robot.clickOn("#regButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Please enter the license plate.");
    }

}