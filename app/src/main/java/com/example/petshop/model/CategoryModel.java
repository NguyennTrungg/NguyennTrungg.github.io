package com.example.petshop.model;

import com.example.petshop.model.PetModel;

import java.util.List;

public class CategoryModel {
    private String nameCategory;
    private List<PetModel> Pets;
    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<PetModel> getPets() {
        return Pets;
    }

    public void setPets(List<PetModel> pets) {
        Pets = pets;
    }

    public CategoryModel(){
    }

    public CategoryModel(String nameCategory, List<PetModel> pets) {
        this.nameCategory = nameCategory;
        Pets = pets;
    }
}
