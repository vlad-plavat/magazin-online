package org.nl.services;

import javafx.scene.control.TextField;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.nl.controllers.RegistrationController;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.model.Product;
import org.nl.model.User;

public class ProductService {

    private static ObjectRepository<Product> productRepository;

    public static void initDatabase() {

        productRepository = UserService.getDatabase().getRepository(Product.class);

    }

    public static Product addProduct(int idProdct, String name, float price, String dimensions, String description, int stock, String imageAddr)
            throws ProductIDAlreadyExistsException {
        checkProductIDDoesNotAlreadyExist(idProdct);
        Product p = new Product(idProdct,name,price,dimensions,description,stock,imageAddr);
        productRepository.insert(p);
        return p;
    }

    public static Product getProduct(int idProduct){
        return productRepository.find(
                ObjectFilters.eq("idProdct", idProduct), FindOptions.limit(0, 1)).toList().get(0);
    }

    private static void checkProductIDDoesNotAlreadyExist(int ID) throws ProductIDAlreadyExistsException {
        for (Product prod : productRepository.find()) {
            if (ID == prod.getIdProdct())
                throw new ProductIDAlreadyExistsException(ID);
        }
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
        productRepository.remove(p);
        p.decreaseStock();
        productRepository.insert(p);
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
}
