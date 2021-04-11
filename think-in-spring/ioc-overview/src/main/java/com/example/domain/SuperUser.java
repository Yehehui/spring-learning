package com.example.domain;

public class SuperUser extends User {
    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SuperUser{" +
                "address='" + address + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
