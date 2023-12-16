package com.example.fundit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class CompanyForm extends AppCompatActivity {
    TextView companName, foundedyear;
    EditText Contact,AbtCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyform);

        companName = findViewById(R.id.companyName);
        foundedyear = findViewById(R.id.foundedyear);
        Contact = findViewById(R.id.userContact);
        AbtCompany = findViewById(R.id.abtcompany);



    }
}
