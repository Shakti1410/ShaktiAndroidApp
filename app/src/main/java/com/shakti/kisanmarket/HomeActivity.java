package com.shakti.kisanmarket;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shakti.kisanmarket.Model.Products;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;

   // private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    //    getSupportActionBar().setHomeButtonEnabled(false);
     //   getSupportActionBar().setLogo(R.drawable.nav_cart);
/*
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            type = getIntent().getStringExtra("Admin");
        }
*/


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

       ActionBarDrawerToggle mtoggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);//For Hide Menu
        drawer.addDrawerListener(mtoggle);
        mtoggle.setDrawerIndicatorEnabled(true);
        mtoggle.syncState();

        View headerview = navigationView.getHeaderView(0);
        TextView usernameTextview = headerview.findViewById(R.id.user_profile_name);
        CircleImageView profileImageview = headerview.findViewById(R.id.profile_image_user);

        usernameTextview.setText(Prevalent.currentOnlineUser.getName());
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.man).into(profileImageview);

     /*   if(!type.equals("Admin"))
        {

        }*/
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_search, R.id.nav_categories,
                R.id.nav_settings, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.logout)
        {
            new AlertDialog.Builder(HomeActivity.this,R.style.Theme_AppCompat_Light_Dialog)
                    .setTitle("Logout")
                    .setMessage("Are you sure,you want to Exit?")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Paper.book().destroy();
                            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        }
                    }).setNegativeButton("Cancel",null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        return super.onOptionsItemSelected(item);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }
public void onBackPressed() {
    new AlertDialog.Builder(HomeActivity.this,
            R.style.Theme_AppCompat_Light_Dialog).setTitle("Logout")
            .setMessage("Are you sure? you want to exit?")
            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                }
            }).setNegativeButton("Cancel",null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
}
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.recycler_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
              || super.onSupportNavigateUp();
    }
}
