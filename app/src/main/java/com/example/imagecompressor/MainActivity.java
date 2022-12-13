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
    private ImageView imageView;
    private Bitmap bmap;
    private boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button uploadBtn1,compressBtn;
        uploadBtn1 = findViewById(R.id.uploadBtn1);
        imageView = findViewById(R.id.imageView);
        compressBtn = findViewById(R.id.compressBtn);

        // onclick Listener for Upload button
        uploadBtn1.setOnClickListener(view -> {
            //set intent
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imageResultLauncher.launch(intent);
        });

        //Onclick listener for compress button
        compressBtn.setOnClickListener(this::onClick);

    }

    //handling intend
    ActivityResultLauncher<Intent> imageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data  = result.getData();
                    Uri selectedImage = Objects.requireNonNull(data).getData();
                    InputStream imageStream = null;
                    try{
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                    bmap = BitmapFactory.decodeStream(imageStream);
                    selected = true;
                    imageView.setImageURI(selectedImage);
                }
            }
    );


    private void onClick(View view) {
        HashMap<Integer, Double> pixmap = new HashMap<>();
        if (!selected) {
            Toast msg = Toast.makeText(getApplicationContext(), "Please select Image First", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            int height = bmap.getHeight();
            int width = bmap.getWidth();
            long numPix = (long) height * width;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = bmap.getPixel(j, i);
                    if (pixmap.containsKey(pixel)) {
                        double tempPix;
                        tempPix = pixmap.get(pixel);
                        tempPix++;
                        pixmap.replace(pixel, tempPix);
                    } else {
                        pixmap.put(pixel, 1.0);
                    }
                }
            }

            /* calculate probability of each pixel occurence
             prob_pixel = numpix/totalnum_pixels
             prob_pixel == probability of occurence of certain pixel
             numpix == number of times pixel repeated
             totalnum_pixels == total number of pixels in image
             */

            for (Map.Entry<Integer, Double> tMapE : pixmap.entrySet()) {
                pixmap.replace(tMapE.getKey(), tMapE.getValue() / numPix);
            }

            // Build a huffman tree


        }
    }
}