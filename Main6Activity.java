package com.example.admin.ebreak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import spencerstudios.com.bungeelib.Bungee;

public class Main6Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main6Activity.this, Main3Activity.class));
        finish();
        Bungee.slideRight(Main6Activity.this);
        super.onBackPressed();
    }
}
