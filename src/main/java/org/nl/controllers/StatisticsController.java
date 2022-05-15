package org.nl.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import org.dizitart.no2.objects.Cursor;
import org.nl.model.Order;
import org.nl.services.OrderService;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

import static java.time.temporal.TemporalAdjusters.*;

public class StatisticsController {
    final private String[] months = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    final private String[] days = {"","Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    final private String sPrice="Value of orders";
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
        String sUnits = "Units sold";
        price_units.getItems().addAll(sUnits,sPrice);
        price_units.setValue(sUnits);
        price_units.setOnAction(this::reloadGraph);
        time.getItems().addAll("year","month","week","day");
        time.setValue("month");
        time.setOnAction(this::reloadGraph);
        datePicker.setValue(LocalDate.now());

        reloadGraph(null);
    }

    @FXML
    private void reloadGraph(ActionEvent evt){
        LocalDate selectedDate = datePicker.getValue();
        Instant i1=Instant.now(),i2=Instant.now();
        switch (time.getValue()){
            case "year":{
                i1 = Instant.from(selectedDate.with(firstDayOfYear()).atStartOfDay(ZoneId.systemDefault()));
                i2 = Instant.from(selectedDate.with(firstDayOfNextYear()).atStartOfDay(ZoneId.systemDefault()));
                break;}
            case "month":{
                i1 = Instant.from(selectedDate.with(firstDayOfMonth()).atStartOfDay(ZoneId.systemDefault()));
                i2 = Instant.from(selectedDate.with(firstDayOfNextMonth()).atStartOfDay(ZoneId.systemDefault()));
                break;}
            case "week":{
                i1 = Instant.from(selectedDate.minusDays(6).atStartOfDay(ZoneId.systemDefault()));
                i2 = Instant.from(selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()));
                break;}
            case "day":{
                i1 = Instant.from(selectedDate.atStartOfDay(ZoneId.systemDefault()));
                i2 = Instant.from(selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()));
                break;}
        }

        Cursor<Order> orders = OrderService.getAllOrdersBetween(Date.from(i1),Date.from(i2));


        XYChart.Series<String,Number> series1 = new XYChart.Series<>();

        switch (time.getValue()){
            case "year":{statYear(series1,orders);break;}
            case "month":{statMonth(Date.from(i1),series1,orders);break;}
            case "week":{statWeek(Date.from(i1),series1,orders);break;}
            case "day":{statDay(series1,orders);break;}
        }

        //OrderService.getAllOrders();
        chart.setLegendVisible(false);
        chart.getData().clear();
        chart.getData().add(series1);
    }

    private void statYear(XYChart.Series<String,Number> series,Cursor<Order> orders){
        float[] vecm = new float[13];
        for(int i=1;i<=12;i++){
            vecm[i]=0;
        }
        for(Order o : orders){
            SimpleDateFormat formatter = new SimpleDateFormat("MM");
            int month = Integer.parseInt(formatter.format(o.getDate()));
            if(price_units.getValue().equals(sPrice))
                vecm[month]+= ProductService.getProduct(o.getIdProduct()).getPrice();
            else
                vecm[month]++;
        }
        for(int i=1;i<=12;i++){
            series.getData().add(new XYChart.Data<>(months[i], vecm[i]));
        }
    }

    private void statMonth(Date date0,XYChart.Series<String,Number> series,Cursor<Order> orders){
        float[] vecd = new float[32];
        for(int i=1;i<=31;i++){
            vecd[i]=0;
        }
        for(Order o : orders){
            SimpleDateFormat formatter = new SimpleDateFormat("dd");
            int day = Integer.parseInt(formatter.format(o.getDate()));

            if(price_units.getValue().equals(sPrice))
                vecd[day]+= ProductService.getProduct(o.getIdProduct()).getPrice();
            else
                vecd[day]++;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(formatter.format(date0));
        formatter = new SimpleDateFormat("MM");
        int month = Integer.parseInt(formatter.format(date0));

        for(int i = 1; i<= YearMonth.of(year, month).lengthOfMonth(); i++){
            series.getData().add(new XYChart.Data<>(""+i, vecd[i]));
        }
    }

    private void statWeek(Date date0,XYChart.Series<String,Number> series,Cursor<Order> orders){
        float[] vecd = new float[8];
        for(int i=1;i<=7;i++){
            vecd[i]=0;
        }
        SimpleDateFormat f = new SimpleDateFormat("u");
        int first = Integer.parseInt(f.format(date0));

        for(Order o : orders){
            SimpleDateFormat formatter = new SimpleDateFormat("u");
            int day = Integer.parseInt(formatter.format(o.getDate()));

            if(price_units.getValue().equals(sPrice))
                vecd[day]+= ProductService.getProduct(o.getIdProduct()).getPrice();
            else
                vecd[day]++;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(" (dd/MM/yyyy)");
        for(int i = 0; i< 7; i++){
            int crday = (first+i-1)%7+1;
            String dateS = formatter.format(Date.from(date0.toInstant().plusSeconds(i*60*60*24)));
            series.getData().add(new XYChart.Data<>(dateS+days[crday], vecd[crday]));
        }
    }
    private void statDay(XYChart.Series<String,Number> series,Cursor<Order> orders){
        float[] vecd = new float[8];
        for(int i=0;i < 8;i++){
            vecd[i]=0;
        }
        for(Order o : orders){
            SimpleDateFormat formatter = new SimpleDateFormat("HH");
            int hour = Integer.parseInt(formatter.format(o.getDate()));

            if(price_units.getValue().equals(sPrice))
                vecd[hour/3]+= ProductService.getProduct(o.getIdProduct()).getPrice();
            else
                vecd[hour/3]++;
        }

        for(int i = 0; i < 8; i++){
            series.getData().add(new XYChart.Data<>((i*3)+":00-"+(i*3+3)+":00", vecd[i]));
        }
    }


}
