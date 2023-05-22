package com.example.petshop.model;

public class UserModel {
    private String name, phone, address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserModel(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}
