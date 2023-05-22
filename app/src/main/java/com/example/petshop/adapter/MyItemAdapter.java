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
import com.example.petshop.ItemDetail;
import com.example.petshop.R;
import com.example.petshop.listener.ICartLoadListener;
import com.example.petshop.listener.IRecyclerViewClickListner;
import com.example.petshop.model.ItemModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyItemViewHolder> {

    private Context context;
    private List<ItemModel> itemModelList;
    private ICartLoadListener iCartLoadListener;

    public MyItemAdapter(Context context, List<ItemModel> itemModelList, ICartLoadListener iCartLoadListener) {
        this.context = context;
        this.itemModelList = itemModelList;
        this.iCartLoadListener = iCartLoadListener;
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyItemViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        ItemModel model = itemModelList.get(position);
        Glide.with(context)
                .load(itemModelList.get(position).getImage())
                .into(holder.imageView);
        holder.txtPrice.setText(new StringBuilder("$").append(itemModelList.get(position).getPrice()));
        holder.txtName.setText(new StringBuilder().append(itemModelList.get(position).getName()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("key", model.getKey());
                intent.putExtra("image", model.getImage());
                intent.putExtra("name", model.getName());
                intent.putExtra("price", model.getPrice());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class MyItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        IRecyclerViewClickListner listner;

        public void setListner(IRecyclerViewClickListner listner) {
            this.listner = listner;
        }

        private Unbinder unbinder;
        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listner.onRecyclerViewClick(v, getAdapterPosition());
        }
    }
}
