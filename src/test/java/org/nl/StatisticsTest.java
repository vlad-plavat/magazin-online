package org.nl;



import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.services.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;
import static org.nl.services.FileSystemService.getFullPath;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class StatisticsTest {

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
    void tearDown(){
        Main.closeDatabases();
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        FileSystemService.setAppFolder("database-test");
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        Main.initDatabases();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("manager/statistica.fxml")));
        primaryStage.setTitle("Nature Leaf Testing");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        StageService.setMainStage(primaryStage);
        tearDown();
        //new Main().start(primaryStage);
    }

    private void copyImage() {
        String finalPath = getFullPath("productImages") + "/" + 100 + ".png";
        try {
            Files.copy(Path.of(Objects.requireNonNull(Main.class.getClassLoader().getResource("icon.png")).
                    getPath().substring(1)), Path.of(finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBackButton(FxRobot robot) throws ProductIDAlreadyExistsException {
        robot.clickOn("Back to menu");
        copyImage();
        ProductService.addProduct(100, "First", 10, "dim1", "desc1", 0, "100.png");

        OrderService.addOrder(USERNAME,100,new Date(),"Address 1");
        OrderService.addOrder(USERNAME,100,new Date(),"Address 2");
        assertThat(OrderService.getAllOrders().size()).isEqualTo(2);

        Instant i1 = Instant.from(LocalDate.now().with(firstDayOfMonth()).atStartOfDay(ZoneId.systemDefault()));
        Instant i2 = Instant.from(LocalDate.now().with(firstDayOfNextMonth()).atStartOfDay(ZoneId.systemDefault()));

        assertThat(OrderService.getAllOrdersBetween(Date.from(i1),Date.from(i2)).size()).isEqualTo(2);
        robot.clickOn("Statistics");

        BarChart<String,Number> chart;
        chart = robot.lookup("#chart").query();

        assertThat(chart.getData().size()).isEqualTo(1);
        float max = 0;
        for(var crSeries : chart.getData().get(0).getData()){
            if(crSeries.getYValue().floatValue()>max)
                max=crSeries.getYValue().floatValue();
        }
        assertThat(max).isEqualTo(2);

        robot.clickOn("#price_units");
        robot.clickOn("Value of orders");

        assertThat(chart.getData().size()).isEqualTo(1);
        max = 0;
        for(var crSeries : chart.getData().get(0).getData()){
            if(crSeries.getYValue().floatValue()>max)
                max=crSeries.getYValue().floatValue();
        }
        assertThat(max).isEqualTo(20);

        robot.clickOn("#time");
        robot.clickOn("year");
        assertThat(chart.getData().size()).isEqualTo(1);
        max = 0;
        for(var crSeries : chart.getData().get(0).getData()){
            if(crSeries.getYValue().floatValue()>max)
                max=crSeries.getYValue().floatValue();
        }
        assertThat(max).isEqualTo(20);


        robot.clickOn("#time");
        robot.clickOn("month");
        assertThat(chart.getData().size()).isEqualTo(1);
        max = 0;
        for(var crSeries : chart.getData().get(0).getData()){
            if(crSeries.getYValue().floatValue()>max)
                max=crSeries.getYValue().floatValue();
        }
        assertThat(max).isEqualTo(20);


        robot.clickOn("#time");
        robot.clickOn("week");
        assertThat(chart.getData().size()).isEqualTo(1);
        max = 0;
        for(var crSeries : chart.getData().get(0).getData()){
            if(crSeries.getYValue().floatValue()>max)
                max=crSeries.getYValue().floatValue();
        }
        assertThat(max).isEqualTo(20);


        robot.clickOn("#time");
        robot.clickOn("day");
        assertThat(chart.getData().size()).isEqualTo(1);
        max = 0;
        for(var crSeries : chart.getData().get(0).getData()){
            if(crSeries.getYValue().floatValue()>max)
                max=crSeries.getYValue().floatValue();
        }
        assertThat(max).isEqualTo(20);

    }


}