package com.shakti.kisanmarket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.AdminOrders;
import com.shakti.kisanmarket.Model.Users;
import com.shakti.kisanmarket.R;

public class SellerDetailsActivity extends AppCompatActivity {
    private RecyclerView sellerList;
    private DatabaseReference sellerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);

        sellerRef = FirebaseDatabase.getInstance().getReference().child("Seller");
        sellerList = findViewById(R.id.admin_seller_list);
        sellerList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(sellerRef,Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users,SellerDetailViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, SellerDetailViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerDetailViewHolder holder, int i, @NonNull Users model) {
                        holder.sellerName.setText("Name :" + model.getName());
                        holder.sellerPhone.setText("Phone :" + model.getPhone());
                        holder.sellerPassword.setText("Password :" + model.getPassword());
                        holder.selleraddress.setText("Address :" + model.getAddress());

                    }

                    @NonNull
                    @Override
                    public SellerDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_list_layout,parent,false);
                        return new SellerDetailViewHolder(view);
                    }
                };
        sellerList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class SellerDetailViewHolder extends RecyclerView.ViewHolder
    {
        public TextView sellerName,sellerPhone,sellerPassword,selleraddress;
        public SellerDetailViewHolder(@NonNull View itemView)
        {
            super(itemView);

            sellerName = itemView.findViewById(R.id.seller_name_list);
            sellerPhone = itemView.findViewById(R.id.seller_phone_list);
            sellerPassword = itemView.findViewById(R.id.seller_password_list);
            selleraddress = itemView.findViewById(R.id.seller_address_list);


        }
    }
}
