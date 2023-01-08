package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {


    //stores orderID - order object
    HashMap<String,Order> orderMap;

    //stores // partnerID - deliverPartner Object
    HashMap<String,DeliveryPartner> deliveryPartnerMap;

    //Partner ID - list of all the orders assigned to them
    HashMap<String, List<String>> orderPartnerMap;



    public OrderRepository(){
        this.orderMap = new HashMap<>();
        this.deliveryPartnerMap = new HashMap<>();
        this.orderPartnerMap = new HashMap<>();

    }

    public void addOrderToDB(Order order) throws Exception {
        String str = order.getId();
        if(orderMap.containsKey(str)){
            throw new Exception("Order ID Already Exists");
        }

        orderMap.put(str,order);
    }

    public void addPartnerToDB(String partnerId) throws Exception {
        if(deliveryPartnerMap.containsKey(partnerId)){
            throw  new Exception("Partner with the ID Already Exists");
        }

        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        deliveryPartnerMap.put(partnerId,deliveryPartner);
    }

    public void addOrderPartnerPairToDB(String orderId, String partnerId) throws Exception {
        if(!orderMap.containsKey(orderId)){
            throw  new Exception("Order ID Doesn't Exist");
        }
        if(!deliveryPartnerMap.containsKey(partnerId)){
            throw new Exception("partner ID Doesn't Exist");
        }

        if(orderPartnerMap.containsKey(partnerId)){
            for(String str: orderPartnerMap.get(partnerId)){
                if(str.equals(orderId)){
                    throw new Exception("Order is Already Assigned");
                }
            }

            DeliveryPartner deliveryPartner = deliveryPartnerMap.get(partnerId);
            int noOfOrders = deliveryPartner.getNumberOfOrders();
            deliveryPartner.setNumberOfOrders(noOfOrders+1);
            deliveryPartnerMap.put(partnerId,deliveryPartner);

            List<String> list = orderPartnerMap.get(partnerId);
            list.add(orderId);
            orderPartnerMap.put(partnerId,list);
        }else{

            DeliveryPartner deliveryPartner = deliveryPartnerMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(1);
            deliveryPartnerMap.put(partnerId,deliveryPartner);

            List<String> list = new ArrayList<>();
            list.add(orderId);
            orderPartnerMap.put(partnerId,list);
        }

    }

    public Order getOrderFromDB(String orderId) {
        if(orderMap.containsKey(orderId)){
            return orderMap.get(orderId);
        }else{
            return null;
        }
    }

    public DeliveryPartner getDeliveryPartnerFromDB(String partnerId) {
        if(deliveryPartnerMap.containsKey(partnerId)){
            return deliveryPartnerMap.get(partnerId);
        }else{
            return null;
        }
    }

    public Integer getOrderCountFromID(String partnerId) {

        DeliveryPartner deliveryPartner = deliveryPartnerMap.get(partnerId);
        return deliveryPartner.getNumberOfOrders();
    }

    public List<String> getOrderListFromID(String partnerId) {
        if(!orderPartnerMap.containsKey(partnerId)){
            return new ArrayList<>();
        }

        return orderPartnerMap.get(partnerId);
    }

    public List<String> getAllOrdersFromDB() {

        List<String> list = new ArrayList<>();
        for(String str : orderMap.keySet()){
            list.add(str);
        }
        return list;
    }


    public int getOrderCount() {
        return orderMap.size();
    }

    public int getAssignedOrderCount() {
        int count = 0;
        for(String str : orderPartnerMap.keySet()){
            List<String> list = orderPartnerMap.get(str);
            count+=list.size();
        }
        return count;
    }

    public Integer getRemainingNoOfOrders(int timeLimit, String partnerId) {
        int count = 0;
        for(String str: orderPartnerMap.get(partnerId)){
            Order order = orderMap.get(str);
            if(order.getDeliveryTime()>timeLimit){
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeOfPartner(String partnerId) {
        int max = 0;
        for(String str: orderPartnerMap.get(partnerId)){
            Order order = orderMap.get(str);
            max = Math.max(max,order.getDeliveryTime());
        }
        return max;
    }

    public void deletePartnerFromDB(String partnerId) throws Exception {
        if(!deliveryPartnerMap.containsKey(partnerId)){
            throw new Exception("Delivery Partner Doesn't exist");
        }
        deliveryPartnerMap.remove(partnerId);
        orderPartnerMap.remove(partnerId);
    }

    public void deleteOrderFromDB(String orderId) throws Exception {
        if(!orderMap.containsKey(orderId)){
            throw new Exception("Order doesnt Exist");
        }
        for(String str: orderPartnerMap.keySet()){
            for(String res: orderPartnerMap.get(str)){
                if(res.equals(orderId)){
                    //un assign order from partner
                    List<String> list = orderPartnerMap.get(str);
                    list.remove(orderId);
                    orderPartnerMap.put(str,list);
                    //decrease the no of order assigned to the partner
                    DeliveryPartner deliveryPartner = deliveryPartnerMap.get(str);
                    int number = deliveryPartner.getNumberOfOrders()-1;
                    deliveryPartner.setNumberOfOrders(number);
                    deliveryPartnerMap.put(str,deliveryPartner);
                    //delete order from orderMap
                }
            }
        }
        orderMap.remove(orderId);
    }
}
