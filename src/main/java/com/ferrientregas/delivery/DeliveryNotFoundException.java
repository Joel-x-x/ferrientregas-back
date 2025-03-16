package com.ferrientregas.delivery;

public class DeliveryNotFoundException extends Exception {
    public DeliveryNotFoundException() { super("Delivery not found"); }
}
