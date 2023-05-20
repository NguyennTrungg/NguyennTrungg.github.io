package com.example.petshop.listener;

import com.example.petshop.model.ItemDetailModel;

import java.util.List;

public interface IItemDetailLoadListener {
    void onItemDetailLoadSuccess(List<ItemDetailModel> itemDetailList);
    void onItemDetailLoadFailed(String message);
}
