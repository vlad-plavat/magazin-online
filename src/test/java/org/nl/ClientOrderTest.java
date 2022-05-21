package org.nl;


import javafx.fxml.FXMLLoader;
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
import org.nl.model.User;
import org.nl.services.FileSystemService;
import org.nl.services.OrderService;
import org.nl.services.ProductService;
import org.nl.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.nl.services.FileSystemService.getFullPath;
import static org.testfx.assertions.api.Assertions.assertThat;

class NoException extends Exception{}

@ExtendWith(ApplicationExtension.class)
public class ClientOrderTest {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("client/shop.fxml")));
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
    void testProductOrderFailOutOfStock(FxRobot robot) throws ProductIDAlreadyExistsException {
        Pane p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(0);
        copyImage(100);
        copyImage(101);
        copyImage(102);
        ProductService.addProduct(100, "First", 10, "dim1", "desc1", 0, "100.png");
        ProductService.addProduct(101, "Second", 20, "dim2", "desc2", 2, "101.png");
        ProductService.addProduct(102, "Third", 30, "dim3", "desc3", 3, "102.png");

        robot.doubleClickOn("#searchField");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        assertThat(p.getChildren().size()).isEqualTo(3);

        robot.clickOn("#searchField");
        robot.write("First");
        robot.push(KeyCode.ENTER);
        p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(1);

        assertThat(OrderService.getAllOrders().toList().size()).isEqualTo(0);
        assertThrows(NoException.class, () -> {
            robot.clickOn("Order");
            robot.clickOn("The selected product is out of stock.");
            robot.clickOn("Ok");
            throw new NoException();
        });
        assertThat(OrderService.getAllOrders().toList().size()).isEqualTo(0);
    }

    @Test
    void testProductOrderSuccesses(FxRobot robot) throws ProductIDAlreadyExistsException {
        Pane p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(0);
        copyImage(100);
        copyImage(101);
        copyImage(102);
        ProductService.addProduct(100, "First", 10, "dim1", "desc1", 0, "100.png");
        ProductService.addProduct(101, "Second", 20, "dim2", "desc2", 1, "101.png");
        ProductService.addProduct(102, "Third", 30, "dim3", "desc3", 2, "102.png");

        robot.doubleClickOn("#searchField");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        assertThat(p.getChildren().size()).isEqualTo(3);

        robot.clickOn("#searchField");
        robot.write("Second");
        robot.push(KeyCode.ENTER);
        p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(1);

        RegistrationController.loggeduser = new User(USERNAME,UserService.encodePassword(USERNAME,PASSWORD),
                "Client","Address of client");

        assertThat(OrderService.getAllOrders().toList().size()).isEqualTo(0);

        assertThrows(NoException.class, () -> {
            robot.clickOn("Order");
            robot.clickOn("Confirm order");
            throw new NoException();
        });
        assertThat(OrderService.getAllOrders().toList().size()).isEqualTo(1);
        org.nl.model.Order o = OrderService.getAllOrders().toList().get(0);
        assertThat(o.getIdProduct()).isEqualTo(101);
        assertThat(o.getUsername()).isEqualTo(USERNAME);
        assertThat(o.getAddress()).isEqualTo("Address of client");
        assertThat(o.getStatus()).isEqualTo("placed");


        robot.clickOn("#searchField");
        robot.write("Second");
        robot.push(KeyCode.ENTER);
        assertThrows(NoException.class, () -> {
            robot.clickOn("Out of stock");
            throw new NoException();
        });

        robot.doubleClickOn("#searchField");
        robot.write("Third");
        robot.push(KeyCode.ENTER);
        assertThrows(NoException.class, () -> {
            robot.clickOn("Order");
            robot.doubleClickOn("#addressField");
            robot.clickOn("#addressField");
            robot.write("New Address");
            robot.clickOn("Confirm order");
            throw new NoException();
        });

        assertThat(OrderService.getAllOrders().toList().size()).isEqualTo(2);
        org.nl.model.Order o0 = OrderService.getAllOrders().toList().get(0);
        assertThat(o0.getIdProduct()).isEqualTo(101);
        assertThat(o0.getUsername()).isEqualTo(USERNAME);
        assertThat(o0.getAddress()).isEqualTo("Address of client");
        assertThat(o0.getStatus()).isEqualTo("placed");
        org.nl.model.Order o1 = OrderService.getAllOrders().toList().get(1);
        assertThat(o1.getIdProduct()).isEqualTo(102);
        assertThat(o1.getUsername()).isEqualTo(USERNAME);
        assertThat(o1.getAddress()).isEqualTo("New Address");
        assertThat(o1.getStatus()).isEqualTo("placed");
        assertThat(o1.getDate()).isNotEqualTo(o0.getDate());

        assertThat(ProductService.getProduct(100).getStock()).isEqualTo(0);
        assertThat(ProductService.getProduct(101).getStock()).isEqualTo(0);
        assertThat(ProductService.getProduct(102).getStock()).isEqualTo(1);
    }
}
