package com.shakti.kisanmarket.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shakti.kisanmarket.Inteface.ItemClickListner;
import com.shakti.kisanmarket.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductName,txtproductDescription,txtproductprice,sellername,sellerphone,review,txtquantity;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtproductName = (TextView)itemView.findViewById(R.id.product_name);
        txtproductDescription = (TextView)itemView.findViewById(R.id.product_Descrip);
        txtproductprice = (TextView)itemView.findViewById(R.id.product_priceItem);
        txtquantity = (TextView)itemView.findViewById(R.id.product_quantityitm);
        sellername = (TextView)itemView.findViewById(R.id.product_seller_name);
        sellerphone = (TextView)itemView.findViewById(R.id.product_seller_phone);
        review = (TextView)itemView.findViewById(R.id.product_review_btn);

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
