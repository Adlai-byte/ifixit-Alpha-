package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICES;

import java.util.HashMap;

public class ElectricalServices {

    private HashMap<String, Double> servicePriceMap;

    public ElectricalServices() {
        servicePriceMap = new HashMap<>();
    }

    public void addService(String serviceType, double price) {
        servicePriceMap.put(serviceType, price);
    }

    public double getPrice(String serviceType) {
        return servicePriceMap.getOrDefault(serviceType, 0.0);
    }

    public boolean hasService(String serviceType) {
        return servicePriceMap.containsKey(serviceType);
    }

    public HashMap<String, Double> getServicePriceMap() {
        return servicePriceMap;
    }




}
