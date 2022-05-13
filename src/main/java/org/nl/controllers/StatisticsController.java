package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import org.nl.services.OrderService;
import org.nl.services.StageService;

import java.time.LocalDate;
import java.util.Date;

public class StatisticsController {
    final String sUnits = "Units sold", sPrice="Value of orders";
    @FXML
    public DatePicker datePicker;
    @FXML
    public ChoiceBox<String> price_units;
    @FXML
    public ChoiceBox<String> time;
    @FXML
    private BarChart<String,Number> chart;

    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/Manager.fxml");
    }

    @FXML
    public void initialize(){
        price_units.getItems().addAll(sUnits,sPrice);
        price_units.setValue(sUnits);
        time.getItems().addAll("year","moth","week","day");
        time.setValue("year");
        datePicker.setValue(LocalDate.now());

        reloadGraph();
    }

    private void reloadGraph(){
        XYChart.Series<String,Number> series1 = new XYChart.Series<>();
        /*series1.getData().add(new XYChart.Data("austria", 25601.34));
        series1.getData().add(new XYChart.Data("brazil", 20148.82));
        series1.getData().add(new XYChart.Data("france", 10000));
        series1.getData().add(new XYChart.Data("italy", 35407.15));
        series1.getData().add(new XYChart.Data("usa", 12000));*/

        //OrderService.getAllOrders();
        chart.setLegendVisible(false);
        chart.getData().add(series1);
    }


}
