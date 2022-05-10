package org.nl.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.nl.model.Order;

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
}
