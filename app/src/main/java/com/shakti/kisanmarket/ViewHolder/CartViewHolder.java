package com.shakti.kisanmarket.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shakti.kisanmarket.Inteface.ItemClickListner;
import com.shakti.kisanmarket.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName,txtProductprice,txtProductquantity,txtproductsellername,txtsellerphone;
    private ItemClickListner itemClickListner;
    public Button product_feedback;



    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductprice = itemView.findViewById(R.id.cart_product_price);
        txtProductquantity = itemView.findViewById(R.id.cart_product_quantity);
        txtsellerphone = itemView.findViewById(R.id.cart_product_seller_phone);
        txtproductsellername = itemView.findViewById(R.id.cart_product_seller);
        product_feedback = itemView.findViewById(R.id.product_feedback);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
