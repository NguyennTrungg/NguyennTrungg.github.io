package com.example.petshop.listener;

import com.example.petshop.model.PetModel;


import java.util.List;

public interface IPetLoadListener {
    void onPetLoadSuccess(String category, List<PetModel> petList);
    void onPetLoadFailed(String message);
}
