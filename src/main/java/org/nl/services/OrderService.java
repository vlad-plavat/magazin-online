package org.nl.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.nl.model.Order;

import java.util.Date;

public class OrderService {
    private static ObjectRepository<Order> orderRepository;

    public static void initDatabase() {
        orderRepository = UserService.getDatabase().getRepository(Order.class);

    }

    public static Order addOrder(String username, String idProduct, Date date, String status, String address){
        Order o = new Order(username, idProduct, date, status, address);
        orderRepository.insert(o);
        return o;
    }
}
