package com.shakti.kisanmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shakti.kisanmarket.Prevalent.Prevalent;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check ="";
    private TextView pageTitle,titleQues;
    private EditText phoneNo,ques_1,ques_2;
    private Button verify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");
        pageTitle = (TextView)findViewById(R.id.total_tv);
        titleQues = (TextView)findViewById(R.id.title_ques);
        phoneNo = (EditText) findViewById(R.id.find_phone);
        ques_1 = (EditText)findViewById(R.id.question_1);
        ques_2 = (EditText)findViewById(R.id.question_2);
        verify_btn = (Button) findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNo.setVisibility(View.GONE);
        if(check.equals("setting"))
        {
            pageTitle.setText("Set Question");
            titleQues.setText("Set Answers for the Question?");
            verify_btn.setText("Set");
            displayPreviouAnswer();
            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                   setAnswer();
                }
            });
        }
        else if(check.equals("login"))
        {
            phoneNo.setVisibility(View.VISIBLE);
            verify_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verify();
                }
            });
        }

    }

    private void verify()
    {
        final String phone = phoneNo.getText().toString();
        final String ans_1 = ques_1.getText().toString().toLowerCase();
        final String ans_2 = ques_2.getText().toString().toLowerCase();

        if(!phone.equals("") && !ans_1.equals("") && !ans_2.equals(""))
        {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.exists())
                    {
                        String mphone = snapshot.child("phone").getValue().toString();
                        if (snapshot.hasChild("Security Question"))
                        {
                            String ans1 = snapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2 = snapshot.child("Security Question").child("answer2").getValue().toString();
                            if(!ans1.equals(ans_1))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "First Answer Wrong ", Toast.LENGTH_SHORT).show();
                            }
                            else   if(!ans2.equals(ans_2))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Second Answer Wrong ", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().equals(""))
                                        {
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(ResetPasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                     dialog.cancel();
                                    }
                                });
                                builder.show();

                            }

                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set security questions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number not Exist.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
            else {
            Toast.makeText(this, "Please Complete form", Toast.LENGTH_SHORT).show();
        }


    }

    private void displayPreviouAnswer()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    ques_1.setText(ans1);
                    ques_2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAnswer()
    {
        String ans_1 = ques_1.getText().toString().toLowerCase();
        String ans_2 = ques_2.getText().toString().toLowerCase();
        if(ans_1.equals("") && ans_2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Answer both Questions", Toast.LENGTH_SHORT).show();
        }
        else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentOnlineUser.getPhone());
            HashMap<String,Object> userdataMap =new HashMap<>();
            userdataMap.put("answer1",ans_1);
            userdataMap.put("answer2",ans_2);

            ref.child("Security Question").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Answer for Security Question Set Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
