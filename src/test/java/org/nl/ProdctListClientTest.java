package org.nl;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.services.FileSystemService;
import org.nl.services.ProductService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.nl.services.FileSystemService.getFullPath;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class ProdctListClientTest {

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.setAppFolder("database-test");
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
        setUp();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("client/shop.fxml")));
        primaryStage.setTitle("Nature Leaf Testing");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        //new Main().start(primaryStage);
        tearDown();
    }

    @Test
    void testProductViewEmpty(FxRobot robot) {
        Pane p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(0);
    }

    private void copyImage(int productId){
        String finalPath = getFullPath("productImages") + "/" + productId + ".png";
        try {
            Files.copy(Path.of(Objects.requireNonNull(Main.class.getClassLoader().getResource("icon.png")).
                    getPath().substring(1)), Path.of(finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testProductHasItemsAndFilter(FxRobot robot) throws ProductIDAlreadyExistsException {
        Pane p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(0);
        copyImage(100);copyImage(101);copyImage(102);
        ProductService.addProduct(100,"First" ,10,"dim1","desc1",0,"100.png");
        ProductService.addProduct(101,"Second",20,"dim2","desc2",2,"101.png");
        ProductService.addProduct(102,"Third" ,30,"dim3","desc3",3,"102.png");
        robot.clickOn("Back to menu");
        robot.clickOn("Shop");
        p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(3);
        Node n;
        n = robot.lookup("First").query();  assertThat(n).isNotNull();
        n = robot.lookup("Second").query(); assertThat(n).isNotNull();
        n = robot.lookup("Third").query();  assertThat(n).isNotNull();

        robot.clickOn("#searchField");
        robot.write("ird");
        robot.push(KeyCode.ENTER);
        p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(1);
        n = robot.lookup("Third").query();  assertThat(n).isNotNull();

        robot.doubleClickOn("#searchField");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(3);

        robot.clickOn("#minPrice");
        robot.write("15");
        robot.push(KeyCode.ENTER);
        robot.clickOn("#maxPrice");
        robot.write("28");
        robot.push(KeyCode.ENTER);
        p = robot.lookup("#pane").query();
        assertThat(p.getChildren().size()).isEqualTo(1);
        n = robot.lookup("Second").query();  assertThat(n).isNotNull();

        robot.doubleClickOn("#minPrice");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        robot.doubleClickOn("#maxPrice");
        robot.push(KeyCode.BACK_SPACE);
        robot.push(KeyCode.ENTER);
        assertThat(p.getChildren().size()).isEqualTo(3);
        robot.clickOn("#onlyStock");
        assertThat(p.getChildren().size()).isEqualTo(2);
        n = robot.lookup("Second").query();  assertThat(n).isNotNull();
        n = robot.lookup("Third").query();  assertThat(n).isNotNull();

        robot.clickOn("#onlyStock");
        assertThat(p.getChildren().size()).isEqualTo(3);
        robot.clickOn("#sortBy");
        robot.clickOn("Price ascending");

        double pos1,pos2,pos3;
        n = robot.lookup("First").query();  assertThat(n).isNotNull();
        pos1 = n.getParent().getLayoutY();
        n = robot.lookup("Second").query();  assertThat(n).isNotNull();
        pos2 = n.getParent().getLayoutY();
        n = robot.lookup("Third").query();  assertThat(n).isNotNull();
        pos3 = n.getParent().getLayoutY();
        assertThat(pos1).isLessThan(pos2);
        assertThat(pos2).isLessThan(pos3);


        robot.clickOn("#sortBy");
        robot.clickOn("Price descending");
        n = robot.lookup("First").query();  assertThat(n).isNotNull();
        pos1 = n.getParent().getLayoutY();
        n = robot.lookup("Second").query();  assertThat(n).isNotNull();
        pos2 = n.getParent().getLayoutY();
        n = robot.lookup("Third").query();  assertThat(n).isNotNull();
        pos3 = n.getParent().getLayoutY();
        assertThat(pos2).isLessThan(pos1);
        assertThat(pos3).isLessThan(pos2);
    }
}
