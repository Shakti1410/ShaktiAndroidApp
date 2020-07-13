package com.shakti.kisanmarket.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.shakti.kisanmarket.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddNewProductActivity extends AppCompatActivity {
    private String categoryName, description,price,quantity, pname, savecurrentDate,savecurrentTime,finalprice;
    private ImageView product_image;
    private Button add_new_product_btn;
    private EditText productName,productDescription,productprice,productQuantity;
    private static final int galleryPick= 1;
    private Uri imageUri;
    private String productRandomKey,downloadImgUri;
    private StorageReference productImgRef;
    private DatabaseReference productRef,sellersRef;
    private ProgressDialog loadingbar;
    private String sName,sAddress,sPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);


        categoryName = getIntent().getExtras().get("category").toString();

        product_image = (ImageView)findViewById(R.id.select_product_image);
        add_new_product_btn = (Button)findViewById(R.id.add_new_product);
        productName = (EditText) findViewById(R.id.product_name);
        productDescription = (EditText) findViewById(R.id.product_description);
        productQuantity = (EditText) findViewById(R.id.product_quantity);
        productprice = (EditText) findViewById(R.id.product_price);
        productImgRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellersRef = FirebaseDatabase.getInstance().getReference().child("Seller");
        loadingbar = new ProgressDialog(this);

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGellary();
            }
        });

        add_new_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        sellersRef.child(Prevalent.currentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    sName = snapshot.child("name").getValue().toString();
                    sPhone = snapshot.child("phone").getValue().toString();
                    sAddress = snapshot.child("address").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ValidateProductData()
    {

        description = productDescription.getText().toString();
        price = productprice.getText().toString();
        pname = productName.getText().toString();
        quantity = productQuantity.getText().toString();
        int p = Integer.valueOf(price);
        int fp = (int) (p + 0.15*p);
        finalprice = String.valueOf(fp);

        if(imageUri == null)
        {
            Toast.makeText(this, "Please Select Product Image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Please Enter Product Description ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Please Enter Product Price ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pname))
        {
            Toast.makeText(this, "Please Enter Product Name ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(quantity))
        {
            Toast.makeText(this, "Please Enter Product Quantity ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInfo();
        }
    }

    private void StoreProductInfo() {
        loadingbar.setTitle("Add New Product");
        loadingbar.setMessage("Please wait, while we are adding the new product");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        savecurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        savecurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = savecurrentDate+savecurrentTime;

        final StorageReference filepath = productImgRef.child(imageUri.getLastPathSegment() + productRandomKey+".jpg");

        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String messge = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error" + messge, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(SellerAddNewProductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }
                            downloadImgUri = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {
                            downloadImgUri = task.getResult().toString();
                            Toast.makeText(SellerAddNewProductActivity.this, "got the Product Uri Image Successfully.", Toast.LENGTH_SHORT).show();
                             SaveProductInfo();
                        }

                    }
                });
            }
        });

    }

    private void SaveProductInfo() {
        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put("pid",productRandomKey);
        productmap.put("date",savecurrentDate);
        productmap.put("time",savecurrentTime);
        productmap.put("description",description);
        productmap.put("image",downloadImgUri);
        productmap.put("category",categoryName);
        productmap.put("price",finalprice);
        productmap.put("pname",pname);
        productmap.put("quantity",quantity);
        productmap.put("sellerName",sName);
        productmap.put("sellerPhone",sPhone);
        productmap.put("sellerAddress",sAddress);
        productmap.put("categoryState",categoryName+"Not Approved");
        productmap.put("nameState","Not Approved"+pname);
        productmap.put("productState","Not Approved");

        productRef.child(productRandomKey).updateChildren(productmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    loadingbar.dismiss();
                    Toast.makeText(SellerAddNewProductActivity.this, "Product added Successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SellerAddNewProductActivity.this, SellerProductsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    loadingbar.dismiss();
                    String messesge = task.getException().toString();
                    Toast.makeText(SellerAddNewProductActivity.this, "Error :" + messesge, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenGellary() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode  == RESULT_OK && data != null)
        {
            imageUri = data.getData();
            product_image.setImageURI(imageUri);
        }
    }


}
