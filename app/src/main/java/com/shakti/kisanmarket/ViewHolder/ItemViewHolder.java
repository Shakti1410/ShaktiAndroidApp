package com.shakti.kisanmarket.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shakti.kisanmarket.Inteface.ItemClickListner;
import com.shakti.kisanmarket.R;


public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductName,txtproductDescription,txtproductprice,txtproductState,review,txtquantity;
    public ImageView imageView;
    public ItemClickListner listner;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        txtproductName = (TextView)itemView.findViewById(R.id.product_seller_name);
        txtproductDescription = (TextView)itemView.findViewById(R.id.product_seller_Descrip);
        txtproductprice = (TextView)itemView.findViewById(R.id.product_seller_priceItem);
        txtquantity = (TextView)itemView.findViewById(R.id. product_seller_quantityitm);
        txtproductState = (TextView)itemView.findViewById(R.id.product_state);
        review = (TextView)itemView.findViewById(R.id.product_review_btn1);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }
    @Override
    public void onClick(View v)
    {
        listner.onClick(v,getAdapterPosition(),false);
    }
}
