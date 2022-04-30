package org.nl.services;

import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.nl.exceptions.ProductIDAlreadyExistsException;
import org.nl.model.Product;

import java.util.ArrayList;
import java.util.Objects;

import static org.nl.services.FileSystemService.getPathToFile;

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
}
