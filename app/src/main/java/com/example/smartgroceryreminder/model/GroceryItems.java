package com.example.smartgroceryreminder.model;

public class GroceryItems {
    private int id;
    private String brand, name, useage, image, manufactureDate, expiryDate, alarm;

    public GroceryItems() {
    }

    public GroceryItems(int id, String brand, String name, String useage, String image, String manufactureDate, String expiryDate, String alarm) {
        this.id = id;
        this.brand = brand;
        this.name = name;
        this.useage = useage;
        this.image = image;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.alarm = alarm;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUseage() {
        return useage;
    }

    public void setUseage(String useage) {
        this.useage = useage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}


