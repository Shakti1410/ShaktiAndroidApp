package com.shakti.kisanmarket;

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
import com.shakti.kisanmarket.Admin.AdminNewOrdersActivity;
import com.shakti.kisanmarket.Admin.AdminUserProductsActivity;
import com.shakti.kisanmarket.Model.AdminOrders;
import com.shakti.kisanmarket.Prevalent.Prevalent;

public class OrederHistoryActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oreder_history);


        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders History");
        orderList = findViewById(R.id.order_his_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef.orderByChild("id").equalTo(Prevalent.currentOnlineUser.getPhone()),AdminOrders.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrders,OrdersHistoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, OrdersHistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersHistoryViewHolder holder, final int i, @NonNull final AdminOrders model)
                    {
                        holder.userName.setText("Name :" + model.getName());
                        holder.userPhone.setText("Phone :" + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount :" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at :" + model.getDate() + " " + model.getTime());
                        holder.userShippingaddress.setText("Shipping Address :" + model.getAddress() + " "+ model.getCity());

                        holder.showOrders_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // String uID = getRef(i).getKey();
                                Intent intent= new Intent(OrederHistoryActivity.this, UserOrderHistoryProductsActivity.class);
                               // intent.putExtra("uid",uID);
                                intent.putExtra("pid",model.getPid());
                                intent.putExtra("key",model.getKey());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OrdersHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new OrdersHistoryViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class OrdersHistoryViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhone,userTotalPrice,userDateTime,userShippingaddress;
        public Button showOrders_btn;
        public OrdersHistoryViewHolder(@NonNull View itemView)
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
