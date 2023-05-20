package com.example.petshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.model.CategoryModel;
import com.example.petshop.adapter.PetAdapter;
import com.example.petshop.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private Context mContext;
    private List<CategoryModel> mListCategory;

    public CategoryAdapter(Context mContext, List<CategoryModel> list) {
        this.mContext = mContext;
        this.mListCategory = list;
    }

    public void setData(List<CategoryModel> list){
        this.mListCategory = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = mListCategory.get(position);
        if(category == null)
            return;

        holder.txtNameCategory.setText(category.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.rcvPet.setLayoutManager(linearLayoutManager);

        PetAdapter petAdapter = new PetAdapter(mContext,category.getPets());
        petAdapter.setData(category.getPets());
        holder.rcvPet.setAdapter(petAdapter);

    }

    @Override
    public int getItemCount() {
        if(mListCategory != null)
            return mListCategory.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNameCategory;
        private RecyclerView rcvPet;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameCategory = itemView.findViewById(R.id.txt_category);
            rcvPet = itemView.findViewById(R.id.rcv_pet);

        }
    }
}
