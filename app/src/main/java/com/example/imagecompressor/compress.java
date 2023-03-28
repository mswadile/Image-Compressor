package com.example.imagecompressor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class compress extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bmap;
    private boolean selected = false;
    private TextView infoTxt;
    String imgName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress);

        Button uploadBtn1,compressBtn;
        uploadBtn1 = findViewById(R.id.uploadBtn1);
        imageView = findViewById(R.id.imageView);
        compressBtn = findViewById(R.id.compressBtn);
        infoTxt = findViewById(R.id.imgInfoTxt);

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
                    Cursor returnCursor =
                            getContentResolver().query(selectedImage, null, null, null, null);
                    assert returnCursor != null;
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    returnCursor.moveToFirst();
                    imgName = returnCursor.getString(nameIndex);
                    returnCursor.close();
                    bmap = BitmapFactory.decodeStream(imageStream);
                    selected = true;
                    imageView.setImageURI(selectedImage);
                    String infoStr = "Image name: " +
                              imgName +
                            "\nImage Size: " +
                            Formatter.formatShortFileSize(this, bmap.getByteCount()) +
                            "\nImage Diamentions: " +
                            bmap.getHeight() + " X " + bmap.getWidth() +
                            "\nImage type: JPEG";
                    infoTxt.setText(infoStr);
                }
            }
    );

    private void genCode(HuffmanEncoder.HuffmanNode node ,String code,Map<Integer,String> codes){
        if(node.isLeaf()){
            codes.put(node.value,code);
        }else{
            genCode(node.left,code + "0" , codes);
            genCode(node.right,code + "1" , codes);
        }
    }

    private static int getKey(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }

    private BitSet encodeImage(Bitmap bitmap,Map<Integer,String> codes){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        BitSet bits = new BitSet();
        int bitIndex = 0;

        for(int i=0;i < height;i++){
            for(int j=0;j < width;j++){
                //get RGB values of pixel
                int color = bmap.getPixel(j,i);
                int red = Color.red(color);
                int blue = Color.blue(color);
                int green = Color.green(color);
                String code =  codes.get(getKey(red,green,blue));
                for(int k = 0;k < code.length();k++){
                    char c = code.charAt(k);
                    if(c == '1'){
                        bits.set(bitIndex);
                    }
                    bitIndex++;
                }
            }
        }
        return bits;
    }


    private void onClick(View view) {
        HashMap<Integer, Integer> pixmap = new HashMap<>();
        if (!selected) {
            Toast msg = Toast.makeText(getApplicationContext(), "Please select Image First", Toast.LENGTH_SHORT);
            msg.show();
        } else {
            int height = bmap.getHeight();
            int width = bmap.getWidth();
            int[] pixels = new int[width * height];
            // get pixel values
            bmap.getPixels(pixels,0,width,0,0,width,height);

            // Calculate the frequency of each pixel occurrence
            //Create a map and set the frequency of pixel color ignoring alpha channel
            for(int pixel:pixels){
                int color = pixel & 0x00FFFFFF; //ignore alpha channel
                if(!pixmap.containsKey(color)){
                    pixmap.put(color,0);
                }
                pixmap.put(color,pixmap.get(color) + 1);
            }

            Log.d("alpha",pixmap.toString());
            // Build huffman tree
            PriorityQueue<HuffmanEncoder.HuffmanNode> pq = new PriorityQueue<>();
            for(Map.Entry<Integer,Integer> entry : pixmap.entrySet()){
                int color = entry.getKey();
                int frequency = entry.getValue();
                HuffmanEncoder.HuffmanNode node = new HuffmanEncoder.HuffmanNode(color,frequency,null,null);
                pq.offer(node);
            }

            while(pq.size() > 1){
                //get two nodes with lowest frequency count
                HuffmanEncoder.HuffmanNode left = pq.poll();
                HuffmanEncoder.HuffmanNode right = pq.poll();

                //create a new node with sum of the frequency count
                HuffmanEncoder.HuffmanNode parent = new HuffmanEncoder.HuffmanNode(0, left.frequency + right.frequency,left,right);

                //add new node back to the priority queue
                pq.offer(parent);
            }

            //the last remaining node is root node of huffman tree
            HuffmanEncoder.HuffmanNode root = pq.poll();

            //Encode the Image Using Huffman tree
            //Generate codes
            Map<Integer,String> codes = new HashMap<>();
            genCode(root,"",codes);
            //Encode
            BitSet bits = encodeImage(bmap,codes);

            //Save File to the Storage
            //Create a filename
            File sDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(sDir, imgName);

            //Write to the file
            try (FileOutputStream out = new FileOutputStream(file)) {
                byte[] bytes = bits.toByteArray();
                out.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Intent to notify media scanner
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            // Send an intent to share the file
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("*/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            this.startActivity(Intent.createChooser(shareIntent, "Share file using:"));

        }
    }
}
