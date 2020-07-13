package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.shakti.kisanmarket.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageview;
    private EditText fullName,phoneNumber,addres;
    private TextView profilechangebtn,closebtn,updatebtn;
    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private String check = "";
    private ProgressDialog loadingbar;
    private Button security_ques_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageview = (CircleImageView)findViewById(R.id.setting_profile_image);
        fullName = (EditText)findViewById(R.id.setting_fullName);
        phoneNumber = (EditText) findViewById(R.id.setting_phone);
        addres = (EditText) findViewById(R.id.setting_address);
        profilechangebtn = (TextView)findViewById(R.id.profile_img_change);
        closebtn = (TextView)findViewById(R.id.close_setting);
        updatebtn = (TextView) findViewById(R.id.update_setting);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        loadingbar = new ProgressDialog(this);
        security_ques_btn = (Button)findViewById(R.id.security_question);
        UseerInfoDisplay(profileImageview,fullName,phoneNumber,addres);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.equals("clicked"))
                {
                    UserInfoSaved();
                }
                else
                {
                    UpdateInfo();
                }
            }
        });

        profilechangebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check ="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1).
                        start(SettingsActivity.this);
            }
        });
        security_ques_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","setting");
                startActivity(intent);
            }
        });
    }

    private void UpdateInfo()
    {
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap =new HashMap<>();
        userMap.put("name",fullName.getText().toString());
        userMap.put("address",addres.getText().toString());
        userMap.put("phoneOrder",phoneNumber.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Information Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri= result.getUri();
            profileImageview.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Image not select,try again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }


    }

    private void UserInfoSaved()
    {
      if(TextUtils.isEmpty(fullName.getText().toString()))
      {
          Toast.makeText(this, "Enter Name...", Toast.LENGTH_SHORT).show();
      }
       else if(TextUtils.isEmpty(phoneNumber.getText().toString()))
        {
            Toast.makeText(this, "Enter Phone Number...", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(addres.getText().toString()))
        {
            Toast.makeText(this, "Enter Address...", Toast.LENGTH_SHORT).show();
        }
       else if(check.equals("clicked"))
      {
          uploadImage();

      }

    }

    private void uploadImage()
    {
        loadingbar.setTitle("Update Profile");
        loadingbar.setMessage("Please wait, while we are updating account information");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        if(imageUri != null)
        {
            final StorageReference fileRef = storageReference.child(Prevalent.currentOnlineUser.getPhone()+".jpg");
                 uploadTask = fileRef.putFile(imageUri);
                 uploadTask.continueWithTask(new Continuation() {
                     @Override
                     public Object then(@NonNull Task task) throws Exception {
                         if(!task.isSuccessful())
                         {
                             throw task.getException();
                         }
                         return fileRef.getDownloadUrl();

                     }
                 }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                     @Override
                     public void onComplete(@NonNull Task<Uri> task) {

                         if(task.isSuccessful())
                         {
                             Uri downloadUrl = task.getResult();
                             myUrl= downloadUrl.toString();
                             DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("Users");
                             HashMap<String,Object> userMap =new HashMap<>();
                             userMap.put("name",fullName.getText().toString());
                             userMap.put("address",addres.getText().toString());
                             userMap.put("phoneOrder",phoneNumber.getText().toString());
                             userMap.put("image",myUrl);
                             ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                             loadingbar.dismiss();
                             startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                             Toast.makeText(SettingsActivity.this, "Information Updated Successfully", Toast.LENGTH_SHORT).show();
                             finish();
                         }
                         else
                         {
                             loadingbar.dismiss();
                             Toast.makeText(SettingsActivity.this, "Something went wrong, please try again after sometime", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
        }
        else {
            Toast.makeText(this, "Image not select,try again...", Toast.LENGTH_SHORT).show();
        }
    }

    private void UseerInfoDisplay(final CircleImageView profileImageview, final EditText fullName, final EditText phoneNumber, final EditText addres)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address1 = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageview);
                        fullName.setText(name);
                        phoneNumber.setText(phone);
                        addres.setText(address1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
