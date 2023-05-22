package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.petshop.evenbus.MyUpdateCartEvent;
import com.example.petshop.listener.ICartLoadListener;
import com.example.petshop.model.CartModel;
import com.example.petshop.model.ItemModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public class ItemDetail extends AppCompatActivity implements ICartLoadListener {


    ICartLoadListener cartLoadListener;
    FrameLayout btCart;
    TextView btnAdd, txtName, txt_Price, txtDetail, txtInfo;
    private String key,img,name,price;
    ImageView img_pet,btnBack;
    NotificationBadge badge;
    RelativeLayout item_detail_layout;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        CountCartItem();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_item_detail);

        init();
        LoadItemFromFirebase();
        CountCartItem();
        }

    private void LoadItemFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Item").child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String detail = ""+snapshot.child("detail").getValue();
                        String information = ""+snapshot.child("information").getValue();
                        txtDetail.setText(detail);
                        txtInfo.setText(information);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(item_detail_layout, "Error", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void init() {
        txt_Price = findViewById(R.id.txt_price);
        img_pet = findViewById(R.id.img_pet);
        txtName = findViewById(R.id.txt_name);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        img = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        txt_Price.setText(price);
        txtName.setText(name);
        Glide.with(this).load(img).into(img_pet);

        ButterKnife.bind(this);
        btCart = findViewById(R.id.btCart);
        btnAdd = findViewById(R.id.txt_addtocart);
        txtDetail = findViewById(R.id.txt_detail);
        txtInfo = findViewById(R.id.txt_infomation);
        badge = findViewById(R.id.Badge);
        item_detail_layout = findViewById(R.id.item_detail_layout);

        cartLoadListener = this;
        btCart.setOnClickListener(v -> startActivity(new Intent(this, Cart.class)));
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(new ItemModel(key,name,img,price));
            }
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }


    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        int CartSum = 0;
        for(CartModel cartModel: cartModelList)
            CartSum += cartModel.getQuantity();
        badge.setNumber(CartSum);
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(item_detail_layout, message, Snackbar.LENGTH_LONG).show();
    }

    private void CountCartItem() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot cartSnapshot : snapshot.getChildren()){
                            CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                            cartModel.setKey(cartSnapshot.getKey());
                            cartModels.add(cartModel);
                        }
                        cartLoadListener.onCartLoadSuccess(cartModels);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    private void addToCart(ItemModel itemModel) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");
        userCart.child(itemModel.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            //if user already have item in cart
                            //update quantity and total price
                            CartModel cartModel = snapshot.getValue(CartModel.class);
                            cartModel.setQuantity(cartModel.getQuantity()+1);
                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("quantity", cartModel.getQuantity());
                            updateData.put("totalPrice", cartModel.getQuantity()*Float.parseFloat(cartModel.getPrice()));

                            userCart.child(itemModel.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        cartLoadListener.onCartLoadFailed("Add to Cart success");
                                    })
                                    .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        else
                        { //if item not have in cart, add to cart
                            CartModel  cartModel = new CartModel();
                            cartModel.setName(itemModel.getName());
                            cartModel.setImage(itemModel.getImage());
                            cartModel.setKey(itemModel.getKey());
                            cartModel.setPrice(itemModel.getPrice());
                            cartModel.setQuantity(1);
                            cartModel.setTotalPrice(Float.parseFloat(itemModel.getPrice()));

                            userCart.child(itemModel.getKey())
                                    .setValue(cartModel)
                                    .addOnSuccessListener(aVoid -> {
                                        cartLoadListener.onCartLoadFailed("Add to Cart success");
                                    })
                                    .addOnFailureListener(e -> cartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }
}
