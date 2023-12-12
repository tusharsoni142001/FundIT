package com.example.fundit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent iHome = new Intent(MainActivity.this,HomeScreen.class);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {

                startActivity(iHome);

            }
        },2000);

    }
}