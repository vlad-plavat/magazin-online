package org.nl.model;

import org.dizitart.no2.objects.Id;

import java.util.Objects;

public class Product {
    @Id
    private int idProdct;
    private String name;
    private float price;
    private String dimensions;
    private String description;
    private int stock;
    private String imageAddr;

    public Product(int idProdct, String name, float price, String dimensions, String description, int stock, String imageAddr) {
        this.idProdct = idProdct;
        this.name = name;
        this.price = price;
        this.dimensions = dimensions;
        this.description = description;
        this.stock = stock;
        this.imageAddr = imageAddr;
    }

    public Product(){

    }

    public int getIdProdct() {
        return idProdct;
    }

    public void setIdProdct(int idProdct) {
        this.idProdct = idProdct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageAddr() {
        return imageAddr;
    }

    public void setImageAddr(String imageAddr) {
        this.imageAddr = imageAddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return idProdct == product.idProdct && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProdct, name);
    }
}
