package com.driver;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    OrderRepository orderRepository = new OrderRepository();

    public void addOrderbyBody(Order order) throws Exception {
        orderRepository.addOrderToDB(order);
    }


    public void addPartnerByID(String partnerId) throws Exception {
        orderRepository.addPartnerToDB(partnerId);
    }

    public void addOrderToPartner(String orderId, String partnerId) throws Exception {
        orderRepository.addOrderPartnerPairToDB(orderId,partnerId);
    }

    public Order getOrder(String orderId) {
        return orderRepository.getOrderFromDB(orderId);
    }

    public DeliveryPartner getDeliveryPartner(String partnerId) {
        return orderRepository.getDeliveryPartnerFromDB(partnerId);
    }

    public Integer getOrderCount(String partnerId) throws Exception {
        if(!orderRepository.deliveryPartnerMap.containsKey(partnerId)){
            throw new Exception("partner Id doesnt Exist");
        }
        if(!orderRepository.orderPartnerMap.containsKey(partnerId)){
            return 0;
        }
        return orderRepository.getOrderCountFromID(partnerId);
    }

    public List<String> getOrdersList(String partnerId) throws Exception {
        if(!orderRepository.deliveryPartnerMap.containsKey(partnerId)){
            throw new Exception(("Partner ID doesn't Exist"));
        }
        return orderRepository.getOrderListFromID(partnerId);
    }

    public List<String> getOrders() {
        if(orderRepository.orderMap.isEmpty()){
            return new ArrayList<>();
        }
        return orderRepository.getAllOrdersFromDB();
    }

    public Integer getCount() {
        int totalOrderCount = orderRepository.getOrderCount();
        int totalAssignedOrders = orderRepository.getAssignedOrderCount();
        return totalOrderCount-totalAssignedOrders;
    }

    public Integer getOrdersLeftOut(String time, String partnerId) throws Exception {
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3));
        int timeLimit = (hour*60) + minute;
        if(!orderRepository.deliveryPartnerMap.containsKey(partnerId)){
            throw new Exception("Partner ID doesn't Exist");
        }
        return orderRepository.getRemainingNoOfOrders(timeLimit,partnerId);

    }

    public String getLastDeliveryTime(String partnerId) throws Exception {
        if(!orderRepository.deliveryPartnerMap.containsKey(partnerId)){
            throw new Exception("Partner doesnt exist");
        }
       int time = orderRepository.getLastDeliveryTimeOfPartner(partnerId);
        return LocalTime.MIN.plus(Duration.ofMinutes( time )).toString();
    }

    public void deletePartner(String partnerId) throws Exception {
        orderRepository.deletePartnerFromDB(partnerId);
    }


    public void deleteOrder(String orderId) throws Exception {
        orderRepository.deleteOrderFromDB(orderId);
    }
}
