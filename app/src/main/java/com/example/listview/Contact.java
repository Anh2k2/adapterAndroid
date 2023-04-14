package com.example.listview;

import java.util.Comparator;

public class Contact {
    private int id;

    private String images;

    private String name;
    private String lastName;

    private String phone;

    public Contact(){
    }

    public Contact(int id, String images, String name, String phone){
        this.id = id;
        this.images = images;
        this.name = name;
        this.phone = phone;
        String[] str = name.split("\\s+");
        this.lastName = str[str.length-1];

    }
    
    public Contact(int id, String images) {
        this.id = id;
        this.images = images;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

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

    public static class NameOrder implements Comparator<Contact> {
        @Override
        public int compare(Contact a, Contact b) {
            return a.lastName.compareTo(b.lastName);
        }
    }

    public static class PhoneOrder implements Comparator<Contact> {

        @Override
        public int compare(Contact a, Contact b) {
            return a.phone.compareTo(b.phone);
        }
    }

}
