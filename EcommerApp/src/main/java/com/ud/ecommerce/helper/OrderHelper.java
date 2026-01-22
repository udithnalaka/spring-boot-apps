package com.ud.ecommerce.helper;

public class OrderHelper {

    public static void addDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread interupted: {} ", e);
        }
    }
}
