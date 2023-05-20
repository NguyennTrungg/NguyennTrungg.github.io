package com.example.petshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petshop.model.PetDetailModel;
import com.example.petshop.model.PetModel;
import com.example.petshop.PetDetail;
import com.example.petshop.R;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder>{
    private List<PetModel> mPets;
    private Context context;

    public PetAdapter(Context context, List<PetModel> list) {
        this.context = context;
        this.mPets = list;
    }
    public void setData(List<PetModel> list){
        this.mPets = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetModel pet = mPets.get(position);
        if(pet == null)
            return;

        Glide.with(context).load(pet.getImg()).into(holder.imgPet);
        holder.txtName.setText(pet.getTitle());
        holder.txtAge.setText(new StringBuilder("Age: ").append(pet.getAge()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PetDetail.class);
                intent.putExtra("key", pet.getKey());
                intent.putExtra("image", pet.getImg());
                intent.putExtra("name", pet.getTitle());
                intent.putExtra("age", pet.getAge());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPets.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPet;
        private TextView txtName;
        private TextView txtAge;
        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPet = itemView.findViewById(R.id.img_pet);
            txtName = itemView.findViewById(R.id.txt_name);
            txtAge = itemView.findViewById(R.id.txt_age);
        }
    }
}
