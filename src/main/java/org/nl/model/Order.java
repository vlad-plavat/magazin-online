package org.nl.model;

import java.util.Date;
import java.util.Objects;

public class Order {
    private String username;
    private int idProduct;
    private Date date;
    private String status;
    private String address;

    public Order(){}

    public Order(String username, int idProduct, Date date, String status, String address) {
        this.username = username;
        this.idProduct = idProduct;
        this.date = date;
        this.status = status;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(username, order.username) && Objects.equals(idProduct, order.idProduct) && Objects.equals(date, order.date) && Objects.equals(status, order.status) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, idProduct, date, status, address);
    }
}
