package org.nl.services;

import javafx.scene.control.TextField;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.nl.model.Order;
import org.nl.model.Product;

import java.util.Date;

public class OrderService {
    private static ObjectRepository<Order> orderRepository;

    public static void initDatabase() {
        orderRepository = UserService.getDatabase().getRepository(Order.class);

    }

    public static void addOrder(String username, int idProduct, Date date, String address){
        Order o = new Order(username, idProduct, date, "placed", address);
        orderRepository.insert(o);
    }

    public static Cursor<Order> getAllPlacedOrders() {
        return orderRepository.find(ObjectFilters.eq("status", "placed"));
    }

    public static  Cursor<Order> getAllOrders(){
        return orderRepository.find();
    }

    public static boolean checkOrderName(String prodName, String searchField) {
        String[] words = searchField.split("\\s+");
        for(String s : words){
            if(!prodName.toLowerCase().contains(s.toLowerCase()))
                return false;
        }
        return true;
    }

    public static Order getOrder(Date orderDate, String userOrd){
        return orderRepository.find(
                ObjectFilters.and( ObjectFilters.eq("date", orderDate), ObjectFilters.eq("username", userOrd)),
                FindOptions.limit(0, 1)).toList().get(0);
    }

    public static void processOrder(Date orderDate, String userOrd) {
        Order o = getOrder(orderDate,userOrd);

        orderRepository.remove(ObjectFilters.and(
                ObjectFilters.eq("date", orderDate), ObjectFilters.eq("username", userOrd)));
        o.process();
        orderRepository.insert(o);
    }
}
