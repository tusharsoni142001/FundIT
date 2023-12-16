package com.example.fundit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class founderform  extends AppCompatActivity {
    private TextView email, name;
    EditText experience,education,about;
    CheckBox cb1,cb2,cb3,cb4;
    private Button submit;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    String uexperience,ueducation,uabout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.founderform);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Founder");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initialising the layout items
        email = findViewById(R.id.et_email);
        name = findViewById(R.id.et_name);
        experience = findViewById(R.id.et_experience);
        education = findViewById(R.id.et_education);
        about = findViewById(R.id.et_about);
        cb1=findViewById(R.id.cb_expertise_product);
        cb2=findViewById(R.id.cb_expertise_business);
        cb3=findViewById(R.id.cb_expertise_marketing);
        cb4=findViewById(R.id.cb_expertise_operation);
        submit=findViewById(R.id.btnsubmit);

        String uid = currentUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        // Retrieving user data from firebase
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String uname = "" + dataSnapshot1.child("name").getValue();
                    //String userType = "" + dataSnapshot1.child("userType").getValue();
                    String emaill = "" + dataSnapshot1.child("email").getValue();



                    if (dataSnapshot1.hasChild("experience")) {
                        // If field exists, retrieve its value
                        uexperience = "" + dataSnapshot1.child("experience").getValue();

                        // Check if the retrieved value is not null before using it
                        if (uexperience != null) {
                            experience.setText(uexperience);
                        }
                    }

                    /*
                    if (dataSnapshot1.hasChild("expertise")) {
                        // If field exists, retrieve its value
                        String uexpertise = "" + dataSnapshot1.child("expertise").getValue();

                        // Check if the retrieved value is not null before using it
                        if (uexpertise != null) {
                            experience.setText(emaill);
                        }
                    }
                    */

                    if (dataSnapshot1.hasChild("education")) {
                        // If field exists, retrieve its value
                        ueducation = "" + dataSnapshot1.child("education").getValue();

                        // Check if the retrieved value is not null before using it
                        if (ueducation != null) {
                            education.setText(ueducation);
                        }
                    }

                    if (dataSnapshot1.hasChild("about")) {
                        // If field exists, retrieve its value
                        String uabout = "" + dataSnapshot1.child("about").getValue();

                        // Check if the retrieved value is not null before using it
                        if (uabout != null) {
                            about.setText(uabout);
                        }
                    }

                    if (dataSnapshot1.hasChild("expertise")) {
                        // If field exists, retrieve its value
                        String expertise = "" + dataSnapshot1.child("expertise").getValue();

                        // Check if the retrieved value is not null before using it
                        if (uabout != null) {
                            about.setText(uabout);
                        }
                    }

                    name.setText(uname);
                    email.setText(emaill);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users").child(uid);

                uexperience=experience.getText().toString();
                ueducation=education.getText().toString();
                uabout=about.getText().toString();
                String expertise=null;

                if(cb1.isChecked())
                {
                    if(expertise==null)
                    {
                        expertise=cb1.getText().toString();
                    }
                    else
                    {
                        expertise=expertise+", "+cb1.getText().toString();
                    }

                }

                if(cb2.isChecked())
                {
                    if(expertise==null)
                    {
                        expertise=cb2.getText().toString();
                    }
                    else
                    {
                        expertise=expertise+", "+cb2.getText().toString();
                    }

                }
                if(cb3.isChecked())
                {
                    if(expertise==null)
                    {
                        expertise=cb3.getText().toString();
                    }
                    else
                    {
                        expertise=expertise+", "+cb3.getText().toString();
                    }

                }
                if(cb4.isChecked())
                {
                    if(expertise==null)
                    {
                        expertise=cb4.getText().toString();
                    }
                    else
                    {
                        expertise=expertise+", "+cb4.getText().toString();
                    }

                }


                // Assuming hashMap contains the updated and new data
                HashMap<String, Object> updatedData = new HashMap<>();



                // New fields to add
                updatedData.put("experience", uexperience);
                updatedData.put("expertise", expertise);
                updatedData.put("education", ueducation);
                updatedData.put("about",uabout);

                // Update the existing data and add new fields
                reference.updateChildren(updatedData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(founderform.this, "User updated successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(founderform.this, "Failed to update user", Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });

    }


    }