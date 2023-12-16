package com.example.fundit;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class CompanyForm extends AppCompatActivity {
    TextView companyName ;
    EditText Contact,AbtCompany,foundedyear,et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.companyform);

        companyName = findViewById(R.id.et_companyname);
        et_email = findViewById(R.id.et_email);
        foundedyear = findViewById(R.id.founded_year);
        AbtCompany = findViewById(R.id.et_about);

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




    }
}
