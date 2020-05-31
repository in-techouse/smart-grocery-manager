package com.example.smartgroceryreminder.model;

public class GroceryItems {
    private String pname, manufacture, expiry;
    private int id;

    public GroceryItems() {

    }

    public GroceryItems(String pname, String manufacture, String expiry, int id) {
        this.pname = pname;
        this.manufacture = manufacture;
        this.expiry = expiry;
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


