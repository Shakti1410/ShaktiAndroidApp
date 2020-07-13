package com.shakti.kisanmarket.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.Users;
import com.shakti.kisanmarket.R;

public class UsersDetailsActivity extends AppCompatActivity {

    private RecyclerView userList;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_details);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userList = findViewById(R.id.admin_user_list);
        userList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(userRef,Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users,UserDetailViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, UserDetailViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserDetailViewHolder holder, int i, @NonNull Users model) {
                        holder.userName.setText("Name :" + model.getName());
                        holder.userPhone.setText("Phone :" + model.getPhone());
                        holder.userPassword.setText("Password :" + model.getPassword());
                        holder.useraddress.setText("Address :" + model.getAddress());

                    }

                    @NonNull
                    @Override
                    public UserDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_layout,parent,false);
                        return new UserDetailViewHolder(view);
                    }
                };
        userList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class UserDetailViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhone,userPassword,useraddress;
        public UserDetailViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name_list);
            userPhone = itemView.findViewById(R.id.user_phone_list);
            userPassword = itemView.findViewById(R.id.user_password_list);
            useraddress = itemView.findViewById(R.id.user_address_list);


        }
    }
}
