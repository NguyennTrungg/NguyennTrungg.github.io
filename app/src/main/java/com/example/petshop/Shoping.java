package com.example.petshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.adapter.MyItemAdapter;
import com.example.petshop.evenbus.MyUpdateCartEvent;
import com.example.petshop.listener.ICartLoadListener;
import com.example.petshop.listener.IItemLoadListener;
import com.example.petshop.model.CartModel;
import com.example.petshop.model.ItemModel;
import com.example.petshop.utils.SpaceItemDecoration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Shoping extends AppCompatActivity implements IItemLoadListener, ICartLoadListener {
    @BindView(R.id.recyclerDrink)
    RecyclerView recycler_drink;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.Badge)
    NotificationBadge badge;
    @BindView(R.id.btCart)
    FrameLayout btCart;

    IItemLoadListener itemLoadListener;
    ICartLoadListener cartLoadListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoping);

        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.nav);
        botNav.setSelectedItemId(R.id.Shopping);
        botNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.Shopping:
                        break;
                    case R.id.Service:
                        startActivity(new Intent(getApplicationContext(), Services.class));
                        break;
                    case R.id.Other:
                        startActivity(new Intent(getApplicationContext(), Other.class));
                        break;
                }
                return true;
            }
        });


        init();
        LoadItemFromFirebase();
        CountCartItem();
    }

    private void LoadItemFromFirebase() {
        List<ItemModel> itemModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Item")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                                ItemModel itemModel = itemSnapshot.getValue(ItemModel.class);
                                itemModel.setKey(itemSnapshot.getKey());
                                itemModels.add(itemModel);
                            }
                            itemLoadListener.onItemLoadSuccess(itemModels);
                        }
                        else {
                            itemLoadListener.onItemLoadFailed("Can't find Item");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        itemLoadListener.onItemLoadFailed(error.getMessage());
                    }
                });
    }

    private void init() {
        ButterKnife.bind(this);

        itemLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recycler_drink.setLayoutManager(gridLayoutManager);
        recycler_drink.addItemDecoration(new SpaceItemDecoration());

        btCart.setOnClickListener(v -> startActivity(new Intent(this, Cart.class)));
    }

    @Override
    public void onItemLoadSuccess(List<ItemModel> itemModelList) {
        MyItemAdapter adapter = new MyItemAdapter(this, itemModelList, cartLoadListener);
        recycler_drink.setAdapter(adapter);
    }

    @Override
    public void onItemLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
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
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CountCartItem();
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
}
