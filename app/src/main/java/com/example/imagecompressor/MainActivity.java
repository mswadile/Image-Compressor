package com.example.imagecompressor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button comBtn;
        Button exBtn;
        comBtn = findViewById(R.id.comBtn);
        exBtn = findViewById(R.id.exBtn);
        comBtn.setOnClickListener(this::comClick);
        exBtn.setOnClickListener(this::exClick);
    }

    private void comClick(View view){
        Intent compress = new Intent(MainActivity.this, compress.class);
        startActivity(compress);
    }

    private void exClick(View view){
        Intent extract = new Intent(MainActivity.this, extract.class);
        startActivity(extract);
    }
}