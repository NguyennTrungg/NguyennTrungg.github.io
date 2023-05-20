package com.example.petshop.model;

public class PetModel {
    private String key;
    private String img;
    private String title;
    private String age;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public PetModel(){
    }

    public PetModel(String key, String age, String img, String title) {
        this.key = key;
        this.img = img;
        this.title = title;
        this.age = age;
    }
}
