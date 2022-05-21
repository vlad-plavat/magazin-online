package org.nl;



import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nl.controllers.RegistrationController;
import org.nl.model.User;
import org.nl.services.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.assertions.api.Assertions.assertThat;



@ExtendWith(ApplicationExtension.class)
class StoreCheckTest {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";
    public static final String[] ROLES = new String[]{"Client", "Worker", "Courier", "Manager"};

    private void onlyInit() throws Exception{
        FileSystemService.setAppFolder("database-test");
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        Main.initDatabases();
    }

    @BeforeEach
    void setUp() throws Exception {
        onlyInit();
        UserService.addUser(USERNAME, PASSWORD, ROLES[3], "");
        ProductService.addProduct(0, "Chair", (float) 24.99, "H: 50 cm", "description", 5, "");
        Date d1 = new Date();

        ProductService.addProduct(1, "Sofa", (float) 37.99, "H: 100 cm", "description", 0, "");
        Date d2 = new Date();


    }

    @AfterEach
    void tearDown(){
        Main.closeDatabases();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        onlyInit();
        RegistrationController.loggeduser = new User(USERNAME, UserService.encodePassword(USERNAME, PASSWORD), ROLES[3], "");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("manager/StoreCheck.fxml")));
        primaryStage.setTitle("Nature Leaf Testing");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        StageService.setMainStage(primaryStage);
        tearDown();
        //new Main().start(primaryStage);
    }

    @Test
    void testBackButton(FxRobot robot) {
        robot.clickOn("Back to menu");
        robot.clickOn("Store Check");
    }

    @Test
    void testSearch(FxRobot robot) {
        robot.clickOn("#searchField");
        robot.push(KeyCode.ENTER);
        Pane p = robot.lookup("#pane").query();
        Assertions.assertThat(p.getChildren().size()).isEqualTo(2);
        robot.clickOn("#searchField");
        robot.write("Chair");
        robot.push(KeyCode.ENTER);
        Assertions.assertThat(p.getChildren().size()).isEqualTo(1);
    }

    @Test
    void testOnlyInStock(FxRobot robot) {
        robot.clickOn("#searchField");
        robot.push(KeyCode.ENTER);
        Pane p = robot.lookup("#pane").query();
        Assertions.assertThat(p.getChildren().size()).isEqualTo(2);
        robot.clickOn("#onlyStock");
        Assertions.assertThat(p.getChildren().size()).isEqualTo(1);
    }

    @Test
    void testChangeProductData(FxRobot robot) {
        robot.clickOn("#searchField");
        robot.push(KeyCode.ENTER);
        robot.doubleClickOn("#nameField");
        robot.write("Scaun");
        robot.push(KeyCode.ENTER);
        assertThat(ProductService.getProduct(0).getName()).isEqualTo("Scaun");
        robot.doubleClickOn("#priceField");
        robot.write("19.99");
        robot.push(KeyCode.ENTER);
        assertThat(ProductService.getProduct(0).getPrice()).isEqualTo((float)19.99);
        Node n = (Node) robot.lookup("#stockField").queryAll().toArray()[1];
        robot.doubleClickOn(n);
        robot.write("10");
        robot.push(KeyCode.ENTER);
        assertThat(ProductService.getProduct(1).getStock()).isEqualTo(10);

    }

    @Test
    void withdrawButtonTest(FxRobot robot){
        robot.clickOn("#searchField");
        robot.push(KeyCode.ENTER);
        robot.clickOn("#withdrawButton");
        Pane p = robot.lookup("#pane").query();
        Assertions.assertThat(p.getChildren().size()).isEqualTo(2);
        robot.clickOn("No");
        Assertions.assertThat(p.getChildren().size()).isEqualTo(2);
        robot.clickOn("#withdrawButton");
        Assertions.assertThat(p.getChildren().size()).isEqualTo(2);
        robot.clickOn("Yes");
        Assertions.assertThat(p.getChildren().size()).isEqualTo(1);
    }


    @Test
    void sortByTest(FxRobot robot){
        robot.clickOn("#searchField");
        robot.push(KeyCode.ENTER);
        robot.clickOn("#sortBy");
        robot.clickOn("Price descending");

        double pos1,pos2;
        Node n;
        n = robot.lookup("Sofa").query();  assertThat(n).isNotNull();
        pos1 = n.getParent().getLayoutY();
        n = robot.lookup("Chair").query();  assertThat(n).isNotNull();
        pos2 = n.getParent().getLayoutY();
        Assertions.assertThat(pos1).isLessThan(pos2);

        robot.clickOn("#sortBy");
        robot.clickOn("Price ascending");
        n = robot.lookup("Chair").query();  assertThat(n).isNotNull();
        pos1 = n.getParent().getLayoutY();
        n = robot.lookup("Sofa").query();  assertThat(n).isNotNull();
        pos2 = n.getParent().getLayoutY();
        Assertions.assertThat(pos1).isLessThan(pos2);
    }

    @Test
    void addProductTestFailed(FxRobot robot){
        robot.clickOn("Add product");
        robot.clickOn("Save product");
        assertThrows(NoException.class, () -> {

            robot.clickOn("Enter the product's name.");
            robot.clickOn("Ok");
            throw new NoException();
        });

        robot.clickOn("#nameField");
        robot.write("Table");
        robot.clickOn("Save product");
        assertThrows(NoException.class, () -> {

            robot.clickOn("Check price entered!");
            robot.clickOn("Ok");
            throw new NoException();

        });
        robot.clickOn("#priceField");
        robot.write("32.99");
        robot.clickOn("Save product");
        assertThrows(NoException.class, () -> {

            robot.clickOn("Check stock entered!");
            robot.clickOn("Ok");
            throw new NoException();

        });
        robot.clickOn("#stockField");
        robot.write("13");
        robot.clickOn("Save product");
        assertThrows(NoException.class, () -> {

            robot.clickOn("Enter the product's dimensions.");
            robot.clickOn("Ok");
            throw new NoException();

        });

        robot.clickOn("#dimensionsField");
        robot.write("H: 70 cm");
        robot.clickOn("Save product");
        assertThrows(NoException.class, () -> {

            robot.clickOn("Select a valid image!");
            robot.clickOn("Ok");
            throw new NoException();

        });
        robot.push(KeyCode.ALT, KeyCode.F4);

    }


}