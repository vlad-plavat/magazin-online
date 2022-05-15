package org.nl.services;

import javafx.scene.control.TextField;
import org.apache.commons.io.FilenameUtils;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.nl.Main;
import org.nl.controllers.RegistrationController;
import org.nl.controllers.StoreCheckController;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.exceptions.SimpleTextException;
import org.nl.exceptions.UsernameAlreadyExistsException;
import org.nl.model.Product;
import org.nl.model.User;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.nl.services.FileSystemService.getFullPath;

public class ProductService {

    private static ObjectRepository<Product> productRepository;

    public static void initDatabase() {

        productRepository = UserService.getDatabase().getRepository(Product.class);
        /*try {
            addProduct(7,"ceva",4.99f,"mare","fain",1,"file:/E:/sofa.jpg");
        } catch (ProductIDAlreadyExistsException ignored) {

        }*/
    }

    public static void addProduct(int idProdct, String name, float price, String dimensions, String description, int stock, String imageAddr)
            throws ProductIDAlreadyExistsException {
        checkProductIDDoesNotAlreadyExist(idProdct);
        Product p = new Product(idProdct,name,price,dimensions,description,stock,imageAddr);
        productRepository.insert(p);
    }

    public static Product getProduct(int idProduct){
        try {
            return productRepository.find(
                    ObjectFilters.eq("idProdct", idProduct), FindOptions.limit(0, 1)).toList().get(0);
        }catch (IndexOutOfBoundsException e){
            return new Product();
        }

    }

    private static void checkProductIDDoesNotAlreadyExist(int ID) throws ProductIDAlreadyExistsException {
        for (Product prod : productRepository.find()) {
            if (ID == prod.getIdProdct())
                throw new ProductIDAlreadyExistsException(ID);
        }
    }

    public static boolean doesIdExist(int ID) {
        for (Product prod : productRepository.find()) {
            if (ID == prod.getIdProdct())
                return true;
        }
        return false;
    }

    public static Cursor<Product> getAllProducts(){
        return productRepository.find();
    }

    public static Cursor<Product> getAllProducts(boolean ascending){
        return productRepository.find(
                FindOptions.sort("price" , ascending?SortOrder.Ascending:SortOrder.Descending)
        );
    }

    public static boolean checkProductStockName(Product p, TextField searchField, boolean onlyStock) {
        if(onlyStock && p.getStock()==0)
            return false;
        String[] words = searchField.getText().split("\\s+");
        for(String s : words){
            if(!p.getName().toLowerCase().contains(s.toLowerCase()))
                return false;
        }
        return true;
    }

    public static void orderProduct(int productID){
        Product p = getProduct(productID);
        p.decreaseStock();
        productRepository.update(p);
    }

    public static void removeProduct(int productID, StoreCheckController scc){
        Product p = getProduct(productID);
        String finalPath = getFullPath("productImages") +"/"+ p.getImageAddr();
        try {
            productRepository.remove(p);
            scc.reloadProducts(null);
            Files.deleteIfExists(Path.of(finalPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkProductPrice(Product p, TextField minPrice, TextField maxPrice) {
        minPrice.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: grey;");
        maxPrice.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: grey;");
        boolean checkMin = !minPrice.getText().isEmpty();
        boolean checkMax = !maxPrice.getText().isEmpty();
        if(checkMin){
            try {
                float minP = Float.parseFloat(minPrice.getText());
                if(p.getPrice()+0.00001f<minP)
                    return false;
            }catch (NumberFormatException e){
                minPrice.setStyle("-fx-background-color: lightcoral; -fx-border-width: 1px; -fx-border-color: grey;");
            }
        }
        if(checkMax){
            try {
                float maxP = Float.parseFloat(maxPrice.getText());
                if(p.getPrice()-0.00001f>maxP)
                    return false;
            }catch (NumberFormatException e){
                maxPrice.setStyle("-fx-background-color: lightcoral; -fx-border-width: 1px; -fx-border-color: grey;");
            }
        }
        return true;
    }

    public static void changeProductData(int idProduct, String name, float price, String dimensions, String description, int stock, String img){

        Product updatedProduct = new Product(idProduct, name, price, dimensions, description, stock, img);
        productRepository.update(updatedProduct);

        //return new User(username, encodePassword(username, password), aux);

    }

}
