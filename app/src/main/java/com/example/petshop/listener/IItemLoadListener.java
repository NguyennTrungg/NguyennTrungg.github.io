package com.example.petshop.listener;

import com.example.petshop.model.ItemModel;

import java.util.List;

public interface IItemLoadListener {
    void onItemLoadSuccess(List<ItemModel> itemModelList);
    void onItemLoadFailed(String message);
}
