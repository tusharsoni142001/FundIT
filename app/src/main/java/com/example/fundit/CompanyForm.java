package com.example.fundit;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyForm extends AppCompatActivity {
    TextView companyName ;
    EditText cemail,AbtCompany,foundedyear,cwebsite,dpiitno,csize;
    Button submitbtn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyform);

        companyName = findViewById(R.id.et_companyname);
        cemail = findViewById(R.id.et_email);
        foundedyear = findViewById(R.id.founded_year);
        cwebsite = findViewById(R.id.c_website);
        dpiitno = findViewById(R.id.c_dpiitno);
        AbtCompany = findViewById(R.id.et_about);
        csize=findViewById(R.id.companysize);

        String uid = currentUser.getUid();
        submitbtn=findViewById(R.id.btnsubmit);

        //Retrieving company details from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Companies");

        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String cname = "" + dataSnapshot1.child("companyName").getValue();
                    String foundedYear = "" + dataSnapshot1.child("foundedYear").getValue();
                    String emaill = "" + dataSnapshot1.child("companyEmail").getValue();
                    String companyWebsite = "" + dataSnapshot1.child("companyWebsite").getValue();
                    String companySize = "" + dataSnapshot1.child("companySize").getValue();
                    String DPIITno=""+dataSnapshot1.child("DPIITno").getValue();
                    String companyAbout=""+dataSnapshot1.child("companyAbout").getValue();


                    if (dataSnapshot1.hasChild("foundedYear")) {
                        // If field exists, retrieve its value
                        foundedYear = "" + dataSnapshot1.child("foundedYear").getValue();

                        // Check if the retrieved value is not null before using it
                        if (foundedYear != null) {
                            foundedyear.setText(foundedYear);
                        }
                    }


                    if (dataSnapshot1.hasChild("companyEmail")) {
                        // If field exists, retrieve its value
                        emaill = "" + dataSnapshot1.child("companyEmail").getValue();

                        // Check if the retrieved value is not null before using it
                        if (emaill != null) {
                            cemail.setText(emaill);
                        }
                    }


                    if (dataSnapshot1.hasChild("companyWebsite")) {
                        // If field exists, retrieve its value
                        companyWebsite = "" + dataSnapshot1.child("companyWebsite").getValue();

                        // Check if the retrieved value is not null before using it
                        if (companyWebsite != null) {
                            cwebsite.setText(companyWebsite);
                        }
                    }

                    if (dataSnapshot1.hasChild("companyWebsite")) {
                        // If field exists, retrieve its value
                        companySize = "" + dataSnapshot1.child("companyWebsite").getValue();

                        // Check if the retrieved value is not null before using it
                        if (companySize != null) {
                            csize.setText(companySize);
                        }
                    }

                    if (dataSnapshot1.hasChild("DPIITno")) {
                        // If field exists, retrieve its value
                        DPIITno = "" + dataSnapshot1.child("DPIITno").getValue();

                        // Check if the retrieved value is not null before using it
                        if (DPIITno != null) {
                            dpiitno.setText(DPIITno);
                        }
                    }

                    if (dataSnapshot1.hasChild("companyAbout")) {
                        // If field exists, retrieve its value
                        companyAbout = "" + dataSnapshot1.child("companyAbout").getValue();

                        // Check if the retrieved value is not null before using it
                        if (companyAbout != null) {
                            AbtCompany.setText(companyAbout);
                        }
                    }

                    companyName.setText(cname);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        foundedyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        CompanyForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Extracting only the year and setting it to the foundedyear EditText.
                                foundedyear.setText(String.valueOf(year));
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyEmail = cemail.getText().toString();
                String companyFoundedYear = foundedyear.getText().toString();
                String companyWebsite = cwebsite.getText().toString();
                String companyDpiitno = dpiitno.getText().toString();
                String aboutCompany = AbtCompany.getText().toString();
                String companySize = csize.getText().toString();

                //Regex for DPIIT number
                String regexPattern = "^(DIPP|DPIIT)\\d{5}$";
                // Create a Pattern object
                Pattern pattern = Pattern.compile(regexPattern);
                // Create a Matcher object
                Matcher matcher = pattern.matcher(companyDpiitno);

                /*
                if(!matcher.matches())
                {
                    Toast.makeText(CompanyForm.this,"Invalid DPIIT Number",Toast.LENGTH_LONG).show();
                }*/

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Companies").child(uid);


                // Assuming hashMap contains the updated and new data
                HashMap<String, Object> updatedData = new HashMap<>();

                // New fields to add
                updatedData.put("companyEmail", companyEmail);
                updatedData.put("foundedYear", companyFoundedYear);
                updatedData.put("companyWebsite", companyWebsite);
                updatedData.put("DPIITno", companyDpiitno);
                updatedData.put("companyAbout", aboutCompany);
                updatedData.put("companySize", companySize);

                // Update the existing data and add new fields
                reference.updateChildren(updatedData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CompanyForm.this, "Company data updated successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CompanyForm.this, "Failed to update company data", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}