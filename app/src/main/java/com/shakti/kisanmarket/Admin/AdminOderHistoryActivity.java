package com.shakti.kisanmarket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.shakti.kisanmarket.OrederHistoryActivity;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.R;
import com.shakti.kisanmarket.UserOrderHistoryProductsActivity;

public class AdminOderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_oder_history);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders History");
        orderList = findViewById(R.id.order_his_list_ad);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef,AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersHistoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersHistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersHistoryViewHolder holder, int i, @NonNull final AdminOrders model) {
                        holder.userName.setText("Name :" + model.getName());
                        holder.userPhone.setText("Phone :" + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount :" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at :" + model.getDate() + " " + model.getTime());
                        holder.userShippingaddress.setText("Shipping Address :" + model.getAddress() + " "+ model.getCity());

                        holder.showOrders_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // String uID = getRef(i).getKey();
                                Intent intent= new Intent(AdminOderHistoryActivity.this, AdminOrderHistoryProductsActivity.class);
                                // intent.putExtra("uid",uID);

                                intent.putExtra("key",model.getKey());
                                intent.putExtra("id",model.getId());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersHistoryViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();

    }
    public static class AdminOrdersHistoryViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhone,userTotalPrice,userDateTime,userShippingaddress;
        public Button showOrders_btn;
        public AdminOrdersHistoryViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhone = itemView.findViewById(R.id.order_phone1_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingaddress = itemView.findViewById(R.id.order_Address_city);
            showOrders_btn = itemView.findViewById(R.id.show_all_product_btn);

        }
    }
}
