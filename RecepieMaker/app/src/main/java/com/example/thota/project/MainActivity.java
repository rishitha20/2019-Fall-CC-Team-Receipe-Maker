package com.example.thota.aseproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

//This page is the display page of the logo

public class MainActivity extends AppCompatActivity {
public final int TIME_OUT= 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.CYAN);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent pass= new Intent(MainActivity.this, Login.class);
                startActivity(pass);
                finish();
            }
        },TIME_OUT);
    }
}
