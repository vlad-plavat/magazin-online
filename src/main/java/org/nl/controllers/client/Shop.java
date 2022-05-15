package org.nl.controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.dizitart.no2.objects.Cursor;
import org.nl.Main;
import org.nl.model.Product;
import org.nl.services.ProductService;
import org.nl.services.StageService;

import java.net.URL;

import static org.nl.services.FileSystemService.imgFromPrd;

public class Shop {
    @FXML
    public ChoiceBox<String> sortBy;
    @FXML
    public CheckBox onlyStock;
    @FXML
    public TextField searchField;
    @FXML
    public TextField maxPrice;
    @FXML
    public TextField minPrice;

    //private ArrayList<Pane> produseAfisate = new ArrayList<>();

    @FXML
    private AnchorPane pane;
    @FXML
    public void initialize() {
        //i.setImage(new Image("file:/E:/ceva.png"));
        //i.imageProperty().set(new Image("file:/E:/ceva.png"));
        loadAllProducts();
        sortBy.getItems().addAll("Sort by...", "Price ascending","Price descending");
        sortBy.setValue("Sort by...");
        sortBy.setOnAction(this::reloadProducts);
    }

    @FXML
    public void goBack(ActionEvent evt){
        StageService.loadPage(evt,"Menus/Client.fxml");
    }

    private void loadAllProducts(){
        for(int chind=pane.getChildren().size();chind>0;chind--) {
            pane.getChildren().remove(0);
        }
        Cursor<Product> all = ProductService.getAllProducts();
        int i = 0;
        for(Product p : all){
            addProductOnScreen(p,i);
            i++;
        }
        pane.setPrefHeight(i*125);
    }

    private void addProductOnScreen(Product p, int i){
        try {
            URL toFxml = Main.class.getClassLoader().getResource("item.fxml");
            if(toFxml == null)
                throw new RuntimeException("Could not load fxml file item.fxml");
            Pane newPane = FXMLLoader.load(toFxml);
            pane.getChildren().add(newPane);

            ((Text)newPane.getChildren().get(1)).setText(p.getName());
            ((Text)newPane.getChildren().get(2)).setText(String.format("Price: $%.2f",p.getPrice()));
            ((Text)newPane.getChildren().get(3)).setText("Dimensions: " + p.getDimensions());
            if(p.getStock()>0){
                ((Text)newPane.getChildren().get(4)).setText("In stock");
                ((Text)newPane.getChildren().get(4)).setFill(Color.GREEN);
            }else{
                ((Text)newPane.getChildren().get(4)).setText("Out of stock");
                ((Text)newPane.getChildren().get(4)).setFill(Color.RED);
            }
            ((Text)newPane.getChildren().get(7)).setText(""+p.getIdProdct());

            newPane.setLayoutY(i*125);
            ((ImageView)newPane.getChildren().get(0)).setImage(new Image(imgFromPrd(p)));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    public void reloadProducts(ActionEvent actionEvent) {
        for(int chind=pane.getChildren().size();chind>0;chind--) {
            pane.getChildren().remove(0);
        }
        String sort = sortBy.getValue();

        Cursor<Product> all;
        if(sort.equals("Sort by..."))
            all = ProductService.getAllProducts();
        else
            if(sort.equals("Price ascending"))
                all = ProductService.getAllProducts(true);
            else
                all = ProductService.getAllProducts(false);


        int i = 0;

        for(Product p : all){
            if(ProductService.checkProductStockName(p,searchField,onlyStock.isSelected()))
                if(ProductService.checkProductPrice(p,minPrice,maxPrice)) {
                    addProductOnScreen(p, i);
                    i++;
                }
        }
        pane.setPrefHeight(i*125);
    }
}
